package me.zoom.xannax.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.event.events.WaterPushEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
    public Velocity(){
        super("Velocity", "Velocity", Category.Movement);
    }

    public Setting.Boolean noPush;
    Setting.Boolean antiKnockBack;

    public void setup(){
        noPush = registerBoolean("No Push", "NoPush", false);
        antiKnockBack = registerBoolean("Velocity", "Velocity", false);
    }

    //Velocity
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
        if (antiKnockBack.getValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity) {
                if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId())
                    event.cancel();
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.cancel();
            }
        }
    });

    @EventHandler
    private final Listener<WaterPushEvent> waterPushEventListener = new Listener<>(event -> {
        if (noPush.getValue()){
            event.cancel();
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }

    public String getHudInfo() {
        String t = "";
        t = "[" + ChatFormatting.WHITE + "0% 0%" + ChatFormatting.GRAY + "]";
        return t;
    }
}
