package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.util.BlockInteractionHelper;
import me.zoom.xannax.util.BlockInteractionHelper.ValidResult;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.util.PlayerUtil;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class HoleFill extends Module {

    public HoleFill() {
        super("HoleFill", "Fill Holes", Category.Combat);
    }

    Setting.Boolean hole_toggle;
    Setting.Boolean hole_rotate;
    Setting.Integer hole_range;

    public void setup(){

        ArrayList<String> hands = new ArrayList<>();
        hands.add("Mainhand");
        hands.add("Offhand");
        hands.add("Both");

        hole_toggle = registerBoolean("Toggle", "HoleFillToggle", true);
        hole_rotate = registerBoolean("Rotate", "HoleFillRotate", true);
        hole_range = registerInteger("Range", "HoleFillRange", 4, 1, 6);
    }


    private final ArrayList<BlockPos> holes = new ArrayList<>();

    @Override
    public void onEnable() {
        if (find_in_hotbar() == -1) {
            this.disable();
        }
        find_new_holes();
    }

    @Override
    public void onDisable() {
        holes.clear();
    }

    @Override
    public void onUpdate() {

        if (find_in_hotbar() == -1) {
            this.disable();
            return;
        }

        if (holes.isEmpty()) {
            if (!hole_toggle.getValue()) {
                disable();
                return;

            } else {
                find_new_holes();
            }
        }

        BlockPos pos_to_fill = null;

        for (BlockPos pos : new ArrayList<>(holes)) {

            if (pos == null) continue;

            BlockInteractionHelper.ValidResult result = BlockInteractionHelper.valid(pos);

            if (result != ValidResult.Ok) {
                holes.remove(pos);
                continue;
            }
            pos_to_fill = pos;
            break;
        }

        if (find_in_hotbar() == -1) {
            this.disable();
            return;
        }

        if (pos_to_fill != null) {
            if (BlockUtils.placeBlock(pos_to_fill, find_in_hotbar(), hole_rotate.getValue(), hole_rotate.getValue())) {
                holes.remove(pos_to_fill);
            }
        }

    }

    public void find_new_holes() {

        holes.clear();

        for (BlockPos pos : BlockInteractionHelper.getSphere(PlayerUtil.GetLocalPlayerPosFloored(), hole_range.getValue(), (int) hole_range.getValue(), false, true, 0)) {

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            boolean possible = true;

            for (BlockPos seems_blocks : new BlockPos[] {
                    new BlockPos( 0, -1,  0),
                    new BlockPos( 0,  0, -1),
                    new BlockPos( 1,  0,  0),
                    new BlockPos( 0,  0,  1),
                    new BlockPos(-1,  0,  0)
            }) {
                Block block = mc.world.getBlockState(pos.add(seems_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    possible = false;
                    break;
                }
            }

            if (possible) {
                holes.add(pos);
            }
        }
    }

    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockEnderChest) {
                    return i;
                }

                if (block instanceof BlockObsidian) {
                    return i;
                }
            }
        }
        return -1;
    }

}
