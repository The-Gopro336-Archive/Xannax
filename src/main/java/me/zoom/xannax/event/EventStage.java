package me.zoom.xannax.event;

import net.minecraftforge.fml.common.eventhandler.Event;
 public class EventStage
         extends Event {
       private int stage;

       public EventStage() {}

       public EventStage(int stage) {
        /* 12 */     this.stage = stage;
        /*    */   }

       public int getStage() {
        /* 16 */     return this.stage;
        /*    */   }

       public void setStage(int stage) {
             this.stage = stage;
       }
 }
