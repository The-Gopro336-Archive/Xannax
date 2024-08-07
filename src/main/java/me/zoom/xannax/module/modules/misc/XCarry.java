package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.*;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", "XCarry", Category.Misc);
    }

    @EventHandler
    private Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketCloseWindow){
            if(((CPacketCloseWindow)event.getPacket()).windowId == mc.player.inventoryContainer.windowId){
                event.cancel();
            }
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
