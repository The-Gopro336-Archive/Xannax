package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends Module {
    public Chat() {
        super("Chat", "Chat", Category.Misc);
    }

    public Setting.Boolean clearBkg;
    Setting.Boolean chattimestamps;
    Setting.Mode format;
    Setting.Mode color;
    Setting.Mode decoration;
    public static Setting.Boolean customFont;
    public static Setting.Boolean noChatShadow;
    Setting.Boolean space;
    Setting.Boolean greentext;

    public void setup(){

        ArrayList<String> formats = new ArrayList<>();
        formats.add("H24:mm");
        formats.add("H12:mm");
        formats.add("H12:mm a");
        formats.add("H24:mm:ss");
        formats.add("H12:mm:ss");
        formats.add("H12:mm:ss a");

        clearBkg = registerBoolean("Clear Chat", "ClearChat", false);
        greentext = registerBoolean("Green Text", "GreenText", false);
        chattimestamps = registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
        format = registerMode("Format", "Format", formats, "H24:mm");
        space = registerBoolean("Space", "Space", false);
        customFont = registerBoolean("CustomFont", "CustomFont", false);
        noChatShadow = registerBoolean("NoChatShadow", "NoChatShadow", false);

    }

    @EventHandler
    private final Listener<ClientChatReceivedEvent> chatReceivedEventListener = new Listener<>(event -> {
        //Chat Time Stamps

        if (chattimestamps.getValue()) {
            String dateFormat = format.getValue().replace("H24", "k").replace("H12", "h");
            String date = new SimpleDateFormat(dateFormat).format(new Date());
            TextComponentString time = new TextComponentString(ClickGuiModule.getTextColor() + "<" + date + ">" + ChatFormatting.RESET);
            event.setMessage(time.appendSibling(event.getMessage()));
        }
    });

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (greentext.getValue()) {
            if (event.getPacket() instanceof CPacketChatMessage) {
                if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage) event.getPacket()).getMessage().startsWith(Command.getPrefix()))
                    return;
                String message = ((CPacketChatMessage) event.getPacket()).getMessage();
                String prefix = "";
                prefix = ">";
                String s = prefix + message;
                if (s.length() > 255) return;
                ((CPacketChatMessage) event.getPacket()).message = s;
            }
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }

    public String toUnicode(String s) {
        return s.toLowerCase()
                .replace("a", "\u1d00")
                .replace("b", "\u0299")
                .replace("c", "\u1d04")
                .replace("d", "\u1d05")
                .replace("e", "\u1d07")
                .replace("f", "\ua730")
                .replace("g", "\u0262")
                .replace("h", "\u029c")
                .replace("i", "\u026a")
                .replace("j", "\u1d0a")
                .replace("k", "\u1d0b")
                .replace("l", "\u029f")
                .replace("m", "\u1d0d")
                .replace("n", "\u0274")
                .replace("o", "\u1d0f")
                .replace("p", "\u1d18")
                .replace("q", "\u01eb")
                .replace("r", "\u0280")
                .replace("s", "\ua731")
                .replace("t", "\u1d1b")
                .replace("u", "\u1d1c")
                .replace("v", "\u1d20")
                .replace("w", "\u1d21")
                .replace("x", "\u02e3")
                .replace("y", "\u028f")
                .replace("z", "\u1d22");
    }
}

