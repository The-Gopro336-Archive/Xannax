package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.XannaxRPC;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;

public class RPCModule extends Module {
    public RPCModule() {
        super("DiscordRPC", "DiscordRPC", Category.Misc);
    }

    public void onEnable() {
        XannaxRPC.init();
        if(mc.player != null)
            Command.sendClientMessage(ChatFormatting.GREEN + "DiscordRPC started!");
    }

    public void onDisable() {
        XannaxRPC.shutdown();
        Command.sendClientMessage(ChatFormatting.RED + "DiscordRPC shutdown!");
    }
}
