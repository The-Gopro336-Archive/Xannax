package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PlayerLeaveEvent extends Event {

    private final String name;
    private final UUID uuid;
    private final EntityPlayer entity;

    public PlayerLeaveEvent(String n, UUID id, EntityPlayer ent){
        super();
        name = n;
        uuid = id;
        entity = ent;
    }

    public String getName(){
        return name;
    }

    public EntityPlayer getEntity() {
        /* 37 */     return entity;
        /*    */   }

    public UUID getUuid() {return uuid;}
}
