package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

import java.util.ArrayList;

public class ESP extends Module {

    public ESP() {
        super("ESP", "ESP", Category.Render);
    }

    public void setup() {
        width = registerDouble("Width", "Width",3 ,0.1, 10);
        Cwidth = registerDouble("C Width", "CWidth",3 ,0.1, 10);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("OutLine");
        modes.add("WireFrame");
        mode = registerMode("RenderMode", "RenderMode",modes, "WireFrame");
        self = registerBoolean("Self", "Self", false);
        player = registerBoolean("Player", "Player", true);
        crystal = registerBoolean("Crystal", "Crystal", false);
        redd = registerInteger("Red", "RedESP", 255, 0, 255);
        greenn = registerInteger("Green", "GreenESP", 255, 0, 255);
        bluee = registerInteger("Blue", "BlueESP", 255, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
        Credd = registerInteger("CRed", "CRedESP", 255, 0, 255);
        Cgreenn = registerInteger("C Green", "CGreenESP", 255, 0, 255);
        Cbluee = registerInteger("C Blue", "CBlueESP", 255, 0, 255);
        Crainbow = registerBoolean("C Rainbow", "CRainbow", false);
        Csaturation = registerInteger("C Saturation", "CSaturation", 50, 0, 100);
        Cbrightness = registerInteger("C Brightness", "CBrightness", 50, 0, 100);
        Cspeed = registerInteger("C Speed", "CSpeed", 50, 1, 100);
    }

    public static Setting.Mode mode;
    public static Setting.Double width;
    public static Setting.Double Cwidth;
    public static Setting.Integer redd;
    public static Setting.Integer greenn;
    public static Setting.Integer bluee;
    public static Setting.Integer Credd;
    public static Setting.Integer Cgreenn;
    public static Setting.Integer Cbluee;
    public static Setting.Boolean self;
    public static Setting.Boolean crystal;
    public static Setting.Boolean rainbow;
    public static Setting.Integer saturation;
    public static Setting.Integer brightness;
    public static Setting.Integer speed;
    public static Setting.Boolean Crainbow;
    public static Setting.Integer Csaturation;
    public static Setting.Integer Cbrightness;
    public static Setting.Integer Cspeed;
    public static Setting.Boolean player;
}
