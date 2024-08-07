package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.module.Module;

public class NoWeather extends Module {
    public NoWeather() {
        super("NoWeather", "Rain rain go away", Category.Misc);
    }

    public void onUpdate() {
        if (mc.world == null) return;
        if (mc.world.isRaining()) {
            mc.world.setRainStrength(0);
        }
    }
}
