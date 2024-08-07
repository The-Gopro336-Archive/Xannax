package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class FastSwim extends Module {
    // $FF: synthetic field
    int divider = 5;
    // $FF: synthetic field
    private Setting.Boolean down;
    // $FF: synthetic field
    private Setting.Boolean forward;
    // $FF: synthetic field
    private Setting.Boolean up;
    // $FF: synthetic field
    private Setting.Boolean sprint;
    // $FF: synthetic field
    private Setting.Boolean only2b;

    public FastSwim() {
        super("FastSwim", "FastSwim", Category.Movement);
    }

    public void setup() {
        this.up = registerBoolean("FastSwimUp", "FastSwimUp", true);
        this.down = registerBoolean("FastSwimDown", "FastSwimDown", true);
        this.forward = registerBoolean("FastSwimForward", "FastSwimForward", true);
        this.sprint = registerBoolean("AutoSprintInLiquid", "AutoSprintInLiquid", true);
        this.only2b = registerBoolean("Only2b", "Only2b", true);
    }

    public void onUpdate() {
        int var1;
        if ((Boolean)this.only2b.getValue() && !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org")) {
            if ((Boolean)this.sprint.getValue() && (mc.player.isInLava() || mc.player.isInWater())) {
                mc.player.setSprinting(true);
            }

            if ((mc.player.isInWater() || mc.player.isInLava()) && mc.gameSettings.keyBindJump.isKeyDown() && (Boolean)this.up.getValue()) {
                mc.player.motionY = 0.725D / (double)this.divider;
            }

            if (mc.player.isInWater() || mc.player.isInLava()) {
                if ((!(Boolean)this.forward.getValue() || !mc.gameSettings.keyBindForward.isKeyDown()) && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
                    mc.player.jumpMovementFactor = 0.0F;
                } else {
                    mc.player.jumpMovementFactor = 0.34F / (float)this.divider;
                }
            }

            if (mc.player.isInWater() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 2.2D / (double)var1;
            }

            if (mc.player.isInLava() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 0.91D / (double)var1;
            }
        }

        if (!(Boolean)this.only2b.getValue()) {
            if ((Boolean)this.sprint.getValue() && (mc.player.isInLava() || mc.player.isInWater())) {
                mc.player.setSprinting(true);
            }

            if ((mc.player.isInWater() || mc.player.isInLava()) && mc.gameSettings.keyBindJump.isKeyDown() && (Boolean)this.up.getValue()) {
                mc.player.motionY = 0.725D / (double)this.divider;
            }

            if (mc.player.isInWater() || mc.player.isInLava()) {
                if ((!(Boolean)this.forward.getValue() || !mc.gameSettings.keyBindForward.isKeyDown()) && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown()) {
                    mc.player.jumpMovementFactor = 0.0F;
                } else {
                    mc.player.jumpMovementFactor = 0.34F / (float)this.divider;
                }
            }

            if (mc.player.isInWater() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 2.2D / (double)var1;
            }

            if (mc.player.isInLava() && (Boolean)this.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                var1 = this.divider * -1;
                mc.player.motionY = 0.91D / (double)var1;
            }
        }

    }
}

