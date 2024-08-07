package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

public class ChatSuffix extends Module{
    public ChatSuffix(){
        super("ChatSuffix", "Show off your client!",  Category.Misc);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage){
            if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(Command.getPrefix()) || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith("!"))
                return;
            String Separator2 = " \u23D0 ";
            String old = ((CPacketChatMessage) event.getPacket()).getMessage();
            String suffix = Separator2 + "\u0058\u1d00\u0274\u0274\u1d00\u0058";
            String s = old + suffix;
            if (s.length() > 255) return;
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}

