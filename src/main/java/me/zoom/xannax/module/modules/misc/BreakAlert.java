package me.zoom.xannax.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import me.zoom.xannax.command.Command;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.init.Items;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import me.zoom.xannax.setting.Setting;
import net.minecraft.util.math.BlockPos;
import io.netty.util.internal.ConcurrentSet;
import me.zoom.xannax.module.Module;

public class BreakAlert extends Module
{
    Setting.Mode mode;
    Setting.Boolean ignoreSelf;
    Setting.Integer x;
    Setting.Integer y;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    private ConcurrentSet<BlockPos> breaking;
    private ConcurrentSet<BlockPos> test;
    public BlockPos[] xd;
    boolean testy;
    int delay;

    public BreakAlert() {
        super("BreakAlert", "Watch out bro!", Category.Misc);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Chat");
        modes.add("Text");
        modes.add("Dot");
        this.mode = registerMode("Mode", "Mode", modes, "Chat");
        this.ignoreSelf = registerBoolean("Ignore Self", "IgnoreSelf", true);
        x = registerInteger("X", "X", 255, 0, 960);
        y = registerInteger("Y", "Y", 255, 0, 530);
        red = registerInteger("Red", "RedArmor", 255, 0, 255);
        green = registerInteger("Green", "GreenArmor", 255, 0, 255);
        blue = registerInteger("Blue", "BlueArmor", 255, 0, 255);
        this.breaking = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.test = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        this.xd = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
    }

    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        this.test.clear();
        if (BreakAlert.mc.world == null) {
            return;
        }
        BreakAlert.mc.world.playerEntities.forEach(entityPlayer -> {
            RayTraceResult ray;
            RayTraceResult ray2;
            if (this.ignoreSelf.getValue()) {
                if (!entityPlayer.getName().equalsIgnoreCase(BreakAlert.mc.player.getName()) && entityPlayer.isSwingInProgress && entityPlayer.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
                    ray = entityPlayer.rayTrace(5.0, BreakAlert.mc.getRenderPartialTicks());
                    if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && BreakAlert.mc.world.getBlockState(ray.getBlockPos()).getBlock() != Blocks.BEDROCK) {
                        this.test.add(ray.getBlockPos());
                    }
                }
            }
            else if (entityPlayer.isSwingInProgress && entityPlayer.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
                ray2 = entityPlayer.rayTrace(5.0, BreakAlert.mc.getRenderPartialTicks());
                if (ray2 != null && ray2.typeOfHit == RayTraceResult.Type.BLOCK && BreakAlert.mc.world.getBlockState(ray2.getBlockPos()).getBlock() != Blocks.BEDROCK) {
                    this.test.add(ray2.getBlockPos());
                }
            }
            return;
        });
        this.breaking.removeIf(blockPos -> !this.test.contains((Object)blockPos));
        this.breaking.addAll((Collection)this.test);
        this.testy = false;
        for (final BlockPos pos : this.xd) {
            final BlockPos pos2 = new BlockPos(BreakAlert.mc.player.posX, BreakAlert.mc.player.posY, BreakAlert.mc.player.posZ).add(pos.x, pos.y, pos.z);
            if (!this.breaking.isEmpty() && this.breaking.contains((Object)pos2)) {
                this.testy = true;
            }
        }
        if (this.testy && this.mode.getValue().equalsIgnoreCase("Chat")) {
            if (this.delay == 0) {
                    Command.sendClientMessage(ChatFormatting.RED + "Your feet are being mined!");
            }
            this.delay = 100;
        }
    }

    @Override
    public void onRender() {
        if (this.testy && this.mode.getValue().equalsIgnoreCase("dot")) {
            GlStateManager.pushMatrix();
            Gui.drawRect(BreakAlert.mc.displayWidth / 4 - 3, BreakAlert.mc.displayHeight / 4 - 3, BreakAlert.mc.displayWidth / 4 + 4, BreakAlert.mc.displayHeight / 4 + 4, new Color(255, 0, 0, 255).getRGB());
            GlStateManager.popMatrix();
        }
        else if (this.testy && this.mode.getValue().equalsIgnoreCase("Text")) {
            GlStateManager.pushMatrix();
            drawStringWithShadow("Your Feet Are Being Mined", x.getValue(), y.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
            GlStateManager.popMatrix();
        }
    }

    private void drawStringWithShadow (String text,int x, int y, int color) {
        if (ModuleManager.isModuleEnabled("CustomFont"))
            Xannax.fontRenderer.drawStringWithShadow(text, x, y, color);
        else
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
}
