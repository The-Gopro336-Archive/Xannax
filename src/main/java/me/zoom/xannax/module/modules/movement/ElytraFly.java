package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

//osiris+ skid, its just an ely fly lol
public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Elytrafly module", Category.Movement);
    }

    Setting.Double speed;

    public void setup(){
        speed = registerDouble("Speed", "Speed", 1.8, 0, 3);
    }

    public void onUpdate(){
        if(mc.player.capabilities.isFlying || mc.player.isElytraFlying())
            mc.player.setSprinting(false);
        if (mc.player.capabilities.isFlying) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(mc.player.posX, mc.player.posY - 0.000050000002f, mc.player.posZ);
            mc.player.capabilities.setFlySpeed((float)speed.getValue());
            mc.player.setSprinting(false);
        }

        if (mc.player.onGround) {
            mc.player.capabilities.allowFlying = false;
        }

        if (mc.player.isElytraFlying()) {
            mc.player.capabilities.setFlySpeed(.915f);
            mc.player.capabilities.isFlying = true;

            if (!mc.player.capabilities.isCreativeMode)
                mc.player.capabilities.allowFlying = true;
        }
    }

    protected void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (!mc.player.capabilities.isCreativeMode)
            mc.player.capabilities.allowFlying = false;
    }
}
