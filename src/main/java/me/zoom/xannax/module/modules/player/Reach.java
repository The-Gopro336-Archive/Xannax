package me.zoom.xannax.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

public class Reach extends Module {
    public Reach() {super("Reach", "Reach", Category.Player);}

    public static Setting.Boolean override;
    public static Setting.Double add;
    public static Setting.Double reach;

    public void setup() {
        override = registerBoolean("Override", "Override", false);
        add = registerDouble("Add", "AddR", 1, 0, 3);
        reach = registerDouble("Reach", "Reach", 1, 0, 6);
    }

    @Override
    public String getHudInfo(){
        String t = "";
        if (override.getValue()){
            t = "[" + ChatFormatting.WHITE + reach.getValue() + ChatFormatting.GRAY + "]";
        } else {
            t = "[" + ChatFormatting.WHITE + add.getValue() + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
