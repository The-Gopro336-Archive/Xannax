package me.zoom.xannax.module.modules.player;

import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.Xannax;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;

public class BuildHeight extends Module {
    public BuildHeight() {super("BuildHeight", "Lets you place blocks at build height", Category.Player);}

    @EventHandler
    private Listener<PacketEvent.Send> send_listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
            if (p.getPos().getY() >= 255 && p.getDirection() == EnumFacing.UP) {
                p.placedBlockDirection = EnumFacing.DOWN;
            }
        }
    });

    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
