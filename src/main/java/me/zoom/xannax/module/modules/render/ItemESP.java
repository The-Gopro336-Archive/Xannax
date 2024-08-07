package me.zoom.xannax.module.modules.render;


import java.awt.Color;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.EntityUtil;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;

public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP", "ItemESP", Category.Render);
    }

    Setting.Boolean items;
    Setting.Boolean xporbs;
    Setting.Boolean xpbottles;
    Setting.Boolean pearl;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    Setting.Integer boxAlpha;
    Setting.Integer alpha;
    Setting.Boolean rainbow;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    private static ESP INSTANCE;

    public void setup() {

        this.items = registerBoolean("Items", "Items", false);
        this.xporbs = registerBoolean("XpOrbs", "XpOrbs", false);
        this.xpbottles = registerBoolean("XpBottles", "XpBottles", false);
        this.pearl = registerBoolean("Pearls", "Pearls", false);
        this.red = registerInteger("Red", "Red", 255, 0, 255);
        this.green = registerInteger("Green", "Green", 255, 0, 255);
        this.blue = registerInteger("Blue", "Blue", 255, 0, 255);
        this.boxAlpha = registerInteger("BoxAlpha", "BoxAlpha", 120, 0, 255);
        this.alpha = registerInteger("Alpha", "Alpha", 255, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (this.items.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityItem && ESP.mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(red.getValue(), green.getValue(), blue.getValue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityXPOrb && ESP.mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(red.getValue(), green.getValue(), blue.getValue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.pearl.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderPearl && ESP.mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(red.getValue(), green.getValue(), blue.getValue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityExpBottle && ESP.mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                    final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(red.getValue(), green.getValue(), blue.getValue());
                    final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(bb, rainbowColor2.getRed() / 255.0f, rainbowColor2.getGreen() / 255.0f, rainbowColor2.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(bb, new Color(rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
}
