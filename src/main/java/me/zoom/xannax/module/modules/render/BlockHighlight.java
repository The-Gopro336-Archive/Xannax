package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.GeometryMasks;
import me.zoom.xannax.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import static org.lwjgl.opengl.GL11.*;

public class BlockHighlight extends Module {
    public BlockHighlight() {
        super("BlockHighlight", "BlockHighlight", Category.Render);
    }

    Setting.Integer w;
    Setting.Boolean shade;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    Setting.Integer alpha;

    int c; //outline
    int c2; //fill

    public void setup() {
        shade = registerBoolean("Fill", "Fill", false);
        w = registerInteger("Width", "Width", 2, 1, 10);
        red = registerInteger("Red", "RedBH", 255, 0, 255);
        green = registerInteger("Green", "GreenBH", 255, 0, 255);
        blue = registerInteger("Blue", "BlueBH", 255, 0, 255);
        alpha = registerInteger("Alpha", "AlphaBH", 50, 0, 255);
    }

    public void onWorldRender(RenderEvent event) {
        RayTraceResult ray = mc.objectMouseOver;
        AxisAlignedBB bb;
        BlockPos pos;
        c = new Color(red.getValue(), green.getValue(), blue.getValue(), 255).getRGB();
        c2 = new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()).getRGB();
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            pos = ray.getBlockPos();
            bb = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos);
            if (bb != null && pos != null && mc.world.getBlockState(pos).getMaterial() != Material.AIR) {
                RenderUtil.prepareGL();
                glEnable(GL_LINE_SMOOTH);
                RenderUtil.drawBoundingBox(bb, w.getValue(), c);
                RenderUtil.releaseGL();
                if (shade.getValue()) {
                    RenderUtil.prepare(GL11.GL_QUADS);
                    RenderUtil.drawBox(bb, c2, GeometryMasks.Quad.ALL);
                    RenderUtil.release();
                }
            }
        }
    }
}

