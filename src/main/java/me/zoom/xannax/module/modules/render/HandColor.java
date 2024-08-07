package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

public class HandColor extends Module{
    public HandColor(){super("HandColor", "HandColor", Category.Render);}

    public static Setting.Integer redh;
    public static Setting.Integer  greenh;
    public static Setting.Integer  blueh;
    public static Setting.Integer  alphah;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;

    public void setup() {
        redh = registerInteger("Red", "RedH", 255, 0, 255);
        greenh = registerInteger("Green", "GreenH", 255, 0, 255);
        blueh = registerInteger("Blue", "BlueH", 255, 0, 255);
        alphah = registerInteger("Alpha", "AlphaH", 50, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
    }
}
