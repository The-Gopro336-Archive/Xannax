package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.BlockUtils;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.GeometryMasks;
import me.zoom.xannax.module.Module;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VoidESP extends Module {
    public VoidESP(){
        super("VoidESP", "VoidESP", Category.Render);
    }

    Setting.Boolean rainbow;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    Setting.Integer renderDistance;
    Setting.Integer activeYValue;
    Setting.Mode renderType;
    Setting.Mode renderMode;
    Setting.Integer rV;
    Setting.Integer gV;
    Setting.Integer bV;
    Setting.Integer oW;
    Setting.Integer alpha;

    public void setup(){
        ArrayList<String> render = new ArrayList<>();
        render.add("Outline");
        render.add("Fill");
        render.add("Both");

        ArrayList<String> modes = new ArrayList<>();
        modes.add("Box");
        modes.add("Flat");

        rV = registerInteger("Red", "RedVoid", 255, 0, 255);
        gV = registerInteger("Green", "GreenVoid", 255, 0, 255);
        bV = registerInteger("Blue", "BlueVoid", 255, 0, 255);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        speed = registerInteger("Speed", "Speed", 50, 1, 100);
        alpha = registerInteger("Alpha", "Alpha", 50, 0, 255);
        oW = registerInteger("OutlineW", "OutlineWVoid", 2, 1, 10);
        renderDistance = registerInteger("Distance", "Distance", 10, 1, 40);
        activeYValue = registerInteger("Activate Y", "ActivateY", 20, 0, 256);
        renderType = registerMode("Render", "Render", render, "Both");
        renderMode = registerMode("Mode", "Mode", modes, "Flat");
    }

    private ConcurrentSet<BlockPos> voidHoles;

    @Override
    public void onUpdate(){
        if (mc.player.dimension == 1){
            return;
        }
        if (mc.player.getPosition().getY() > activeYValue.getValue()){
            return;
        }
        if (voidHoles == null){
            voidHoles = new ConcurrentSet<>();
        }else {
            voidHoles.clear();
        }

        List<BlockPos> blockPosList = BlockUtils.getCircle(getPlayerPos(), 0, renderDistance.getValue(), false);

        for (BlockPos blockPos : blockPosList){
            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK)) {
                continue;
            }
            if (isAnyBedrock(blockPos, Offsets.center)) {
                continue;
            }
            voidHoles.add(blockPos);
        }
    }

    @Override
    public void onWorldRender(RenderEvent event){
        final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(rV.getValue(), gV.getValue(), bV.getValue());
        final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
        if (mc.player == null || voidHoles == null){
            return;
        }
        if (mc.player.getPosition().getY() > activeYValue.getValue()){
            return;
        }
        if (voidHoles.isEmpty()){
            return;
        }
        voidHoles.forEach(blockPos -> {
            RenderUtil.prepare(GL11.GL_QUADS);
            if (renderMode.getValue().equalsIgnoreCase("Box")){
                drawBox(blockPos, rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
            } else {
                drawFlat(blockPos, rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
            }
            RenderUtil.release();
            RenderUtil.prepare(7);
            drawOutline(blockPos, oW.getValue(), rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue());
            RenderUtil.release();
        });
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
        for (BlockPos pos : offset) {
            if (mc.world.getBlockState(origin.add(pos)).getBlock().equals(Blocks.BEDROCK)) {
                return true;
            }
        } return false;
    }

    private static class Offsets {
        static final BlockPos[] center = {
                new BlockPos(0, 0, 0),
                new BlockPos(0, 1, 0),
                new BlockPos(0, 2, 0)
        };
    }

    public void drawFlat(BlockPos blockPos, int r, int g, int b) {
        if (renderType.getValue().equalsIgnoreCase("Fill") || renderType.getValue().equalsIgnoreCase("Both")) {
            Color color;
            AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
            if (renderMode.getValue().equalsIgnoreCase("Flat")) {
                color = new Color(r, g, b, alpha.getValue());
                RenderUtil.drawBox(blockPos, color.getRGB(), GeometryMasks.Quad.DOWN);
            }
        }
    }

    private void drawBox(BlockPos blockPos, int r, int g, int b) {
        if (renderType.getValue().equalsIgnoreCase("Fill") || renderType.getValue().equalsIgnoreCase("Both")) {
            Color color;
            AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
            color = new Color(r, g, b, alpha.getValue());
            RenderUtil.drawBox(blockPos, color.getRGB(), GeometryMasks.Quad.ALL);
        }
    }

    public void drawOutline(BlockPos blockPos, int width, int r, int g, int b) {
        if (renderType.getValue().equalsIgnoreCase("Outline") || renderType.getValue().equalsIgnoreCase("Both")) {
            final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
            hue[0] += .02f;
            if (renderMode.getValue().equalsIgnoreCase("Box")) {
                RenderUtil.drawBoundingBoxBlockPos(blockPos, width, r, g, b, 255);
            }
            if (renderMode.getValue().equalsIgnoreCase("Flat")) {
                    RenderUtil.drawBoundingBoxBottom2(blockPos, width, r, g, b, 255);
            }
        }
    }
}
