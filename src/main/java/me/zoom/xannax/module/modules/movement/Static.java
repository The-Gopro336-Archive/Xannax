package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class Static extends Module
{
    public Static() {
        super("Static", "Static", Category.Movement);
    }
    Setting.Mode mode;
    Setting.Boolean disabler;
    Setting.Boolean ySpeed;
    Setting.Double speed;
    Setting.Double height;


    public void setup() {
        ArrayList<String> modes = new ArrayList<>();
        modes.add("STATIC");
        modes.add("ROOF");
        modes.add("NOVOID");
        this.mode = registerMode("Mode", "Mode", modes, "NOVOID");
        this.disabler = registerBoolean("Disable", "Disable", true);
        this.ySpeed = registerBoolean("YSpeed", "YSpeed", false);
        this.speed = registerDouble("Speed", "Speed", 0.1f, 0.0f, 10.0f);
        this.height = registerDouble("Height", "Height", 3.0f, 0.0f, 256.0f);
    }

    @Override
    public void onUpdate() {
        switch (mode.getValue()) {
            case "STATIC": {
                Static.mc.player.capabilities.isFlying = false;
                Static.mc.player.motionX = 0.0;
                Static.mc.player.motionY = 0.0;
                Static.mc.player.motionZ = 0.0;
                if (!this.ySpeed.getValue()) {
                    break;
                }
                Static.mc.player.jumpMovementFactor = (float)speed.getValue();
                if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = Static.mc.player;
                    player.motionY += this.speed.getValue();
                }
                if (Static.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2 = Static.mc.player;
                    player2.motionY -= this.speed.getValue();
                    break;
                }
                break;
            }
            case "ROOF": {
                Static.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
                if (this.disabler.getValue()) {
                    this.disable();
                    break;
                }
                break;
            }
            case "NOVOID": {
                if (Static.mc.player.noClip || Static.mc.player.posY > this.height.getValue()) {
                    break;
                }
                final RayTraceResult trace = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
                if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return;
                }
                Static.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (Static.mc.player.getRidingEntity() != null) {
                    Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
                    break;
                }
                break;
            }
        }
    }
}
