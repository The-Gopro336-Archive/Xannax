package me.zoom.xannax.clickgui;

import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Frames {

    public ArrayList<Component> guicomponents;
    public Module.Category category;
    private final int width;
    private final int barHeight;
    private int height;
    public int x;
    public int y;
    public int dragX;
    public int dragY;
    private boolean isDragging;
    public boolean open;
    public int buttonHeightProper;
    boolean font;

    int mouseX;
    int mouseY;

    boolean hovering;

    int animation;

    public Frames(final Module.Category catg){
        this.guicomponents = new ArrayList<Component>();
        this.category = catg;
        this.open = true;
        this.isDragging = false;
        this.x = 5;
        this.y = 5;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int tY = this.barHeight;

        this.buttonHeightProper = 0;

        this.mouseX = 0;
        this.mouseY = 0;

        this.hovering = false;

        this.animation = 50;

        for (final Module mod : ModuleManager.getModulesInCategory(catg)){
            final Buttons devmodButton = new Buttons(mod, this, tY);
            this.guicomponents.add(devmodButton);
            tY += 16;
        }
        this.refresh();
    }

    public ArrayList<Component> getComponents() {
        return this.guicomponents;
    }

    public int getWidth() {
        return this.width;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(final int newX) {
        this.x = newX;
    }

    public void setY(final int newY) {
        this.y = newY;
    }

    public void renderGUIFrame(final FontRenderer fontRenderer){


        final Color rainbowColor1 = new Color(RenderUtil.getRainbow(ClickGuiModule.speed.getValue() * 100, 0, ClickGuiModule.saturation.getValue() / 100.0f, ClickGuiModule.brightness.getValue() / 100.0f));
        final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), 255);

        if (ClickGuiModule.rainbowType.getValue().equalsIgnoreCase("Frame")) {
            Gui.drawRect(this.x - 2, this.y - 4, this.x + this.width + 2, this.y + 16, rainbowColor2.getRGB());
        } else {
            Gui.drawRect(this.x - 2, this.y - 4, this.x + this.width + 2, this.y + 16, new Color(36, 36, 36, 255).getRGB());
        }
        if (ClickGuiModule.rainbowType.getValue().equalsIgnoreCase("Text")) {
            if (font)
                Xannax.fontRenderer.drawStringWithShadow(this.category.name(), (float) (this.x + 2), (float) (this.y + 3), rainbowColor2.getRGB());
            else
                FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + 2, this.y + 3, rainbowColor2.getRGB());
        } else {
            if (font)
                Xannax.fontRenderer.drawStringWithShadow(this.category.name(), (float) (this.x + 2), (float) (this.y + 3), -1);
            else
                FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + 2, this.y + 3, new Color(255, 255, 255, 255).getRGB());
        }
        if (this.open && !this.guicomponents.isEmpty()){
            for (final Component component : this.guicomponents){
                component.renderComponent();
            }
        }
    }
    public int getAnimation(){
        return animation;
    }

    public boolean isHovering() {
        return hovering;
    }

    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isWithinFrame(final int x, final int y){
        if (isWithinHeader(mouseX, mouseY)){
            return true;
        } else if (x >= this.x && x <= this.x + this.width && y >= this.y - 1 && y <= this.y + this.barHeight + this.buttonHeightProper){
            return true;
        } else return false;
    }

    public void updatePosition(final int mouseX, final int mouseY){
        if (this.isDragging){
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isWithinHeader(final int x, final int y){
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public void setDrag(final boolean drag){
        this.isDragging = drag;
    }

    public void setOpen(final boolean open){
        this.open = open;
    }

    public boolean isOpen(){
        return this.open;
    }

    public void refresh(){
        int off = this.barHeight;
        for (final Component comp : this.guicomponents){
            comp.setOff(off);
            off += comp.getHeight();
        }
        this.height = off;
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (final Frames frames : ClickGUI.frames) {
            if (scrollWheel < 0) {
                frames.setY(frames.getY() - 10);
            }
            else if (scrollWheel > 0) {
                frames.setY(frames.getY() + 10);
            }
        }
    }

    public static void scissorBox(int x, int y, int w, int h, int screenHeight){
        ScaledResolution scaledResolution = new ScaledResolution(Xannax.mc);
        int interpolatedX = x * scaledResolution.getScaleFactor();
        int interpolatedY = y * scaledResolution.getScaleFactor();
        int interpolatedW = interpolatedX + (w * scaledResolution.getScaleFactor());
        int interpolatedH = interpolatedY + (h * scaledResolution.getScaleFactor());
        GL11.glScissor(interpolatedX, screenHeight - interpolatedH, interpolatedW - interpolatedX, interpolatedH - interpolatedY);
    }
}
