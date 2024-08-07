package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.InputUpdateEvent;

public class NoSlow extends Module {
    public NoSlow(){
        super("NoSlow", "NoSlow", Category.Movement);
    }

    public Setting.Boolean guiMove;
    public Setting.Boolean noSlow;

    public void setup(){
        noSlow = registerBoolean("SoulSand", "SoulSand", false);
    }

    //No Slow
    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
            if (mc.player.isHandActive() && !mc.player.isRiding()) {
                event.getMovementInput().moveStrafe *= 5;
                event.getMovementInput().moveForward *= 5;
            }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
