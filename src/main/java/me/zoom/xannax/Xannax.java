package me.zoom.xannax;

import me.zoom.xannax.event.EventProcessor;
import me.zoom.xannax.macro.MacroManager;
import me.zoom.xannax.util.CapeUtils;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.TpsUtils;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.zoom.xannax.command.CommandManager;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.util.font.CFontRenderer;
import me.zoom.xannax.clickgui.ClickGUI;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import org.apache.logging.log4j.Logger;

import java.awt.*;

@Mod(modid = Xannax.MOD_ID, name = Xannax.MOD_NAME, version = Xannax.VERSION)
public class Xannax {
    public static final String MOD_ID = "xannax";
    public static final String MOD_NAME = "XannaX";
    public static final String VERSION = "0.9.1";
    public static Minecraft mc;
    public static final Logger log = LogManager.getLogger(MOD_NAME);

    public MacroManager macroManager;
    public CapeUtils capeUtils;
    public ClickGUI clickGUI;
    public Friends friends;
    public ModuleManager moduleManager;
    EventProcessor eventProcessor;
    public static CFontRenderer fontRenderer;
    public static Enemies enemies;

    public static final EventBus EVENT_BUS = new EventManager();

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance
    private static Xannax INSTANCE;

    public Xannax(){
        INSTANCE = this;
    }

    /**
     * This is the first initialization event.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    }

    /**
     * This is the second initialization event. Initialize your managers here.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        mc = Minecraft.getMinecraft();
        eventProcessor = new EventProcessor();
        eventProcessor.init();

        fontRenderer = new CFontRenderer(new Font("Ariel", Font.PLAIN, 18), true, false);

        TpsUtils tpsUtils = new TpsUtils();

        friends = new Friends();
        enemies = new Enemies();
        log.info("Friends and enemies initialized!");

        moduleManager = new ModuleManager();
        log.info("Modules initialized!");

        clickGUI = new ClickGUI();
        log.info("ClickGUI initialized!");

        macroManager = new MacroManager();
        log.info("Macros initialized!");

        CommandManager.initCommands();
        log.info("Commands initialized!");

        log.info("Initialization complete!\n");
    }

    /**
     * This is the final initialization event.
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        Display.setTitle(MOD_NAME + " " + VERSION);

        capeUtils = new CapeUtils();
        log.info("Capes initialised!");

        log.info("PostInitialization complete!\n");
    }

    public static Xannax getInstance(){
        return INSTANCE;
    }
}
