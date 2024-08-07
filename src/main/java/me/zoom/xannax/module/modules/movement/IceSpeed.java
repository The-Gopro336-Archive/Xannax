package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module {
    public IceSpeed() {
        super("IceSpeed", "SPEED", Category.Movement);
    }

    Setting.Double speed;
    public void setup(){
        speed = registerDouble("Speed", "Speed", 0.4, 0, 1.0);

    }

    public void onUpdate() {
        Blocks.ICE.slipperiness = (float) this.speed.getValue();
        Blocks.PACKED_ICE.slipperiness = (float) this.speed.getValue();
        Blocks.FROSTED_ICE.slipperiness = (float) this.speed.getValue();
    }

    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98F;
        Blocks.PACKED_ICE.slipperiness = 0.98F;
        Blocks.FROSTED_ICE.slipperiness = 0.98F;
    }
}