package me.zoom.xannax.module.modules.render;

import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class EnchantColor extends Module
{
    public static Setting.Integer red;
    public static Setting.Integer green;
    public static Setting.Integer blue;

    public EnchantColor() {
        super("EnchantColor", "Changes the color of the enchantment effect", Category.Render);
        red = registerInteger("Red", "Red", 255, 0, 255);
        green = registerInteger("Green", "Green", 255, 0, 255);
        blue = registerInteger("Blue", "Blue", 255, 0, 255);
    }
}

