package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.RenderUtil;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BreakESP extends Module {

    Setting.Integer alpha;
    Setting.Boolean ignoreSelf;
    Setting.Integer alphaF;
    Setting.Integer red;
    Setting.Boolean fade;
    Setting.Integer blue;
    Setting.Integer green;
    Setting.Mode mode;
    Setting.Boolean onlyObby;

    public void setup() {
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Solid");
        modes.add("Outline");
        modes.add("Full");
        ignoreSelf = registerBoolean("IgnoreSelf", "IgnoreSelf", false);
        onlyObby = registerBoolean("OnlyObi", "OnlyObi", true);
        alpha = registerInteger("Alpha", "Alpha", 50, 0, 255);
        alphaF = registerInteger("AlphaF", "AlphaF", 50, 0, 255);
        fade = registerBoolean("Fade", "Fade", false);
        red = registerInteger("Red", "Red", 255, 0, 255);
        green = registerInteger("Green", "Green", 255, 0, 255);
        blue = registerInteger("Blue", "Blue", 255, 0, 255);
        mode = registerMode("Mode", "Mode", modes, "Solid");
    }
    private ConcurrentSet test = new ConcurrentSet();
    public ConcurrentSet breaking = new ConcurrentSet();
    float inc;
    BlockPos pos;
    public static BreakESP INSTANCE;
    private Map alphaMap = new HashMap();
    private ArrayList options;

    public BreakESP() {
        super("BreakESP", "BreakESP", Category.Render);
        this.alphaMap.put(0, 28);
        this.alphaMap.put(1, 56);
        this.alphaMap.put(2, 84);
        this.alphaMap.put(3, 112);
        this.alphaMap.put(4, 140);
        this.alphaMap.put(5, 168);
        this.alphaMap.put(6, 196);
        this.alphaMap.put(7, 224);
        this.alphaMap.put(8, 255);
        this.alphaMap.put(9, 255);
    }

    public void onWorldRender(RenderEvent var1) {
        mc.renderGlobal.damagedBlocks.forEach((var1x, var2) -> {
            if (var2 != null) {
                if (!(Boolean)this.ignoreSelf.getValue() || mc.world.getEntityByID(var1x) != mc.player) {
                    if (!(Boolean)this.onlyObby.getValue() || mc.world.getBlockState(var2.getPosition()).getBlock() == Blocks.OBSIDIAN) {
                        int var3 = (Boolean)this.fade.getValue() ? (Integer)this.alphaMap.get(var2.getPartialBlockDamage()) : (Integer)this.alpha.getValue();
                        if (((String)this.mode.getValue()).equalsIgnoreCase("Solid")) {
                            RenderUtil.prepare(7);
                            RenderUtil.drawBox(var2.getPosition(), (Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), var3, 63);
                            RenderUtil.release();
                        } else {
                            IBlockState var4;
                            Vec3d var5;
                            if (((String)this.mode.getValue()).equalsIgnoreCase("Full")) {
                                var4 = mc.world.getBlockState(var2.getPosition());
                                var5 = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
                                RenderUtil.drawFullBox(var4.getSelectedBoundingBox(mc.world, var2.getPosition()).grow(0.0020000000949949026D).offset(-var5.x, -var5.y, -var5.z), var2.getPosition(), 1.5F, (Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), var3, (Integer)this.alphaF.getValue());
                            } else if (((String)this.mode.getValue()).equalsIgnoreCase("Outline")) {
                                var4 = mc.world.getBlockState(var2.getPosition());
                                var5 = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
                                RenderUtil.drawBoundingBox(var4.getSelectedBoundingBox(mc.world, var2.getPosition()).grow(0.0020000000949949026D).offset(-var5.x, -var5.y, -var5.z), 1.5F, (Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), var3);
                            } else {
                                RenderUtil.prepare(7);
                                RenderUtil.drawBox(var2.getPosition(), (Integer)this.red.getValue(), (Integer)this.green.getValue(), (Integer)this.blue.getValue(), var3, 63);
                                RenderUtil.release();
                            }
                        }

                    }
                }
            }
        });
    }
}

