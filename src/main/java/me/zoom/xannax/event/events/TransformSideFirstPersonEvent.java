package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent extends Event {
    private final EnumHandSide handSide;

    public TransformSideFirstPersonEvent(EnumHandSide handSide){
        this.handSide = handSide;
    }

    public EnumHandSide getHandSide(){
        return handSide;
    }
}
