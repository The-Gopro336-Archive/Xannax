package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkyColor extends Module {
    public SkyColor() {
        super("SkyColor", "SkyColor", Category.Render);
        r = this.registerInteger("Red","Red",  255, 0, 255);
        g = this.registerInteger("Green", "Green",0, 0, 255);
        b = this.registerInteger("Blue", "Blue",255, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow",true);
    }
    Setting.Integer r;
    Setting.Integer g;
    Setting.Integer b;
    Setting.Boolean rainbow;


    public void onUpdate() {
        if (this.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }




    @SubscribeEvent
    public void fogColors(EntityViewRenderEvent.FogColors event) {
        event.setRed((float) r.getValue() / 255f); //colours in java are in decimal between 0 and 1 stupid paster
        event.setGreen((float) g.getValue() / 255f);
        event.setBlue((float) b.getValue() / 255f);
    }

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {

        event.setDensity(0);
        event.setCanceled(true);
    }


    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);

    }
}
