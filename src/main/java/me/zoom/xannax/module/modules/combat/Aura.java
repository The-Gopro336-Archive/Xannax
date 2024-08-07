package me.zoom.xannax.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.util.TpsUtils;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class Aura extends Module {

    public Aura() {
        super("Aura", "aura :D", Category.Combat);
    }

    Setting.Mode mode;
    Setting.Boolean player;
    Setting.Boolean hostile;
    Setting.Boolean sword;
    Setting.Boolean sync_tps;
    Setting.Double range;
    Setting.Integer delay;
    Setting.Boolean toggleMsg;

    public void setup() {
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Normal");
        modes.add("A32k");
        mode = registerMode("Mode", "Mode", modes, "Normal");
        player = registerBoolean("Player", "Player", true);
        hostile = registerBoolean("Hostile", "Hostile", false);
        sword = registerBoolean("Sword", "Sword", true);
        sync_tps = registerBoolean("SyncTPS", "SyncTPS", true);
        range = registerDouble("Range", "Range", 5.0, 0.5, 6.0);
        delay = registerInteger("Delay", "Delay", 2, 0, 10);
        toggleMsg = registerBoolean("ToggleMSG", "ToggleMSG", true);
    }

    boolean start_verify = true;

    EnumHand actual_hand = EnumHand.MAIN_HAND;

    double tick = 0;

    @Override
    protected void onEnable() {
        tick = 0;
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Aura has been toggled on!");
        }
    }

    @Override
    public void onDisable() {
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED +  "Aura has been toggled off!");
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player != null && mc.world != null) {

            tick++;

            if (mc.player.isDead | mc.player.getHealth() <= 0) {
                return;
            }

            if (mode.getValue().equalsIgnoreCase("Normal")) {
                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && sword.getValue()) {
                    start_verify = false;
                } else if ((mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && sword.getValue()) {
                    start_verify = true;
                } else if (!sword.getValue()) {
                    start_verify = true;
                }

                Entity entity = find_entity();

                if (entity != null && start_verify) {
                    // Tick.
                    float tick_to_hit  = 20.0f - TpsUtils.getTickRate();

                    // If possible hit or no.
                    boolean is_possible_attack = mc.player.getCooledAttackStrength(sync_tps.getValue() ? -tick_to_hit : 0.0f) >= 1;

                    // To hit if able.
                    if (is_possible_attack) {
                        attack_entity(entity);
                    }
                }
            } else {

                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;

                if (tick < delay.getValue()) return;

                tick = 0;

                Entity entity = find_entity();

                if (entity != null) {
                    attack_entity(entity);
                }
            }

        }
    }

    public void attack_entity(Entity entity) {

        if (mode.getValue().equalsIgnoreCase("A32k")) {

            int newSlot = -1;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY) {
                    continue;
                }
                if (checkSharpness(stack)) {
                    newSlot = i;
                    break;
                }
            }

            if (newSlot != -1) {
                mc.player.inventory.currentItem = newSlot;
            }

        }

        // Get actual item off hand.
        ItemStack off_hand_item = mc.player.getHeldItemOffhand();

        // If off hand not null and is some SHIELD like use.
        if (off_hand_item.getItem() == Items.SHIELD) {
            // Ignore ant continue.
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
        }

        // Start hit on entity.
        mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        mc.player.swingArm(actual_hand);
        mc.player.resetCooldown();
    }

    // For find a entity.
    public Entity find_entity() {
        // Create a request.
        Entity entity_requested = null;

        for (Entity player : mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            // If entity is not null continue to next event.
            if (player != null) {
                // If is compatible.
                if (is_compatible(player)) {
                    // If is possible to get.
                    if (mc.player.getDistance(player) <= range.getValue()) {
                        // Atribute the entity into entity_requested.
                        entity_requested = player;
                    }
                }
            }
        }

        // Return the entity requested.
        return entity_requested;
    }

    // Compatible or no.
    public boolean is_compatible(Entity entity) {
        // Instend entity with some type entity to continue or no.
        if (player.getValue() && entity instanceof EntityPlayer) {
            if (entity != mc.player && !(entity.getName().equals(mc.player.getName())) /* && WurstplusFriendManager.is_friend(entity) == false */) {
                return true;
            }
        }

        // If is hostile.
        if (hostile.getValue() && entity instanceof IMob) {
            return true;
        }

        // If entity requested die.
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entity_living_base = (EntityLivingBase) entity;

            if (entity_living_base.getHealth() <= 0) {
                return false;
            }
        }

        // Return false.
        return false;
    }

    private boolean checkSharpness(ItemStack stack) {

        if (stack.getTagCompound() == null) {
            return false;
        }

        NBTTagList enchants = (NBTTagList) stack.getTagCompound().getTag("ench");

        if (enchants == null) {
            return false;
        }

        for (int i = 0; i < enchants.tagCount(); i++) {
            NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                int lvl = enchant.getInteger("lvl");
                if (lvl > 5) {
                    return true;
                }
                break;
            }
        }

        return false;

    }

    @Override
    public String getHudInfo(){
        String t = "";
        if (mode.getValue().equalsIgnoreCase("Normal")){
            t = "[" + ChatFormatting.WHITE + "Single" + ChatFormatting.GRAY + "]";
        } else {
            t = "[" + ChatFormatting.WHITE + "A32k" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
