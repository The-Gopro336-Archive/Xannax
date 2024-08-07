package me.zoom.xannax.module.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.util.*;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import java.text.SimpleDateFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import java.util.Date;


import java.awt.*;
import java.util.Comparator;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ArrayList;

public class HUD extends Module {
    public HUD(){
        super("HUD", "HUD", Category.Client);
        timer = new Timer();
    }

    Setting.Boolean Greeter;
    Setting.Integer GreeterX;
    Setting.Integer GreeterY;
    Setting.Boolean Watermark;
    Setting.Integer WatermarkY;
    //Setting.Boolean WatermarkRainbow;
    Setting.Boolean ArrayList;
    Setting.Boolean ArrayListHot;
    Setting.Mode ArrayListMode;
    Setting.Boolean sortUp;
    Setting.Boolean Potions;
    Setting.Boolean pSortUp;
    Setting.Boolean Hole;
    Setting.Integer holex;
    Setting.Integer holey;
    Setting.Boolean XYZ;
    Setting.Boolean xyzNoGray;
    Setting.Boolean Direction;
    Setting.Boolean ArmorHud;
    Setting.Boolean Ping;
    Setting.Boolean FPS;
    Setting.Boolean TPS;
    Setting.Boolean durability;
    Setting.Boolean Speed;
    Setting.Boolean Time;
    Setting.Boolean tots;
    Setting.Boolean playerViewer;
    Setting.Integer playerViewerX;
    Setting.Integer playerViewerY;
    Setting.Double playerScale;
    Setting.Boolean rainbow;
    Setting.Integer saturation;
    Setting.Integer brightness;
    Setting.Integer speed;
    Setting.Integer red;
    Setting.Integer green;
    Setting.Integer blue;
    public static Setting.Boolean rainbowArray;
    Setting.Boolean textRadar;
    Setting.Integer textRadarY;
    public static Setting.Integer deelay;
    public static Setting.Integer arraySat;
    public static Setting.Integer arrayBri;
    public static Setting.Boolean potionIcons;
    //Setting.Boolean test;
    Color color;
    int modCount;
    int sort;
    int potCount;
    int counter;
    private int value = 1;
    private boolean isChanged = false;
    private boolean isReversing = false;
    private final Timer timer;
    int maxvalue = 255;
    int minvalue = 150;

    private static final float[] tickRates = new float[20];

    DecimalFormat format2 = new DecimalFormat("00");
    private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    private static final ItemStack totemm = new ItemStack(Items.TOTEM_OF_UNDYING);

    public void setup() {
        ArrayList<String> arraymodes = new ArrayList<>();
        arraymodes.add("Static");
        arraymodes.add("Pulse");

        ArrayList = registerBoolean("ArrayList", "ArrayList",  false);
        ArrayListHot = registerBoolean("ArrayList Hot", "ArrayListHot", true);
        ArrayListMode = registerMode("ArrayListMode", "ArrayListMode", arraymodes, "Static");
        sortUp = registerBoolean("Array Sort Up", "ArraySortUp", false);
        rainbowArray = registerBoolean("RainbowArray", "RainbowArray", false);
        arraySat = registerInteger("ArraySat", "ArraySat", 100, 0, 100);
        arrayBri = registerInteger("ArrayBri", "ArrayBri", 100, 0, 100);
        Potions = registerBoolean("Potions", "Potions",  false);
        pSortUp = registerBoolean("Potions Sort Up", "PotionsSortUp", false);
        Watermark = registerBoolean("Watermark", "Watermark", false);
        WatermarkY = registerInteger("Watermark Y", "WatermarkY", 2, 2, 1000);
        //WatermarkRainbow = registerBoolean("Watermark2", "Watermark2", false);
        Greeter = registerBoolean("Greeter", "Greeter", false);
        GreeterX = registerInteger("Greeter X", "GreeterX", 100, 0, 1000);
        GreeterY = registerInteger("Greeter Y", "GreeterY", 100, 0, 1000);
        ArmorHud = registerBoolean("ArmorHud", "ArmorHud", false);
        Hole = registerBoolean("Hole", "Hole", false);
        holex = registerInteger("Hole X", "HoleX", 0, 0, 1000);
        holey = registerInteger("Hole Y", "HoleY", 0, 0, 1000);
        tots = registerBoolean("Totems", "TotemsHUD", false);
        Ping = registerBoolean("Ping", "Ping", false);
        FPS = registerBoolean("FPS", "FPS", false);
        TPS = registerBoolean("TPS", "TPS", false);
        Time = registerBoolean("Time", "Time", false);
        Speed = registerBoolean("Speed", "Speed", false);
        durability = registerBoolean("Durability", "Durability", false);
        XYZ = registerBoolean("XYZ", "XYZ", false);
        Direction = registerBoolean("Direction", "Direction", false);
        xyzNoGray = registerBoolean("Coords No Gray", "XYZNoGray", false);
        textRadar = registerBoolean("TextRadar", "TextRadar", false);
        textRadarY = registerInteger("TextRadarY", "TextRadarY", 100, 0, 1000);
        playerViewer = registerBoolean("Player Viewer", "PlayerViewer", false);
        playerViewerX = registerInteger("Player Viewer X", "PlayerViewerX", 0, 0, 1000);
        playerViewerY = registerInteger("Player Viewer Y", "PlayerViewerY", 0, 0, 1000);
        playerScale = registerDouble("Player Scale", "PlayerScale", 1.0, 0.1, 2.0);
        potionIcons = registerBoolean("PotionIcons", "PotionIcons", false);
        //red = registerInteger("Red", "Red", 255, 0, 255);
        //green = registerInteger("Green", "Green", 255, 0, 255);
        //blue = registerInteger("Blue", "Blue", 255, 0, 255);
        //rainbow = registerBoolean("Rainbow", "Rainbow", false);
        //saturation = registerInteger("Saturation", "Saturation", 50, 0, 100);
        //brightness = registerInteger("Brightness", "Brightness", 50, 0, 100);
        //speed = registerInteger("Speed", "Speed", 50, 1, 100);
        deelay = registerInteger("hue", "hue", 240, 0, 600);
        //test = registerBoolean("test", "test", false);
    }

    public void onRender() {
        if (timer.hasTimePassed(5)) {
            value += isReversing ? -1 : 1;

            if (isReversing && value == minvalue) {
                isReversing = false;
            } else if (!isReversing && value == maxvalue) {
                isReversing = true;
            }
            timer.reset();
        }



        final boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)HUD.mc.player.posX;
        final int posY = (int)HUD.mc.player.posY;
        final int posZ = (int)HUD.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(HUD.mc.player.posX * nether);
        final int hposZ = (int)(HUD.mc.player.posZ * nether);
        counter = 0;
        //final Color rainbowColor1 = rainbow.getValue() ? new Color(RenderUtil.getRainbow(speed.getValue() * 100, 0, saturation.getValue() / 100.0f, brightness.getValue() / 100.0f)) : new Color(red.getValue(), green.getValue(), blue.getValue());
        //final Color rainbowColor2 = new Color(rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue());
        color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue());
        if (XYZ.getValue()) {
            if (xyzNoGray.getValue()) {
                String coords = "XYZ " + posX + ", " + posY + ", " + posZ + " " + "[" + hposX + ", " + hposZ + "]";
                final int[] counter4 = { 1 };
                final char[] stringToCharArray3 = coords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    drawStringWithShadow(String.valueOf(c3), (int) (2 + u), new ScaledResolution(mc).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14 : FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2), rainbowArray.getValue() ? RainbowUtil.rainbow(counter4[0] * deelay.getValue()) : color.getRGB());
                    u += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c3));
                    ++counter4[0];
                }
            } else {
                drawStringWithShadow("XYZ " + ChatFormatting.GRAY + posX + ", " + posY + ", " + posZ + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + hposX + ", " + hposZ + ChatFormatting.RESET + "]", 2, new ScaledResolution(mc).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14 : FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2), rainbowArray.getValue() ? RainbowUtil.rainbow(1 * deelay.getValue()) : color.getRGB());
            }
        }

        if (Direction.getValue()) {
            if (xyzNoGray.getValue()) {
                String direction = getFacing() + " " + "[" + getTowards() + "]";
                final int[] counter5 = { 1 };
                final char[] stringToCharArray3 = direction.toCharArray();
                float u = 0.0f;
                for (final char c4 : stringToCharArray3) {
                    drawStringWithShadow(String.valueOf(c4), (int) (2 + u), new ScaledResolution(mc).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14 : FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2) - (XYZ.getValue() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2 : 0), rainbowArray.getValue() ? RainbowUtil.rainbow(counter5[0] * deelay.getValue()) : color.getRGB());
                    u += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c4));
                    ++counter5[0];
                }
            } else {
                drawStringWithShadow(ChatFormatting.GRAY + getFacing() + " " + ChatFormatting.RESET + "[" + ChatFormatting.GRAY + getTowards() + ChatFormatting.RESET + "]", 2, new ScaledResolution(mc).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 14 : FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2) - (XYZ.getValue() ? FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2 : 0), rainbowArray.getValue() ? RainbowUtil.rainbow(2 * deelay.getValue()) : color.getRGB());
            }
        }

        if (Watermark.getValue()) {
            String string = "XannaX " + Xannax.VERSION;
            final int[] counter1 = { 1 };
            final char[] stringToCharArray = string.toCharArray();
            float i = 0.0f;
            for (final char c : stringToCharArray) {
                drawStringWithShadow(String.valueOf(c), (int) (2 + i), WatermarkY.getValue(), rainbowArray.getValue() ? RainbowUtil.rainbow(counter1[0] * deelay.getValue()) : color.getRGB());
                i += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(c));
                ++counter1[0];
            }
            //drawStringWithShadow("XannaX " + Xannax.VERSION, 2, 2, rainbowArray.getValue() ? RainbowUtil.rainbow(1 * 100) : color.getRGB());
        }

        if (textRadar.getValue()) {
            int[] pee = {1};
            if (mc.world == null || mc.player == null) return;
            float offset = 0;
            final ArrayList<EntityPlayer> players = new ArrayList<>();
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player == mc.player || !player.isEntityAlive() || player.getEntityId() == -1488)
                    continue;
                players.add(player);
            }
            players.sort(Comparator.comparingDouble(player->mc.player.getDistance(player)));
            for (EntityPlayer player : players) {
                drawStringWithShadow("" + getHealthThing(player) + (int)(((EntityPlayer) player).getHealth() + ((EntityPlayer) player).getAbsorptionAmount()) + " " + (Friends.isFriend(player.getName()) ? ChatFormatting.AQUA : ChatFormatting.RESET) + player.getName() + " " + getDistanceThing(player) + (int) mc.player.getDistance(player), 2, textRadarY.getValue() + (int)offset  - ((players.size() / 2)), rainbowArray.getValue() ? RainbowUtil.rainbow(pee[0] * deelay.getValue()) : color.getRGB());
                offset += FontUtils.getFontHeight(ModuleManager.isModuleEnabled("CustomFont")) + 2;
                pee[0]++;
            }
        }


        if (Greeter.getValue()) {
            drawStringWithShadow(MathUtil.getTimeOfDay() + mc.player.getName(), GreeterX.getValue(), GreeterY.getValue(), rainbowArray.getValue() ? RainbowUtil.rainbow(1 * deelay.getValue()) : color.getRGB());
        }

        if (Hole.getValue()) {
            renderHole(holex.getValue(), holey.getValue());
        }

        if (tots.getValue()) {
            renderTotemHUD();
        }

        if (Ping.getValue()) {
            if (pSortUp.getValue()) {
                drawStringWithShadow("Ping " + ChatFormatting.GRAY + getPing() + "ms", new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + getPing() + "ms"), 0 + (Potions.getValue() ? + (potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("Ping " + ChatFormatting.GRAY + getPing() + "ms", new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Ping " + ChatFormatting.GRAY + getPing() + "ms"), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : (counter * -10)) - 10,  rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) :color.getRGB());
                potCount++;
                counter++;
            }
        }

        if (TPS.getValue()) {
            if (pSortUp.getValue()) {
                drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", (double) TpsUtils.getTickRate()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", (double) TpsUtils.getTickRate())), 0 + (Potions.getValue() ? + (potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("TPS " + ChatFormatting.GRAY + String.format("%.2f", (double) TpsUtils.getTickRate()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "TPS " + ChatFormatting.GRAY + String.format("%.2f", (double) TpsUtils.getTickRate())), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : (counter * -10)) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            }
        }

        if (Speed.getValue()) {
            final DecimalFormat df = new DecimalFormat("#.#");

            final double deltaX = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
            final double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
            final float tickRate = (Minecraft.getMinecraft().timer.tickLength / 1000.0f);

            final String BPSText = df.format((MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) / tickRate)*3.6);
            //RenderUtil.drawText("BPS: " + ChatFormatting.WHITE + BPSText, 2, y, getHudColor(), font);
            if (pSortUp.getValue()) {
                drawStringWithShadow("Speed " + ChatFormatting.GRAY + BPSText + "km/h", new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + BPSText + "km/h"), 0 + (Potions.getValue() ? + (potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("Speed " + ChatFormatting.GRAY + BPSText + "km/h", new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Speed " + ChatFormatting.GRAY + BPSText + "km/h"), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : (counter * -10)) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            }
        }

        if (durability.getValue() && isItemTool(mc.player.getHeldItemMainhand().getItem())) {
            if (pSortUp.getValue()) {
                drawStringWithShadow("Durability " + ChatFormatting.GRAY + (mc.player.getHeldItemMainhand().getMaxDamage() - mc.player.getHeldItemMainhand().getItemDamage()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Durability " + ChatFormatting.GRAY + (mc.player.getHeldItemMainhand().getMaxDamage() - mc.player.getHeldItemMainhand().getItemDamage())), 0 + (Potions.getValue() ? + (potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("Durability " + ChatFormatting.GRAY + (mc.player.getHeldItemMainhand().getMaxDamage() - mc.player.getHeldItemMainhand().getItemDamage()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Durability " + ChatFormatting.GRAY + (mc.player.getHeldItemMainhand().getMaxDamage() - mc.player.getHeldItemMainhand().getItemDamage())), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : (counter * -10)) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            }
        }

        if (Time.getValue()) {
            if (pSortUp.getValue()) {
                drawStringWithShadow("Time " + ChatFormatting.GRAY + (new SimpleDateFormat("h:mm a")).format(new Date()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + (new SimpleDateFormat("h:mm a")).format(new Date())), 0 + (Potions.getValue() ? + (potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("Time " + ChatFormatting.GRAY + (new SimpleDateFormat("h:mm a")).format(new Date()), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "Time " + ChatFormatting.GRAY + (new SimpleDateFormat("h:mm a")).format(new Date())), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : (counter * -10)) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            }
        }

        if (playerViewer.getValue()) {
            drawPlayer();
        }

        //if (test.getValue()) {
        //    drawStringWithShadow("test", new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "test"), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? + (potCount * -10) : 0) - 10, color.getRGB());
        //    potCount++;
        //}

        if (FPS.getValue()){
            if (pSortUp.getValue()) {
                drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), 0 + (Potions.getValue() ? +(potCount * 10) : (counter * 10)), rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            } else {
                drawStringWithShadow("FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), new ScaledResolution(mc).getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), "FPS " + ChatFormatting.GRAY + Minecraft.getDebugFPS()), new ScaledResolution(mc).getScaledHeight() + (Potions.getValue() ? +(potCount * -10) : (counter * -10)) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter * deelay.getValue()) : color.getRGB());
                potCount++;
                counter++;
            }
        }

        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};

        if (Potions.getValue()){

            potCount = 0;
            ScaledResolution resolution = new ScaledResolution(mc);
            try {
                mc.player.getActivePotionEffects().forEach(effect -> {
                    String name = I18n.format(effect.getPotion().getName());
                    double duration = effect.getDuration() / 19.99f;
                    int amplifier = effect.getAmplifier() + 1;
                    int color = effect.getPotion().getLiquidColor();
                    double p1 = duration % 60f;
                    //double p2 = duration / 60f;
                    //double p3 = p2 % 60f;
                    //String minutes = format1.format(p3);
                    String seconds = format2.format(p1);
                    String s = name + " " + amplifier + ChatFormatting.GRAY + " " +  (int) duration / 60 + ":" + seconds;
                    if (pSortUp.getValue()) {
                        drawStringWithShadow(s, resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), 0 + (potCount * 10), color);
                        potCount++;
                    } else {
                        drawStringWithShadow(s, resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), s), resolution.getScaledHeight() + (potCount * -10) - 10, color);
                        potCount++;
                    }
                });
            } catch(NullPointerException e){e.printStackTrace();}
        }

        if (ArrayList.getValue()) {

            ScaledResolution resolution = new ScaledResolution(mc);

            int[] counter = {1};

            if(sortUp.getValue()){ sort = -1;
            } else { sort = 1; }
            modCount = 0;
            ModuleManager.getModules()
                    .stream()
                    .filter(Module::isEnabled)
                    .filter(Module::isDrawn)
                    .sorted(Comparator.comparing(module -> FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * (-1)))
                    .forEach(m -> {
                        if(sortUp.getValue()) {
                            if(ArrayListHot.getValue()) {
                                RenderUtil.drawRect(resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + " " + m.getHudInfo()) -4, 0 + (modCount * 10) -1, getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4, 11, new Color (21, 21, 21, 100).getRGB());
                                RenderUtil.drawRect(resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + " " + m.getHudInfo()) -4, 0 + (modCount * 10) -1, 2, 11, rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : color.getRGB());
                            }
                            int x = resolution.getScaledWidth();
                            int lWidth = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo());
                            if (m.animPos < lWidth && m.isEnabled()) {
                                m.animPos = Animation.moveTowards(m.animPos, lWidth + 1, 0.1f, 0.1f);
                            } else if (m.animPos > 1.5f && !m.isEnabled()) {
                                m.animPos = Animation.moveTowards(m.animPos, -1.5f, 0.1f, 0.1f);
                            } else if (m.animPos <= 1.5f && !m.isEnabled()) {
                                m.animPos = -1f;
                            }
                            if (m.animPos > lWidth && m.isEnabled()) {
                                m.animPos = lWidth;
                            }
                            x -= m.animPos;
                            if (ArrayListMode.getValue().equalsIgnoreCase("Static")) {
                                drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x, 0 + (modCount * 10), rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : color.getRGB());
                            }
                            else {
                                drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x, 0 + (modCount * 10), rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), value).getRGB());
                            }
                            hue[0] +=.02f;
                            modCount++;
                            counter[0]++;
                        } else {
                            if(ArrayListHot.getValue()) {
                                RenderUtil.drawRect(resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"),m.getName() + " " + m.getHudInfo()) -4, resolution.getScaledHeight() + (modCount * -10) -1 - 10, getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4, 11, new Color (21, 21, 21, 100).getRGB());
                                RenderUtil.drawRect(resolution.getScaledWidth() - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"),m.getName() + " " + m.getHudInfo()) -4, resolution.getScaledHeight() + (modCount * -10) -1 - 10, 2, 11, rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : color.getRGB());
                            }
                            int x = resolution.getScaledWidth();
                            int lWidth = FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo());
                            if (m.animPos < lWidth && m.isEnabled()) {
                                m.animPos = Animation.moveTowards(m.animPos, lWidth + 1, 0.1f, 0.1f);
                            } else if (m.animPos > 1.5f && !m.isEnabled()) {
                                m.animPos = Animation.moveTowards(m.animPos, -1.5f, 0.1f, 0.1f);
                            } else if (m.animPos <= 1.5f && !m.isEnabled()) {
                                m.animPos = -1f;
                            }
                            if (m.animPos > lWidth && m.isEnabled()) {
                                m.animPos = lWidth;
                            }
                            x -= m.animPos;
                            if (ArrayListMode.getValue().equalsIgnoreCase("Static")) {
                                drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x, resolution.getScaledHeight() + (modCount * -10) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : color.getRGB());
                            }
                            else {
                                drawStringWithShadow(m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo(), x, resolution.getScaledHeight() + (modCount * -10) - 10, rainbowArray.getValue() ? RainbowUtil.rainbow(counter[0] * deelay.getValue()) : new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), value).getRGB());
                            }
                            hue[0] +=.02f;
                            modCount++;
                            counter[0]++;
                        }
                    });
        }

        if (ArmorHud.getValue()) {

            GlStateManager.enableTexture2D();

            ScaledResolution resolution = new ScaledResolution(mc);
            int i = resolution.getScaledWidth() / 2;
            int iteration = 0;
            int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
            for (ItemStack is : mc.player.inventory.armorInventory) {
                iteration++;
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();

                itemRender.zLevel = 200F;
                itemRender.renderItemAndEffectIntoGUI(is, x, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
                itemRender.zLevel = 0F;

                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();

                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffff);
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);
                drawStringWithShadow(dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, ColorHolder.toHex((int) (red * 255), (int) (green * 255), 0));//rainbowColor2.getRed(), rainbowColor2.getGreen(), rainbowColor2.getBlue()
            }

            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }

    }

    private void drawStringWithShadow (String text,int x, int y, int color) {
        if (ModuleManager.isModuleEnabled("CustomFont"))
            Xannax.fontRenderer.drawStringWithShadow(text, x, y, color);
        else
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    private int getWidth(String s){
        if(ModuleManager.isModuleEnabled("CustomFont")) return Xannax.fontRenderer.getStringWidth(s);
        else return mc.fontRenderer.getStringWidth(s);
    }

    private void renderHole(double holex, double holey){
        double leftX = holex;
        double leftY = holey + 16;
        double upX = holex + 16;
        double upY = holey;
        double rightX = holex + 32;
        double rightY = holey + 16;
        double bottomX = holex + 16;
        double bottomY = holey + 32;
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        switch (mc.getRenderViewEntity().getHorizontalFacing()) {
            case NORTH:
                if(northObby() || northBrock()) renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock()));
                if(westObby() || westBrock()) renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock()));
                if(eastObby() || eastBrock()) renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock()));
                if(southObby() || southBrock()) renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock()));
                break;

            case SOUTH:
                if(southObby() || southBrock()) renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock()));
                if(eastObby() || eastBrock()) renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock()));
                if(westObby() || westBrock()) renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock()));
                if(northObby() || northBrock()) renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock()));
                break;

            case WEST:
                if(westObby() || westBrock()) renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock()));
                if(southObby() || southBrock()) renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock()));
                if(northObby() || northBrock()) renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock()));
                if(eastObby() || eastBrock()) renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock()));
                break;

            case EAST:
                if(eastObby() || eastBrock()) renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock()));
                if(northObby() || northBrock()) renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock()));
                if(southObby() || southBrock()) renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock()));
                if(westObby() || westBrock()) renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock()));
                break;
        }
    }

    private void renderItem(double x, double y, ItemStack is){
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(is, (int)x, (int)y);
        RenderHelper.disableStandardItemLighting();
    }

    private boolean northObby(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.OBSIDIAN;
    }
    private boolean eastObby(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.OBSIDIAN;
    }
    private boolean southObby(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.OBSIDIAN;
    }
    private boolean westObby(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.OBSIDIAN;
    }

    private boolean northBrock(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.BEDROCK;
    }
    private boolean eastBrock(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.BEDROCK;
    }
    private boolean southBrock(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.BEDROCK;
    }
    private boolean westBrock(){
        Vec3d vec3d = BlockUtils.getInterpolatedPos(mc.player, 0);
        BlockPos playerPos = new BlockPos(vec3d);
        return mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.BEDROCK;
    }

    public int getPing() {
        int p;
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
            p = -1;
        } else {
            mc.player.getName();
            p = Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getName())).getResponseTime();
        }
        return p;
    }

    public void renderTotemHUD() {
        ScaledResolution resolution = new ScaledResolution(mc);
        final int width = resolution.getScaledWidth();
        final int height = resolution.getScaledHeight();
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(totemm, x, y);
            itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, totemm, x, y, "");
            itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            drawStringWithShadow(totems + "", (int)(x + 19 - 2 - FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), totems + "")), (int)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public void drawPlayer() {
        final EntityPlayer ent = (EntityPlayer)mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(this.playerViewerY.getValue() / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public static void sexyRainbow() {
        String watermark2 = "XannaX 0.1";
        int x1 = 0;

        for (int i = 0; i < watermark2.length(); i++) {
            char ch = watermark2.charAt(i);
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(ch), 2 + x1, 2, RenderUtil.generateRainbowFadingColor(watermark2.length() - 1 - i, true));
            x1 += FontUtils.getStringWidth(ModuleManager.isModuleEnabled("CustomFont"), String.valueOf(ch));
        }

    }

    public String getFacing()
    {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
                return "South";
            case 1:
                return "South";
            case 2:
                return "West";
            case 3:
                return "West";
            case 4:
                return "North";
            case 5:
                return "North";
            case 6:
                return "East";
            case 7:
                return "East";
        }
        return "Invalid";
    }

    public String getTowards()
    {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7)
        {
            case 0:
                return "+Z";
            case 1:
                return "+Z";
            case 2:
                return "-X";
            case 3:
                return "-X";
            case 4:
                return "-Z";
            case 5:
                return "-Z";
            case 6:
                return "+X";
            case 7:
                return "+X";
        }
        return "Invalid";
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0F, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0F, 1.0F, 0.8f) | 0xFF000000;
    }

    public ChatFormatting getHealthThing(EntityLivingBase player) {
        if (((player).getHealth() + (player).getAbsorptionAmount()) <= 5){
            return ChatFormatting.RED;
        }
        if (((player).getHealth() + (player).getAbsorptionAmount()) > 5 && ((player).getHealth() + (player).getAbsorptionAmount()) < 15){
            return ChatFormatting.YELLOW;
        }
        if (((player).getHealth() + (player).getAbsorptionAmount()) >= 15){
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }

    public ChatFormatting getDistanceThing(EntityLivingBase player) {
        if (mc.player.getDistance(player) < 20){
            return ChatFormatting.RED;
        }
        if (mc.player.getDistance(player) >= 20 && mc.player.getDistance(player) < 50){
            return ChatFormatting.YELLOW;
        }
        if (mc.player.getDistance(player) >= 50){
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }

    public boolean isItemTool(Item item) {
        return item instanceof ItemArmor || item == Items.DIAMOND_SWORD || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE || item == Items.IRON_SWORD || item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE || item == Items.GOLDEN_SWORD || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_AXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_HOE || item == Items.STONE_SWORD || item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL || item == Items.STONE_HOE || item == Items.WOODEN_SWORD || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE;
    }

    public void changeIfShould() {

    }

    private static class Timer {
        private long timeStartMs;

        public Timer() { timeStartMs = System.currentTimeMillis(); }

        public boolean hasTimePassed(long ms) { return System.currentTimeMillis() - ms > timeStartMs; }

        public void reset() { timeStartMs = System.currentTimeMillis(); }
    }

}
