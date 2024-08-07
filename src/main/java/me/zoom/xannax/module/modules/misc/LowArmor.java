package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.util.Wrapper;
import me.zoom.xannax.Xannax;
import net.minecraft.item.ItemStack;
import me.zoom.xannax.module.ModuleManager;
import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class LowArmor extends Module {
    public LowArmor(){super("DurabilityWarning", "DurabilityWarning", Category.Misc);}

    Setting.Integer x;
    Setting.Integer y;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    Setting.Integer threshold;
    Color c;

    @Override
    public void setup() {
        threshold = registerInteger("Percent", "Percent", 50, 0, 100);
        x = registerInteger("X", "X", 255, 0, 960);
        y = registerInteger("Y", "Y", 255, 0, 530);
        red = registerInteger("Red", "RedArmor", 255, 0, 255);
        green = registerInteger("Green", "GreenArmor", 255, 0, 255);
        blue = registerInteger("Blue", "BlueArmor", 255, 0, 255);
    }

    @Override
    public void onRender() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb & 0xFF;
        if (this.shouldMend(0) || this.shouldMend(1) || this.shouldMend(2) || this.shouldMend(3)) {
            final String text = "Armor Durability Is Below " + this.threshold.getValue() + "%";
            final int divider = getScale();
            drawStringWithShadow(text, x.getValue(), y.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
    }

    private boolean shouldMend(final int i) {
        return ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(i)).getMaxDamage() != 0 && 100 * ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(i)).getItemDamage() / ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(i)).getMaxDamage() > reverseNumber(this.threshold.getValue(), 1, 100);
    }

    public static int reverseNumber(final int num, final int min, final int max) {
        return max + min - num;
    }

    public static int getScale() {
        int scaleFactor = 0;
        int scale = Wrapper.getMinecraft().gameSettings.guiScale;
        if (scale == 0) {
            scale = 1000;
        }
        while (scaleFactor < scale && Wrapper.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        if (scaleFactor == 0) {
            scaleFactor = 1;
        }
        return scaleFactor;
    }

    private void drawStringWithShadow (String text,int x, int y, int color) {
        if (ModuleManager.isModuleEnabled("CustomFont"))
            Xannax.fontRenderer.drawStringWithShadow(text, x, y, color);
        else
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }


}

