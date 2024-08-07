package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;

import java.util.UUID;

public class PlayerJoinEvent extends Event {
    private final String name;
    private final UUID uuid;

    public PlayerJoinEvent(String n, UUID id){
        super();
        name = n;
        uuid = id;
    }

    public String getName(){
        return name;
    }

    public UUID getUuid() {return uuid;}
}
