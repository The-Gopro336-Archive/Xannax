package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.util.ColorHolder;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class InvPreview extends Module {
    Setting.Integer xSetting;
    Setting.Integer ySetting;

    public InvPreview() {
        super("InvPreview", "InvPreview", Category.Render);
        xSetting = registerInteger("X","X",  784, 0, 1000);
        ySetting = registerInteger("Y", "Y",46, 0,1000);
    }

    public void onRender() {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        RenderUtil.drawBorderedRect(xSetting.getValue(), ySetting.getValue(), xSetting.getValue() + 145, ySetting.getValue() + 48, 1, 0x75101010, ColorHolder.toHex(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue()));
        for (int i = 0; i < 27; i++) {
            final ItemStack itemStack = mc.player.inventory.mainInventory.get(i + 9);
            int offsetX = (xSetting.getValue() + (i % 9) * 16);
            int offsetY = (ySetting.getValue() + (i / 9) * 16);
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, offsetX, offsetY, null);
        }
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
