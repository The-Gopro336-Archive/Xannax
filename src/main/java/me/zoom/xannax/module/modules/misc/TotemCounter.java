package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.event.events.TotemPopEvent;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.*;

public class TotemCounter extends Module{
    public TotemCounter() {super("PopCounter", "PopCounter", Module.Category.Misc);}

    private HashMap<String, Integer> popList = new HashMap();

    @EventHandler
    public Listener<TotemPopEvent> totemPopEvent = new Listener<>(event -> {
        if (popList == null) {
            popList = new HashMap<>();
        }

        if (popList.get(event.getEntity().getName()) == null) {
            popList.put(event.getEntity().getName(), 1);
            Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.GREEN + 1 + ChatFormatting.RED + " totem!");
        } else if (!(popList.get(event.getEntity().getName()) == null)) {
            int popCounter = popList.get(event.getEntity().getName());
            int newPopCounter = popCounter += 1;
            popList.put(event.getEntity().getName(), newPopCounter);
            Command.sendClientMessage(ChatFormatting.RED + event.getEntity().getName() + ChatFormatting.RED + " popped " + ChatFormatting.GREEN + newPopCounter + ChatFormatting.RED + " totems!");
        }
    });
    public void onUpdate() {
        for(EntityPlayer player : mc.world.playerEntities) {
            if (player.getHealth() <= 0) {
                if (popList.containsKey(player.getName())) {
                    Command.sendClientMessage(ChatFormatting.RED + player.getName() + ChatFormatting.RED + " died after popping " + ChatFormatting.GREEN + popList.get(player.getName()) + ChatFormatting.RED  + " totems!");
                    popList.remove(player.getName(), popList.get(player.getName()));
                }
            }
        }
    }

    @EventHandler
    public Listener<PacketEvent.Receive> totemPopListener = new Listener<>(event -> {

        if (mc.world == null || mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                Entity entity = packet.getEntity(mc.world);
                Xannax.EVENT_BUS.post(new TotemPopEvent(entity));
            }
        }

    });

    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
        popList = new HashMap<>();
    }

    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}

