package me.zoom.xannax.module.modules.player;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import net.minecraft.item.ItemPickaxe;

public class NoEntityTrace extends Module {
    public NoEntityTrace() {
        super("NoEntityTrace", "NoEntityTrace", Category.Player);
    }

    Setting.Boolean pickaxeOnly;

    public void setup() {
        pickaxeOnly = registerBoolean("Pickaxe Only", "PickaxeOnly", true);
    }

    boolean isHoldingPickaxe = false;

    public void onUpdate(){
        isHoldingPickaxe = mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe;
    }

    public boolean noTrace(){
        if(pickaxeOnly.getValue()) return isEnabled() && isHoldingPickaxe;
        return isEnabled();
    }
}
