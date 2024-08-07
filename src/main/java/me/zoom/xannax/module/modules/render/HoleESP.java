package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.GeometryMasks;
import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HoleESP extends Module {

    public HoleESP(){
        super("HoleESP", "HoleESP", Category.Render);
    }

    //settings
    public static Setting.Integer rangeS;
    Setting.Integer redb;
    Setting.Integer greenb;
    Setting.Integer blueb;
    Setting.Integer redo;
    Setting.Integer greeno;
    Setting.Integer blueo;
    Setting.Integer outlineW;
    Setting.Integer alpha;
    Setting.Boolean rainbow;
    Setting.Boolean hideOwn;
    Setting.Boolean flatOwn;
    Setting.Mode mode;
    Setting.Mode type;

    //load settings
    public void setup(){
        rangeS = registerInteger("Range", "Range", 5, 1, 20);
        rainbow = registerBoolean("Rainbow", "Rainbow", false);
        hideOwn = registerBoolean("Hide Own", "HideOwn", false);
        flatOwn = registerBoolean("Flat Own", "FlatOwn", false);
        redb = registerInteger("Red Brock", "RedBrock", 255, 0, 255);
        greenb = registerInteger("Green Brock", "GreenBrock", 255, 0, 255);
        blueb = registerInteger("Blue Brock", "BlueBrock", 255, 0, 255);
        redo = registerInteger("Red Obi", "Red Obi", 255, 0, 255);
        greeno = registerInteger("Green Obi", "Green Obi", 255, 0, 255);
        blueo = registerInteger("Blue Obi", "Blue Obi", 255, 0, 255);
        alpha = registerInteger("Alpha", "AlphaHoleESP", 50, 0, 255);
        outlineW = registerInteger("OutlineW", "OutlineW", 2, 1, 12);

        ArrayList<String> render = new ArrayList<>();
        render.add("Outline");
        render.add("Fill");
        render.add("Both");

        ArrayList<String> modes = new ArrayList<>();
        modes.add("Air");
        modes.add("Ground");
        modes.add("Flat");
        modes.add("Slab");
        modes.add("Sexc");

        type = registerMode("Render", "Render", render, "Both");
        mode = registerMode("Mode", "Mode", modes, "Air");
    }

    //defines the render borders
    private final BlockPos[] surroundOffset = {
            new BlockPos(0, -1, 0), // down
            new BlockPos(0, 0, -1), // north
            new BlockPos(1, 0, 0), // east
            new BlockPos(0, 0, 1), // south
            new BlockPos(-1, 0, 0) // west
    };

    //used to register safe holes for rendering
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;

    //defines the area for the client to search
    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    //gets the players location
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    //finds safe holes to render
    @Override
    public void onUpdate(){
        if (safeHoles == null){
            safeHoles = new ConcurrentHashMap<>();
        }
        else{
            safeHoles.clear();
        }

        int range = (int) Math.ceil(rangeS.getValue());

        List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);
        for (BlockPos pos : blockPosList){

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)){
                continue;
            }
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)){
                continue;
            }
            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)){
                continue;
            }
            if (this.hideOwn.getValue() && pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))){
                continue;
            }

            boolean isSafe = true;
            boolean isBedrock = true;

            for (BlockPos offset : surroundOffset){
                Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
                if (block != Blocks.BEDROCK){
                    isBedrock = false;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL){
                    isSafe = false;
                    break;
                }
            }
            if (isSafe){
                safeHoles.put(pos, isBedrock);
            }
        }
    }

    //renders safe holes
    @Override
    public void onWorldRender(final RenderEvent event){
        if (mc.player == null || safeHoles == null){
            return;
        }
        if (safeHoles.isEmpty()){
            return;
        }
        RenderUtil.prepare(GL11.GL_QUADS);

        if(mode.getValue().equalsIgnoreCase("Air")) {
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    drawBox(blockPos, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawBox(blockPos, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if(mode.getValue().equalsIgnoreCase("Ground")) {
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    drawBox2(blockPos, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawBox2(blockPos, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Flat")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock){
                    drawFlat(blockPos, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawFlat(blockPos, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Slab")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock){
                    drawSlab(blockPos, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawSlab(blockPos, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Sexc")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock){
                    RenderUtil.drawGradientFilledBox(blockPos, new Color(redb.getValue(), greenb.getValue(), blueb.getValue(), 144), new Color(redb.getValue(), greenb.getValue(), blueb.getValue(), 0));
                } else RenderUtil.drawGradientFilledBox(blockPos, new Color(redo.getValue(), greeno.getValue(), blueo.getValue(), 144), new Color(redo.getValue(), greeno.getValue(), blueo.getValue(), 0));
            });

        }
        RenderUtil.release();
        RenderUtil.prepare(7);
        if (mode.getValue().equalsIgnoreCase("Air")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    drawOutline(blockPos,1, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawOutline(blockPos,1, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Ground")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    drawOutline(blockPos,1, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawOutline(blockPos,1, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Flat")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    drawOutline(blockPos,1, redb.getValue(), greenb.getValue(), blueb.getValue());
                } else drawOutline(blockPos,1, redo.getValue(), greeno.getValue(), blueo.getValue());
            });
        }
        if (mode.getValue().equalsIgnoreCase("Sexc")){
            safeHoles.forEach((blockPos, isBedrock) -> {
                if (isBedrock) {
                    RenderUtil.drawGradientBlockOutline(blockPos, new Color(redb.getValue(), greenb.getValue(), blueb.getValue(), 0), new Color(redb.getValue(), greenb.getValue(), blueb.getValue(), 144), 1);
                } else RenderUtil.drawGradientBlockOutline(blockPos, new Color(redo.getValue(), greeno.getValue(), blueo.getValue(), 0), new Color(redo.getValue(), greeno.getValue(), blueo.getValue(), 144), 1);
            });
        }
        RenderUtil.release();
    }

    //renders air boxes
    private void drawBox(BlockPos blockPos, int r, int g, int b) {
        if (type.getValue().equalsIgnoreCase("Fill") || type.getValue().equalsIgnoreCase("Both")) {
            Color color;
            AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
            color = new Color(r, g, b, alpha.getValue());

            if (mode.getValue().equalsIgnoreCase("Air")) {
                if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)))
                    RenderUtil.drawBox(blockPos, color.getRGB(), GeometryMasks.Quad.DOWN);
                else
                    RenderUtil.drawBox(blockPos, color.getRGB(), GeometryMasks.Quad.ALL);
            }
        }
    }

    //renders ground boxes
    public void drawBox2(BlockPos blockPos, int r, int g, int b){
        if (type.getValue().equalsIgnoreCase("Fill") || type.getValue().equalsIgnoreCase("Both")) {
            Color color;
            AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);

            color = new Color(r, g, b, alpha.getValue());

            if (mode.getValue().equalsIgnoreCase("Ground")) {
                RenderUtil.drawBox2(blockPos, color.getRGB(), GeometryMasks.Quad.ALL);
            }
        }
    }

    public void drawFlat(BlockPos blockPos, int r, int g, int b) {
        if (type.getValue().equalsIgnoreCase("Fill") || type.getValue().equalsIgnoreCase("Both")) {
            Color color;
            AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
            if (mode.getValue().equalsIgnoreCase("Flat")) {
                color = new Color(r, g, b, alpha.getValue());
                RenderUtil.drawBox(blockPos, color.getRGB(), GeometryMasks.Quad.DOWN);
            }
        }
    }

    public void drawSlab(BlockPos blockPos,  int r, int g, int b) {
        final AxisAlignedBB bb = new AxisAlignedBB(blockPos.getX() - mc.getRenderManager().viewerPosX, blockPos.getY() + 0.1 - mc.getRenderManager().viewerPosY, blockPos.getZ() - mc.getRenderManager().viewerPosZ, blockPos.getX() + 1 - mc.getRenderManager().viewerPosX, blockPos.getY() - mc.getRenderManager().viewerPosY, blockPos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
            if (type.getValue().equalsIgnoreCase("Fill") || type.getValue().equalsIgnoreCase("Both")) {
                RenderUtil.drawESP(bb, r, g, b, alpha.getValue());
            }
            if (type.getValue().equalsIgnoreCase("Outline") || type.getValue().equalsIgnoreCase("Both")) {
                RenderUtil.drawESPOutline(bb, r, g, b, 255f, outlineW.getValue());
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void drawOutline(BlockPos blockPos, int width, int r, int g, int b) {
        if (type.getValue().equalsIgnoreCase("Outline") || type.getValue().equalsIgnoreCase("Both")) {
            final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
            hue[0] += .02f;
            if (mode.getValue().equalsIgnoreCase("Air")) {
                if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
                    RenderUtil.drawBoundingBoxBottom2(blockPos, outlineW.getValue(), r, g, b, 255);
                } else {
                    RenderUtil.drawBoundingBoxBlockPos(blockPos, outlineW.getValue(), r, g, b, 255);

                }
            }
            if (mode.getValue().equalsIgnoreCase("Flat")) {
                RenderUtil.drawBoundingBoxBottom2(blockPos, outlineW.getValue(), r, g, b, 255);
            }
            if (mode.getValue().equalsIgnoreCase("Ground")) {
                RenderUtil.drawBoundingBoxBlockPos2(blockPos, outlineW.getValue(), r, g, b, 255);
            }
        }
    }

    @Override
    public String getHudInfo(){
        String t = "";
        t = "[" + ChatFormatting.WHITE + mode.getValue() + ", " + type.getValue() + ChatFormatting.GRAY + "]";
        return t;
    }
}
