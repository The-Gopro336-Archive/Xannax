package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.module.Module;
import net.minecraft.tileentity.*;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.glEnable;

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", "StorageESP", Category.Render);
    }

    Setting.Integer w;
    ConcurrentHashMap<TileEntity, String> chests = new ConcurrentHashMap<>();

    public void setup(){
        w = registerInteger("Width", "Width", 2 , 1 ,10);
    }

    public void onUpdate(){
        mc.world.loadedTileEntityList.forEach(e->{
            chests.put(e, "");
        });
    }

    public void onWorldRender(RenderEvent event){
        Color c1 = new Color(255, 255, 0, 255);
        Color c2 = new Color(180, 70, 200, 255);
        Color c3 = new Color(150, 150, 150, 255);
        Color c4 = new Color(255,0,0, 255);
        if(chests != null && chests.size() > 0){
            RenderUtil.prepareGL();
            glEnable(GL_LINE_SMOOTH);
            chests.forEach((c, t)->{
                if(mc.world.loadedTileEntityList.contains(c)) {
                    if(c instanceof TileEntityChest)
                        RenderUtil.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox(mc.world, c.getPos()), (float)w.getValue(), c1.getRGB());
                    if(c instanceof TileEntityEnderChest)
                        RenderUtil.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox(mc.world, c.getPos()), (float)w.getValue(), c2.getRGB());
                    if(c instanceof TileEntityShulkerBox)
                        RenderUtil.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox(mc.world, c.getPos()), (float)w.getValue(), c4.getRGB());
                    if(c instanceof TileEntityDispenser
                            || c instanceof TileEntityFurnace
                            || c instanceof TileEntityHopper)
                        RenderUtil.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox(mc.world, c.getPos()), (float)w.getValue(), c3.getRGB());
                }
            });
            RenderUtil.releaseGL();
        }
    }
}

