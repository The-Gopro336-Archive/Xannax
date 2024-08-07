package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;
import me.zoom.xannax.util.Location;

public class JumpEvent extends Event {
    private Location location;

    public JumpEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
