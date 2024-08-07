package me.zoom.xannax.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.util.CrystalUtils;
import me.zoom.xannax.util.DamageUtils;
import me.zoom.xannax.util.TimerUtils;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", "AutoCrystal", Category.Combat);
    }

    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals = new ConcurrentHashMap<>();

    private int oldSlot = -1;
    private int newSlot = -1;

    boolean mainhand = false;
    boolean offhand = false;

    public static EntityPlayer target;

    private boolean switchCooldown = false;
    private boolean isRotating = false;
    public static boolean isAttacking = false;
    private boolean isPlacing = false;

    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    BlockPos render;
    BlockPos position = null;

    TimerUtils breakTimer = new TimerUtils();
    TimerUtils placeTimer = new TimerUtils();

    // Explode
    Setting.Integer breakAttempts;
    Setting.Mode handBreak;
    Setting.Double hitRange;
    Setting.Boolean antiWeakness;
    Setting<Boolean> explode = new Setting<Boolean>("Explode", true);
    Setting<Integer> hitDelay = new Setting<Integer>("Hit Delay", 3, 0, 20);

    // Place
    Setting.Boolean place;
    Setting.Boolean placeUnderBlock;
    Setting.Boolean autoSwitch;
    Setting.Integer placeDelay;
    Setting.Double placeRange;
    Setting.Double wallsRange;
    Setting.Boolean noSuicide;

    // Misc
    Setting.Boolean rotate;
    Setting.Boolean spoofRotations;
    Setting.Double minDmg;
    Setting.Double maxSelfDmg;
    Setting.Boolean cancelCrystal;
    Setting.Boolean swingExploit;
    Setting.Boolean pauseWhileEating;
    Setting.Boolean pauseWhileMining;
    Setting.Mode timer;
    Setting.Boolean toggleMsg;

    // Faceplace
    Setting.Boolean facePlace;
    Setting.Boolean facePlaceCheck;
    Setting.Double facePlaceHp;
    Setting.Boolean armorDestroy;
    Setting.Boolean armorCheck;
    Setting.Integer armorPercent;
    Setting.Integer checkValue;

    // TODO: remake render system

    // Render
    Setting.Boolean renderPlacement; //make
    Setting.Boolean renderCustomFont; //make
    Setting.Boolean renderRainbow; //make
    Setting.Mode renderMode; //make
    Setting.Boolean renderFill; //make
    Setting.Boolean renderOutline; //make
    Setting.Boolean customOutline; //make
    Setting.Boolean renderDamage; //make
    Setting.Integer fillRed; //make
    Setting.Integer fillGreen; //make
    Setting.Integer fillBlue; //make
    Setting.Integer fillAlpha; //make
    Setting.Double lineWidth; //make
    Setting.Integer outlineRed; //make
    Setting.Integer outlineGreen; //make
    Setting.Integer outlineBlue; //make
    Setting.Integer outlineAlpha; //make
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;

    public void setup() {
        ArrayList<String> logics = new ArrayList<>();
        logics.add("Breakplace");
        logics.add("Placebreak");

        ArrayList<String> hands = new ArrayList<>();
        hands.add("Mainhand");
        hands.add("Offhand");
        hands.add("Both");

        explode = registerBoolean("Explode", "Explode", true);
        hitDelay = registerInteger("HitDelay", "HitDelay", 0, 0, 1000);
        breakAttempts = registerInteger("Attempts", "Attempts", 1, 1, 6);
        handBreak = registerMode("Hand", "Hand", hands, "Mainhand");
        hitRange = registerDouble("HitRange", "HitRange", 5.0, 0.0, 10.0);
        antiWeakness = registerBoolean("AntiWeakness", "AntiWeakness", true);

        place = registerBoolean("Place", "Place", true);
        placeUnderBlock = registerBoolean("PlaceUnderBlock", "PlaceUnderBlock", false);
        autoSwitch = registerBoolean("AutoSwitch", "AutoSwitch", true);
        placeDelay = registerInteger("PlaceDelay", "PlaceDelay", 0, 0, 1000);
        placeRange = registerDouble("PlaceRange", "PlaceRange", 5.0, 0.0, 10.0);
        wallsRange = registerDouble("WallsRange", "WallsRange", 3.5, 0.0, 10.0);
        noSuicide = registerBoolean("NoSuicide", "NoSuicide", true);

        rotate = registerBoolean("Rotate", "Rotate", true);
        spoofRotations = registerBoolean("SpoofRotations", "SpoofRotations", true);
        minDmg = registerDouble("MinDMG", "MinDMG", 5.0, 0.0, 20.0);
        maxSelfDmg = registerDouble("MaxSelfDMG", "MaxSelfDMG", 10.0, 0.0, 36.0);
        cancelCrystal = registerBoolean("Cancel", "Cancel", true);
        swingExploit = registerBoolean("SwingExploit", "SwingExploit", true);
        pauseWhileEating = registerBoolean("PauseWhileEating", "PauseWhileEating", true);
        pauseWhileMining = registerBoolean("PauseWhileMining", "PauseWhileMining", true);
        timer = registerMode("Timer", "Timer", logics, "Breakplace");
        toggleMsg = registerBoolean("ToggleMSG", "ToggleMSG", true);

        facePlace = registerBoolean("Faceplace", "Faceplace", true);
        facePlaceCheck = registerBoolean("FaceplaceCheck", "FaceplaceCheck", true);
        facePlaceHp = registerDouble("FaceplaceHP", "FaceplaceHP", 12.0, 0.0, 36.0);
        armorDestroy = registerBoolean("ArmorDestroy", "ArmorDestroy", true);
        armorCheck = registerBoolean("ArmorCheck", "ArmorCheck", true);
        armorPercent = registerInteger("ArmorPercent", "ArmorPercent", 25, 0, 100);
        checkValue = registerInteger("CheckValue", "CheckValue", 30, 0, 100);

        renderPlacement = registerBoolean("Render", "Render", true);
        renderCustomFont = registerBoolean("CustomFont", "CustomFont", true);
        renderRainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
        renderFill = registerBoolean("Fill", "Fill", true);
        renderOutline = registerBoolean("Outline", "Outline", true);
        customOutline = registerBoolean("CustomOutline", "CustomOutline", true);
        renderDamage = registerBoolean("RenderDamage", "RenderDamage", true);
        fillRed = registerInteger("FillRed", "FillRed", 186, 0, 255);
        fillGreen = registerInteger("FillGreen", "FillGreen", 85, 0, 255);
        fillBlue = registerInteger("FillBlue", "FillBlue", 211, 0, 255);
        fillAlpha = registerInteger("FillAlpha", "FillAlpha", 53, 0, 255);
        lineWidth = registerDouble("LineWidth", "LineWidth", 1.5, 0.0, 5.0);
        outlineRed = registerInteger("OutlineRed", "OutlineRed", 255, 0, 255);
        outlineGreen = registerInteger("OutlineGreen", "OutlineGreen", 255, 0, 255);
        outlineBlue = registerInteger("OutlineBlue", "OutlineBlue", 255, 0, 255);
        outlineAlpha = registerInteger("OutlineAlpha", "OutlineAlpha", 255, 0, 255);
    }

    /*public void onUpdate() {
        int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }
        if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
            if (autoSwitch.getValue()) {
                mc.player.inventory.currentItem = crystalSlot;
                resetRotation();
                switchCooldown = true;
            }
            return;
        }
        // return after we did an autoswitch
        if (switchCooldown) {
            switchCooldown = false;
            return;
        }
        doAutoCrystal();
    }*/

    public void onUpdate() {
        doAutoCrystal();
    }


    public void doAutoCrystal() {
        switch (timer.getValue()) {
            case "Breakplace":
                explodeCrystal();
                placeCrystal();
                break;
            case "Placebreak":
                placeCrystal();
                explodeCrystal();
                break;
        }
    }

    public void explodeCrystal() {
        final EntityEnderCrystal crystal = getBestCrystal();
        if (explode.getValue() && crystal != null && mc.player.getDistance(crystal) <= hitRange.getValue()) {
            if (breakTimer.passedMs(hitDelay.getValue())) {
                if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!isAttacking) {
                        oldSlot = mc.player.inventory.currentItem;
                        isAttacking = true;
                    }

                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack == ItemStack.EMPTY) {
                            continue;
                        }

                        if ((stack.getItem() instanceof ItemSword)) {
                            newSlot = i;
                            break;
                        }

                        if ((stack.getItem() instanceof ItemTool)) {
                            newSlot = i;
                            break;
                        }
                    }

                    if (newSlot != -1) {
                        mc.player.inventory.currentItem = newSlot;
                        switchCooldown = true;
                    }
                }

                rotateToPos(crystal.posX, crystal.posY, crystal.posZ, mc.player);
                for (int i = 0; i < breakAttempts.getValue(); i++) {
                    mc.playerController.attackEntity(mc.player, crystal);
                }
                addAttackedCrystal(crystal);
                getSwingingHand(crystal);
                if (cancelCrystal.getValue()) {
                    crystal.setDead();
                }
                breakTimer.reset();
                isAttacking = false;
            }
        } else {
            resetRotation();
            if (oldSlot != -1) {
                mc.player.inventory.currentItem = oldSlot;
                oldSlot = -1;
            }
            isAttacking = false;
        }
    }

    public void placeCrystal() {
        double damage = .5;
        mainhand = (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal);
        offhand = (mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal);

        List<BlockPos> possiblePositions = CrystalUtils.possiblePlacePositions((float) placeRange.getValue(), placeUnderBlock.getValue(), true);
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (Friends.isFriend(entity.getName())) continue;

            if (entity == mc.player || !(entity instanceof EntityPlayer)) continue;

            if (entity.getDistance(mc.player) >= 11) continue;

            if (entity.isDead || entity.getHealth() <= 0) continue;

            for (final BlockPos blockPos : possiblePositions) {

                final double targetDamage = DamageUtils.calculateDamage(blockPos, entity);
                final double selfDamage = DamageUtils.calculateDamage(blockPos, mc.player);

                double minimumDamage;
                double maximumSelfDamage = maxSelfDmg.getValue();

                boolean noPlace = facePlaceCheck.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if (facePlace.getValue() && entity.getHealth() + entity.getAbsorptionAmount() <= facePlaceHp.getValue() && !noPlace && !shouldActionArmor(mc.player, true)) {
                    minimumDamage = 2;
                } else {
                    if (shouldActionArmor(entity, false) && !noPlace && !shouldActionArmor(mc.player, true)) {
                        minimumDamage = 2;
                    } else {
                        minimumDamage = minDmg.getValue();
                    }
                }

                if (targetDamage <= minimumDamage || maximumSelfDamage <= selfDamage || targetDamage <= damage) {
                    continue;
                }
                damage = targetDamage;
                position = blockPos;
                target = entity;
            }
        }

        if (damage == .5) {
            render = null;
            return;
        }

        if (place.getValue()) {
            if (offhand || mainhand && placeTimer.passedMs(placeDelay.getValue())) {
                boolean shouldPauseEating = pauseWhileEating.getValue() && isEatingGap();
                boolean shouldPauseMining = pauseWhileMining.getValue() && isHittingBlock();

                render = position;
                final RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(position.getX() + 0.5, position.getY() - 0.5, position.getZ() + 0.5));
                final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
                isPlacing = true;
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(position, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                isPlacing = false;
            }
        }
    }

    private boolean isEatingGap() {
        return mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && mc.player.isHandActive();
    }

    private boolean isHittingBlock() {
        return mc.playerController.isHittingBlock;
    }

    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    public boolean shouldActionArmor(EntityPlayer player, boolean safe) {
        if (safe) {
            for (ItemStack stack : player.getArmorInventoryList()) {
                if (stack == null || stack.getItem() == Items.AIR) {
                    return true;
                }

                final float percentArmor = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;

                if (armorCheck.getValue() && this.checkValue.getValue() >= percentArmor) {
                    return true;
                }
            }
        } else {
            for (ItemStack stack : player.getArmorInventoryList()) {
                if (stack == null || stack.getItem() == Items.AIR) {
                    return true;
                }

                final float percentArmor = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;

                if (armorDestroy.getValue() && armorPercent.getValue() >= percentArmor) {
                    return true;
                }
            }
        }
        return false;
    }

    private void rotateToPos(double px, double py, double pz, EntityPlayer player) {
        double[] angle = CrystalUtils.calculateLookAt(px, py, pz, player);
        if (rotate.getValue()) {
            isRotating = true;
            setYawAndPitch((float) angle[0], (float) angle[1]);
            isRotating = false;
        }
    }

    private static void setYawAndPitch(float yawValue, float pitchValue) {
        yaw = yawValue;
        pitch = pitchValue;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    public void addAttackedCrystal(EntityEnderCrystal crystal) {
        if (attackedCrystals.containsKey(crystal)) {
            int value = attackedCrystals.get(crystal);
            attackedCrystals.put(crystal, value + 1);
        } else {
            attackedCrystals.put(crystal, 1);
        }
    }

    public void getSwingingHand(EntityEnderCrystal crystal) {
        if (handBreak.getValue().equalsIgnoreCase("Mainhand")) {
            if (swingExploit.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        } else if (handBreak.getValue().equalsIgnoreCase("Offhand")) {
            mc.player.swingArm(EnumHand.OFF_HAND);
        } else {
            if (ModuleManager.isModuleEnabled("OffhandSwing")) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } else {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.OFF_HAND);
            }
        }
    }

    public void onWorldRender(RenderEvent event) {
        if (this.render != null && renderPlacement.getValue()) {
            final Color rainbowColor1 = new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f));
            final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
            final float[] hue = {(System.currentTimeMillis() % (360 * 7) / (360f * 7))};
            int rgb = Color.HSBtoRGB(hue[0], 1, 1);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            final AxisAlignedBB bb = new AxisAlignedBB(render.getX() - mc.getRenderManager().viewerPosX, render.getY() - mc.getRenderManager().viewerPosY + 1, render.getZ() - mc.getRenderManager().viewerPosZ, render.getX() + 1 - mc.getRenderManager().viewerPosX, render.getY() - mc.getRenderManager().viewerPosY, render.getZ() + 1 - mc.getRenderManager().viewerPosZ);

            if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                if (renderRainbow.getValue()) {
                    if (renderFill.getValue()) {
                        RenderUtil.drawESP(bb, rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), fillAlpha.getValue());
                    }

                    if (renderOutline.getValue()) {
                        if (customOutline.getValue()) {
                            RenderUtil.drawESPOutline(bb, rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), outlineAlpha.getValue(), 1f);
                        } else {
                            RenderUtil.drawESPOutline(bb, rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), 255, 1f);
                        }
                    }

                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderHelper.disableStandardItemLighting();
                } else {
                    if (renderFill.getValue()) {
                        RenderUtil.drawESP(bb, fillRed.getValue(), fillGreen.getValue(), fillBlue.getValue(), fillAlpha.getValue());
                    }

                    if (renderOutline.getValue()) {
                        if (customOutline.getValue()) {
                            RenderUtil.drawESPOutline(bb, outlineRed.getValue(), outlineGreen.getValue(), outlineBlue.getValue(), outlineAlpha.getValue(), 1f);
                        } else {
                            RenderUtil.drawESPOutline(bb, fillRed.getValue(), fillGreen.getValue(), fillBlue.getValue(), 255, 1f);
                        }
                    }

                    GlStateManager.enableDepth();
                    GlStateManager.depthMask(true);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderHelper.disableStandardItemLighting();
                }

                if (renderDamage.getValue()) {
                    GlStateManager.pushMatrix();
                    RenderUtil.glBillboardDistanceScaled((float) render.getX() + 0.5f, (float) render.getY() + 0.5f, (float) render.getZ() + 0.5f, mc.player, 1);
                    double damage = DamageUtils.calculateDamage(render.getX() + .5, render.getY() + 1, render.getZ() + .5, target);
                    String damageText = (Math.floor(damage) == damage ? (int) damage : String.format("%.1f", damage)) + "";
                    GlStateManager.disableDepth();
                    GlStateManager.translate(-(mc.fontRenderer.getStringWidth(damageText) / 2.0d), 0, 0);
                    FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), damageText, 0, 0, new Color(140, 140, 140, 255).getRGB());
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0;
        double minimumDamage;
        double maximumDamageSelf = maxSelfDmg.getValue();

        EntityEnderCrystal bestCrystal = null;

        for (Entity c : mc.world.loadedEntityList) {
            if (!(c instanceof EntityEnderCrystal)) {
                continue;
            }

            EntityEnderCrystal crystal = (EntityEnderCrystal) c;

            if (mc.player.getDistance(crystal) > (!mc.player.canEntityBeSeen(crystal) ? wallsRange.getValue() : hitRange.getValue())) {
                continue;
            }

            if (crystal.isDead) {
                continue;
            }

            if (attackedCrystals.containsKey(crystal) && attackedCrystals.get(crystal) > 5) {
                continue;
            }

            for (Entity player : mc.world.playerEntities) {

                if (player == mc.player || !(player instanceof EntityPlayer)) {
                    continue;
                }

                if (Friends.isFriend(player.getName())) {
                    continue;
                }

                if (player.getDistance(mc.player) >= 11) {
                    continue;
                }

                final EntityPlayer target = (EntityPlayer) player;

                if (target.isDead || target.getHealth() <= 0) {
                    continue;
                }

                boolean noBreak = facePlaceCheck.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if (facePlace.getValue() && target.getHealth() + target.getAbsorptionAmount() <= facePlaceHp.getValue() && !noBreak) {
                    minimumDamage = 2;
                } else {
                    if (shouldActionArmor(target, false) && !noBreak && !shouldActionArmor(mc.player, true)) {
                        minimumDamage = 2;
                    } else {
                        minimumDamage = minDmg.getValue();
                    }
                }

                final double targetDamage = DamageUtils.calculateDamage(crystal, target);
                final double selfDamage = DamageUtils.calculateDamage(crystal, mc.player);

                if (targetDamage < minimumDamage) {
                    continue;
                }

                if (selfDamage > maximumDamageSelf || (noSuicide.getValue() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - selfDamage <= 0.5)) {
                    continue;
                }

                if (targetDamage > bestDamage) {
                    bestDamage = targetDamage;
                    bestCrystal = crystal;
                }
            }
        }

        return bestCrystal;
    }

    @EventHandler
    private Listener<PacketEvent.Send> packetSendListener = new Listener<>(event -> {
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && spoofRotations.getValue()) {
            if (isSpoofingAngles) {
                ((CPacketPlayer) packet).yaw = (float) yaw;
                ((CPacketPlayer) packet).pitch = (float) pitch;
            }
        }
    });

    @EventHandler
    private Listener<PacketEvent.Receive> packetReceiveListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    });

    @Override
    public void onEnable() {
        attackedCrystals.clear();
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "AutoCrystal has been toggled on!");
        }
    }

    @Override
    public void onDisable() {
        attackedCrystals.clear();
        resetRotation();
        if (toggleMsg.getValue() && mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED +  "AutoCrystal has been toggled off!");
        }
    }

    @Override
    public String getHudInfo() {
        if (target != null) {
            return "[" + ChatFormatting.GREEN + target.getName() + ChatFormatting.GRAY + "]";
        } else {
            return "["+ ChatFormatting.GREEN + "No target!" + ChatFormatting.GRAY + "]";
        }
    }

    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    private float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            }
            catch (Exception ex) {}
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
}
