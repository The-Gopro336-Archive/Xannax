package me.zoom.xannax.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;

public class KillAura extends Module {
    public KillAura() {
        super("KillAura", "KillAura", Category.Combat);
    }

    Setting.Double range;
    Setting.Boolean criticals;
    Setting.Boolean rotate;
    Setting.Mode aimMode;
    Setting.Boolean toggleMsg;
    boolean rotating;
    public static EntityPlayer target;

    @Override
    public void setup() {
        ArrayList<String> aimModes = new ArrayList<>();
        aimModes.add("Leg");
        aimMode = registerMode("Mode","Mode",  aimModes, "Leg");
        boolean swordOnly = true;
        boolean caCheck = true;
        boolean tpsSync = false;
        boolean isAttacking = false;
        this.range = registerDouble("Range", "Range",4.5, 0.0, 10.0);
        this.criticals = registerBoolean("Criticals", "Criticals",true);
        this.rotate = registerBoolean("Rotate", "Rotate", true);
        toggleMsg = registerBoolean("ToggleMSG", "ToggleMSG", true);
    }

    public void onDisable() {
        rotating = false;
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Killaura has been toggled off!");
        }
    }

    public void onEnable() {
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Killaura has been toggled on!");
        }
    }


    public void onUpdate() {
        if (mc.player != null || mc.world != null) {
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player != mc.player) {
                    if (mc.player.getDistance(player) < range.getValue()) {
                        if (Friends.isFriend(player.getName())) return;
                        if (player.isDead || player.getHealth() > 0) {
                            if (rotating && rotate.getValue()) {
                                float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), player.getPositionVector());
                                mc.player.rotationYaw = angle[0];
                                switch (aimMode.getValue()) {
                                    case "Leg":
                                        mc.player.rotationPitch = angle[1];
                                        break;
                                }
                            }
                            attackPlayer(player);
                        }
                        target = player;
                    } else {
                        rotating = false;
                    }
                }
            }
        }
    }


    public void attackPlayer(EntityPlayer player) {
        if (player != null) {
            if (player != mc.player) {
                if (mc.player.getCooledAttackStrength(0.0f) >= 1) {
                    rotating = true;
                    mc.playerController.attackEntity(mc.player, player);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
        else {
            rotating = false;
        }
    }
}
