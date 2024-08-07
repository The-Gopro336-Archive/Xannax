package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

public class CameraClip extends Module{
    public CameraClip() {super("CameraClip", "CameraClip", Category.Render);}

    public static Setting.Boolean extend;
    public static Setting.Double distance;

    public void setup() {
        extend = registerBoolean("Extend", "Extend", false);
        distance = registerDouble("Distance", "Distance", 10, 0, 50);
    }
}
