package me.zoom.xannax.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;

public class SelfWeb extends Module {
    public SelfWeb(){super("SelfWeb", "SelfWeb", Category.Combat);}
    BlockPos feet;

    int d;
    public static float yaw;
    public static float pitch;
    public boolean isInBlockRange(Entity target) {
        return (target.getDistance(mc.player) <= 4.0F);
    }
    private Setting.Boolean announceUsage;
    private Setting.Integer delay;

    public void setup() {
        ArrayList<String> displayModes = new ArrayList<>();
        delay = registerInteger("Delay", "Delay", 3, 0, 10);
        announceUsage = registerBoolean("ToggleMSG", "ToggleMSG", true);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().canCollideCheck(mc.world.getBlockState(pos),
                false);
    }

    private boolean isStackObby(ItemStack stack) {
        return (stack != null && stack.getItem() == Item.getItemById(30));
    }

    private boolean doesHotbarHaveWeb() {
        for (int i = 36; i < 45; i++) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack != null && isStackObby(stack)) {
                return true;
            }
        }
        return false;
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static IBlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static boolean placeBlockLegit(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3d posVec = (new Vec3d(pos)).add(0.5D, 0.5D, 0.5D);
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            if (canBeClicked(neighbor)) {
                Vec3d hitVec = posVec.add((new Vec3d(side.getDirectionVec())).scale(0.5D));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0D) {
                    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor,
                            side.getOpposite(), hitVec, EnumHand.MAIN_HAND);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void onUpdate() {
        if (mc.player.isHandActive()) {
            return;
        }
        trap(mc.player);
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2.0D) / 2.0D;
    }

    public void onEnable() {
        if (mc.player == null) {
            this.disable();
            return;
        }
        if (announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.GREEN + "SelfWeb has been toggled on!");
        }
        this.d = 0;
    }

    private void trap(EntityPlayer player) {
        if (player.moveForward == 0.0D && player.moveStrafing == 0.0D
                && player.moveForward == 0.0D) {
            this.d++;
        }
        if (player.moveForward != 0.0D || player.moveStrafing != 0.0D
                || player.moveForward != 0.0D) {
            this.d = 0;
        }
        if (!doesHotbarHaveWeb()) {
            this.d = 0;
        }
        if (this.d == this.delay.getValue() && doesHotbarHaveWeb()) {
            this.feet = new BlockPos(player.posX, player.posY, player.posZ);

            for (int i = 36; i < 45; i++) {
                ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
                if (stack != null && isStackObby(stack)) {
                    int oldSlot = mc.player.inventory.currentItem;
                    if (mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                        mc.player.inventory.currentItem = i - 36;
                        if (mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.feet);
                        }


                        mc.player.inventory.currentItem = oldSlot;
                        this.d = 0;
                        break;
                    }
                    this.d = 0;
                }
                this.d = 0;
            }
        }
    }

    public void onDisable() {
        this.d = 0;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "SelfWeb has been toggled on!");
        }
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
    }

    public EnumFacing getEnumFacing(float posX, float posY, float posZ) {
        return EnumFacing.getFacingFromVector(posX, posY, posZ);
    }

    public BlockPos getBlockPos(double x, double y, double z) {
        return new BlockPos(x, y, z);
    }
}

