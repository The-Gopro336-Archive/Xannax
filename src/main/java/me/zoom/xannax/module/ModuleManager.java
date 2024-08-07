package me.zoom.xannax.module;

import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.module.modules.combat.*;
import me.zoom.xannax.module.modules.render.*;
import me.zoom.xannax.module.modules.client.*;
import me.zoom.xannax.module.modules.movement.*;
import me.zoom.xannax.module.modules.misc.*;
import me.zoom.xannax.module.modules.player.*;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

    public static ArrayList<Module> modules;

    public ModuleManager(){
        modules = new ArrayList<>();
        //Combat
        addMod(new Aura());
        addMod(new AutoCrystal());
        addMod(new AutoTrap());
        addMod(new AutoTrapX());
        addMod(new AutoSchoolShooter());
        addMod(new AutoMend());
        addMod(new AutoArmor());
        addMod(new AutoWeed());
        addMod(new AutoTotem());
        addMod(new zopacoffhand());
        addMod(new BedAura());
        addMod(new EXPFast());
        addMod(new Criticals());
        addMod(new EasyPearl());
        addMod(new HoleFill());
        addMod(new SelfTrap());
        addMod(new SelfWeb());
        addMod(new Surround());
        addMod(new OffhandUtils());
        //Movement
        addMod(new Blink());
        addMod(new ElytraFly());
        addMod(new FastSwim());
        addMod(new Speed());
        addMod(new Sprint());
        addMod(new Step());
        addMod(new Scaffold());
        addMod(new ReverseStep());
        addMod(new IceSpeed());
        addMod(new NoSlow());
        addMod(new GuiMove());
        addMod(new Static());
        addMod(new Velocity());
        //Player
        addMod(new AutoReplanish());
        addMod(new BuildHeight());
        addMod(new OffhandSwing());
        addMod(new NoEntityTrace());
        addMod(new MultiTask());
        addMod(new Reach());
        addMod(new SpeedMine());
        //Misc
        addMod(new Announcer());
        addMod(new BreakAlert());
        addMod(new Chat());
        addMod(new ChatSuffix());
        addMod(new RPCModule());
        addMod(new FakePlayer());
        addMod(new LowArmor());
        addMod(new PearlAlert());
        addMod(new TotemCounter());
        addMod(new VisualRange());
        addMod(new MiddleClick());
        addMod(new NoWeather());
        addMod(new XCarry());
        //Client
        addMod(new ClickGuiModule());
        addMod(new CustomFont());
        addMod(new HUD());
        //addMod(new Rainbow());
        addMod(new Compass());
        //Render
        addMod(new BreakESP());
        addMod(new CameraClip());
        addMod(new Chams());
        addMod(new CapesModule());
        addMod(new ESP());
        addMod(new EnchantColor());
        addMod(new ItemESP());
        addMod(new Skeleton());
        addMod(new StorageESP());
        addMod(new SkyColor());
        addMod(new ShulkerPreview());
        addMod(new FullBright());
        addMod(new FovSlider());
        addMod(new Nametags());
        addMod(new NoRender());
        addMod(new HandColor());
        addMod(new ViewModel());
        addMod(new BlockHighlight());
        addMod(new VoidESP());
        addMod(new InvPreview());
        addMod(new LowHands());
        addMod(new LogoutSpots());
        addMod(new HoleESP());
        addMod(new Trajectories());
        addMod(new ToolTips());
        addMod(new Tracers());
    }

    public static void addMod(Module m){
        modules.add(m);
    }

    public static void onUpdate() {
        modules.stream().filter(Module::isToggled).forEach(Module::onUpdate);
    }

    public static void onRender() {
        modules.stream().filter(Module::isToggled).forEach(Module::onRender);
    }


    public static void onWorldRender(RenderWorldLastEvent event) {

        Minecraft.getMinecraft().profiler.startSection("xannax");
        Minecraft.getMinecraft().profiler.startSection("setup");

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1f);

        Vec3d renderPos = getInterpolatedPos(Minecraft.getMinecraft().player, event.getPartialTicks());

        RenderEvent e = new RenderEvent(RenderUtil.INSTANCE, renderPos, event.getPartialTicks());
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.onWorldRender(e);
            Minecraft.getMinecraft().profiler.endSection();
        });

        Minecraft.getMinecraft().profiler.startSection("release");

        GlStateManager.glLineWidth(1f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        RenderUtil.releaseGL();

        Minecraft.getMinecraft().profiler.endSection();
        Minecraft.getMinecraft().profiler.endSection();
    }

    public static ArrayList<Module> getModules() {
        return modules;
    }

    public static ArrayList<Module> getModulesInCategory(Module.Category c){
        ArrayList<Module> list = (ArrayList<Module>) getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
        return list;
    }

    public static void onBind(int key) {
        if (key == 0 || key == Keyboard.KEY_NONE) return;
        modules.forEach(module -> {
            if(module.getBind() == key){
                module.toggle();
            }
        });
    }

    public static Module getModuleByName(String name){
        Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return m;
    }

    public static boolean isModuleEnabled(String name){
        Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return m.isEnabled();
    }

    public static boolean isModuleEnabled(Module m){
        return m.isEnabled();
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }
}
