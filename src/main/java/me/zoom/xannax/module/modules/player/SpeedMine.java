package me.zoom.xannax.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.event.events.DamageBlockEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", "SpeedMine", Category.Player);
    }

    Setting.Mode mode;
    Setting.Boolean haste;

    public void setup() {

        ArrayList<String> Modes = new ArrayList<>();
        Modes.add("Packet");
        Modes.add("Damage");
        Modes.add("Instant");


        mode = registerMode("Mode", "Mode", Modes, "Packet");
        haste = registerBoolean("Haste", "Haste", false);
    }

    public void onUpdate() {
        Minecraft.getMinecraft().playerController.blockHitDelay = 0;

        if (haste.getValue()) {
            PotionEffect effect = new PotionEffect(MobEffects.HASTE, 80950, 1, false, false);
            mc.player.addPotionEffect(new PotionEffect(effect));
        }
        if (!(haste.getValue()) && mc.player.isPotionActive(MobEffects.HASTE)){ //disables haste when you turn the setting off
            mc.player.removePotionEffect(MobEffects.HASTE);
        }
    }

    @EventHandler
    private final Listener<DamageBlockEvent> listener = new Listener<>(event -> {

        if (mc.world == null || mc.player == null) {
            return;
        }
        if (canBreak(event.getPos())) {

            //Packet mine
            if (mode.getValue().equalsIgnoreCase("Packet")) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                event.cancel();
            }

            //Damage
            if (mode.getValue().equalsIgnoreCase("Damage")) {
                if (mc.playerController.curBlockDamageMP >= 0.7f) {
                    mc.playerController.curBlockDamageMP = 1.0f;
                }
            }

            if (mode.getValue().equalsIgnoreCase("Instant")) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace() ));
                mc.playerController.onPlayerDestroyBlock(event.getPos());
                mc.world.setBlockToAir(event.getPos());
            }
        }
    });

    private boolean canBreak(BlockPos pos) {
        final IBlockState blockState = mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1;
    }

    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
        mc.player.removePotionEffect(MobEffects.HASTE);
    }

    public String getHudInfo(){
        String t = "";
        t = "[" + ChatFormatting.WHITE + mode.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
}

