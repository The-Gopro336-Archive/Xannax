package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;
import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent extends Event {

    BlockPos pos;

    public DestroyBlockEvent(BlockPos blockPos){
        super();
        pos = blockPos;
    }

    public BlockPos getBlockPos(){
        return pos;
    }

    public void setPos(BlockPos pos){
        this.pos = pos;
    }
}
