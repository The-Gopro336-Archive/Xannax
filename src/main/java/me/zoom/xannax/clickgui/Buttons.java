package me.zoom.xannax.clickgui;

import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.clickgui.buttons.*;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;

public class Buttons extends Component {

    public Module mod;
    public Frames parent;
    public int offset;
    private boolean isHovered;
    private final ArrayList<Component> subcomponents;
    public boolean open;
    private final int height;

    boolean hovering;

    private static final ResourceLocation opengui = new ResourceLocation("minecraft:opengui.png");
    private static final ResourceLocation closedgui = new ResourceLocation("minecraft:closedgui.png");

    public Buttons(final Module mod, final Frames parent, final int offset) {

        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<Component>();
        this.open = false;
        this.height = 16;
        this.hovering = false;
        int opY = offset + 16;
        if (Xannax.getInstance().settingsManager.getSettingsForMod(mod) != null && !Xannax.getInstance().settingsManager.getSettingsForMod(mod).isEmpty()) {
            for (final Setting s : Xannax.getInstance().settingsManager.getSettingsForMod(mod)) {
                switch (s.getType()) {
                    case MODE: {
                        this.subcomponents.add(new ModeComponent((Setting.Mode)s, this, mod, opY));
                        opY += 16;
                        continue;
                    }
                    case BOOLEAN: {
                        this.subcomponents.add(new BooleanComponent((Setting.Boolean)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    case DOUBLE: {
                        this.subcomponents.add(new DoubleComponent((Setting.Double)s, this, opY));
                        opY += 16;
                        continue;
                    }
                    case INT: {
                        this.subcomponents.add(new IntegerComponent((Setting.Integer)s, this, opY));
                        opY += 16;
                        continue;
                    }
                }
            }
        }
        this.subcomponents.add(new KeybindComponent(this, opY));
    }

    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 16;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 16;
        }
    }

    public void renderComponent() {
        ClickGUI.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.opacity.getValue()).getRGB();
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 16 + this.offset,  new Color(60, 60, 60, 255).getRGB());//this.isHovered ? (this.mod.isEnabled() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() -50).darker().darker().getRGB()) : (this.mod.isEnabled() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue()-50).getRGB()));
        Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1, new Color(60, 60, 60, 255).getRGB());//new Color(0, 0, 0, ClickGuiModule.opacity.getValue()-50).getRGB());
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + 2, this.mod.isEnabled() ? new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), 255).getRGB() : new Color(255, 255, 255, 255).getRGB());//isHovered ? new Color(255 ,255 ,255).getRGB() : new Color(200, 200, 200).getRGB());
        if (this.mod.isEnabled()) {
            Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() - 2, this.parent.getY() + 16 + this.offset,  new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue()).getRGB());
        }
        if (this.subcomponents.size() > 1) {
            //FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.open ? "-" : "+", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, isHovered ? new Color(255 ,255 ,255).getRGB() : new Color(200, 200, 200).getRGB());
        }
        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return 16 * (this.subcomponents.size() + 1);
        }
        return 16;
    }

    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
        if (this.isHovered) {
            RenderUtil.drawBorderedRect(mouseX + 7, mouseY - 3, mouseX + 7 + FontUtils.getStringWidth(FontUtils.getCustomFont(), this.mod.getDescription()) + 3, mouseY + FontUtils.getFontHeight(FontUtils.getCustomFont()) + 3, 1, new Color(0, 0, 0, 200).getRGB(), new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), 255).getRGB());
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.mod.getDescription(), mouseX + 8, mouseY, -1);
        }
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset;
    }

    public void drawOpenRender(int x, int y){
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(opengui);
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void drawClosedRender(int x, int y){
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(closedgui);
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPushMatrix();
        Gui.drawScaledCustomSizeModalRect(x,y,0,0,256,256,10,10,256,256);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
    }
}

