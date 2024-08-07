package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.util.Wrapper;
import me.zoom.xannax.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", "Scaffold", Category.Movement);
        this.blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST);
    }

    private List blackList;
    private int future = 3;

    private boolean hasNeighbour(BlockPos blockPos) {
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos neighbour = blockPos.offset(side);
            if (!Wrapper.getWorld().getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }

        return false;
    }

    public void onUpdate() {
        if (this.isEnabled() && mc.player != null) {
            Vec3d vec3d = EntityUtil.getInterpolatedPos(mc.player, this.future);
            BlockPos blockPos = (new BlockPos(vec3d)).down();
            BlockPos belowBlockPos = blockPos.down();
            if (Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
                int newSlot = -1;

                int oldSlot;
                for(oldSlot = 0; oldSlot < 9; ++oldSlot) {
                    ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(oldSlot);
                    if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                        Block block = ((ItemBlock)stack.getItem()).getBlock();
                        if (!this.blackList.contains(block) && !(block instanceof BlockContainer) && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock() && (!(((ItemBlock)stack.getItem()).getBlock() instanceof BlockFalling) || !Wrapper.getWorld().getBlockState(belowBlockPos).getMaterial().isReplaceable())) {
                            newSlot = oldSlot;
                            break;
                        }
                    }
                }

                if (newSlot != -1) {
                    oldSlot = Wrapper.getPlayer().inventory.currentItem;
                    Wrapper.getPlayer().inventory.currentItem = newSlot;
                    if (!this.hasNeighbour(blockPos)) {
                        EnumFacing[] var11 = EnumFacing.values();
                        int var12 = var11.length;
                        int var8 = 0;

                        while(true) {
                            if (var8 >= var12) {
                                return;
                            }

                            EnumFacing side = var11[var8];
                            BlockPos neighbour = blockPos.offset(side);
                            if (this.hasNeighbour(neighbour)) {
                                blockPos = neighbour;
                                break;
                            }

                            ++var8;
                        }
                    }

                    placeBlockScaffold(blockPos);
                    Wrapper.getPlayer().inventory.currentItem = oldSlot;
                }
            }
        }
    }

    public static boolean placeBlockScaffold(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo((new Vec3d(pos)).add(0.5D, 0.5D, 0.5D)) < eyesPos.squareDistanceTo((new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D)) && canBeClicked(neighbor)) {
                Vec3d hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                if (eyesPos.squareDistanceTo(hitVec) <= 18.0625D) {
                    faceVectorPacketInstant(hitVec);
                    processRightClickBlock(neighbor, side2, hitVec);
                    Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                    mc.rightClickDelayTimer = 4;
                    return true;
                }
            }
        }

        return false;
    }

    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
        getPlayerController().processRightClickBlock(Wrapper.getPlayer(), mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }

    public static IBlockState getState(BlockPos pos) {
        return Wrapper.getWorld().getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = getNeededRotations2(vec);
        Wrapper.getPlayer().connection.sendPacket(new Rotation(rotations[0], rotations[1], Wrapper.getPlayer().onGround));
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{Wrapper.getPlayer().rotationYaw + MathHelper.wrapDegrees(yaw - Wrapper.getPlayer().rotationYaw), Wrapper.getPlayer().rotationPitch + MathHelper.wrapDegrees(pitch - Wrapper.getPlayer().rotationPitch)};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
    }
}
