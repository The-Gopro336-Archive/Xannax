package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.*;

public class AutoWeed extends Module {
    public AutoWeed() {
        super("AutoWeed", "AutoWeed", Category.Combat);
    }

    private boolean isEating = false;

    Setting.Boolean chorus;

    @Override
    public void setup(){
        chorus = registerBoolean("Disable On Chorus", "Disable On Chorus", true);
    }

    @Override
    public void onEnable(){
        mc.player.inventory.currentItem = findGapple();
    }

    @Override
    public void onUpdate() {
        Item itemMainHand = mc.player.getHeldItemMainhand().getItem();
        Item itemONotMainHand = mc.player.getHeldItemOffhand().getItem();
        boolean gapInMainHand = itemMainHand instanceof ItemAppleGold;
        boolean gapNotInMainHand = itemONotMainHand instanceof ItemAppleGold;
        /*
        boolean chorusInMainHand = itemMainHand instanceof ItemChorusFruit;
        boolean chorusNotInMainHand = itemONotMainHand instanceof ItemChorusFruit;
         */
        isEating = true;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        mc.rightClickMouse();
    }

    private int findGapple() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
}

