package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import net.minecraft.client.renderer.ItemRenderer;

public class LowHands extends Module {
    public LowHands() {
        super("LowOffhand", "LowOffhand", Category.Render);
    }
    ItemRenderer itemRenderer = mc.entityRenderer.itemRenderer;

    Setting.Double off;

    public void setup(){
        off = registerDouble("Height", "LowOffhandHeight", 0.5, 0, 1);
    }

    public void onUpdate(){
        itemRenderer.equippedProgressOffHand = (float)off.getValue();
    }
}

