package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.module.Category;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

public class DivisiionOffhand extends Module {

    Setting<Mode> mode = new Setting<Mode>("Mode", Mode.TOTEM);
    Setting<Float> fallback = new Setting<Float>("Fallback", 11f, 0f, 35f);

    public DivisiionOffhand() {
        super("DivisiionOffhand", Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if(mc.player != null) {
            if(mc.currentScreen instanceof GuiContainer) return;
            if(mc.player.getHeldItemOffhand().getItem() != getItem()) {
                moveItem();
            }
            if(mc.player.getHealth() <= fallback.value) {
                int targetSlot = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        targetSlot = i;
                        break;
                    }
                }
                if (targetSlot == -1) {
                    return;
                }
                mc.playerController.windowClick(0, (targetSlot < 9) ? (targetSlot + 36) : targetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

                mode.value = Mode.TOTEM;
            }
        }
    }

    public Item getItem() {
        if(mode.value == Mode.TOTEM) return Items.TOTEM_OF_UNDYING;
        if(mode.value == Mode.OBSIDIAN) return Item.getItemFromBlock(Blocks.OBSIDIAN);
        if(mode.value == Mode.CRYSTAL) return Items.END_CRYSTAL;
        if(mode.value == Mode.GAP) return Items.GOLDEN_APPLE;
        return Items.TOTEM_OF_UNDYING;
    }

    public void moveItem() {
        if(mc.player != null) {
            if(mode.value == Mode.TOTEM) {
                int targetSlot = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                        targetSlot = i;
                        break;
                    }
                }
                if (targetSlot == -1) {
                    return;
                }
                mc.playerController.windowClick(0, (targetSlot < 9) ? (targetSlot + 36) : targetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            }
            if(mode.value == Mode.OBSIDIAN) {
                int targetSlot = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                        targetSlot = i;
                        break;
                    }
                }
                if (targetSlot == -1) {
                    return;
                }
                mc.playerController.windowClick(0, (targetSlot < 9) ? (targetSlot + 36) : targetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            }
            if(mode.value == Mode.GAP) {
                int targetSlot = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                        targetSlot = i;
                        break;
                    }
                }
                if (targetSlot == -1) {
                    return;
                }
                mc.playerController.windowClick(0, (targetSlot < 9) ? (targetSlot + 36) : targetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            }
            if(mode.value == Mode.CRYSTAL) {
                int targetSlot = -1;
                for (int i = 0; i < 45; i++) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                        targetSlot = i;
                        break;
                    }
                }
                if (targetSlot == -1) {
                    return;
                }
                mc.playerController.windowClick(0, (targetSlot < 9) ? (targetSlot + 36) : targetSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    public enum Mode {
        TOTEM,
        OBSIDIAN,
        GAP,
        CRYSTAL
    }

}
