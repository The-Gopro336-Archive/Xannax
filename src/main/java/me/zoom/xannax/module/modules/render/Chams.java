package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

public class Chams extends Module {
    public Chams() {
        super("Chams", "Chams", Category.Render);
    }

    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;
    public static Setting.Integer alpha;
    public static Setting.Integer Cred;
    public static Setting.Integer Cgreen;
    public static Setting.Integer Cblue;
    public static Setting.Integer Calpha;
    public static Setting.Boolean crystal;
    public static Setting.Boolean players;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;
    public static Setting.Boolean Crainbow;
    public static Setting.Integer Csaturation;
    public static Setting.Integer Cbrightness;
    public static Setting.Integer Cspeed;
    public static Setting.Boolean lines;
    public static Setting.Integer width;

    public void setup() {
        players = registerBoolean("Players", "Players", false);
        crystal = registerBoolean("Crystal", "Crystal", false);
        lines = registerBoolean("Lines", "Lines", false);
        width = registerInteger("Width", "Width", 1, 0, 10);
        red = registerInteger("Red", "Red", 255, 0, 255);
        green = registerInteger("Green", "Green", 255, 0, 255);
        blue = registerInteger("Blue", "Blue", 255, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
        alpha = registerInteger("Alpha", "Alpha", 50, 0, 255);
        Cred = registerInteger("C Red", "CRed", 255, 0, 255);
        Cgreen = registerInteger("C Green", "CGreen", 255, 0, 255);
        Cblue = registerInteger("C Blue", "CBlue", 255, 0, 255);
        Crainbow = registerBoolean("C Rainbow", "CRainbow", false);
        Csaturation = registerInteger("C Saturation", "CSaturation", 50, 0, 100);
        Cbrightness = registerInteger("C Brightness", "CBrightness", 50, 0, 100);
        Cspeed = registerInteger("C Speed", "CSpeed", 50, 1, 100);
        Calpha = registerInteger("C Alpha", "CAlpha", 50, 0, 255);
    }
}
