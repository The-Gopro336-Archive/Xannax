package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.event.events.DestroyBlockEvent;
import me.zoom.xannax.event.events.PacketEvent;
import me.zoom.xannax.event.events.PlayerJumpEvent;
import me.zoom.xannax.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Announcer extends Module {

    public Announcer(){
        super("Announcer", "Be annoying!",Category.Misc);
    }

    public static String walkMessage;
    public static String placeMessage;
    public static String jumpMessage;
    public static String breakMessage;
    public static String attackMessage;
    public static String eatMessage;
    public static String guiMessage;

    public static int blockBrokeDelay = 0;
    static int blockPlacedDelay = 0;
    static int jumpDelay = 0;
    static int attackDelay = 0;
    static int eattingDelay = 0;

    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    String heldItem = "";

    int blocksPlaced = 0;
    int blocksBroken = 0;
    int eaten = 0;

    public Setting.Boolean clientSide;
    Setting.Mode language;
    Setting.Boolean walk;
    Setting.Boolean place;
    Setting.Boolean jump;
    Setting.Boolean breaking;
    Setting.Boolean attack;
    Setting.Boolean eat;
    public Setting.Boolean clickGui;
    Setting.Integer delay;

    public void setup(){
        ArrayList<String> modes = new ArrayList<>();
        modes.add("English");
        modes.add("Hebrew");
        modes.add("German");
        modes.add("Spanish");
        modes.add("Swag");
        modes.add("Dutch");
        modes.add("Portuguese");
        language = registerMode("Language", "Language", modes, "English");
        clientSide = registerBoolean("Client Side", "ClientSide", false);
        walk = registerBoolean("Walk", "Walk", true);
        place = registerBoolean("Place", "Place", true);
        jump = registerBoolean("Jump", "Jump", true);
        breaking = registerBoolean("Breaking", "Breaking", true);
        attack = registerBoolean("Attack", "Attack",true);
        eat = registerBoolean("Eat", "Eat",true);
        clickGui = registerBoolean("GUI", "GUI",true);
        delay = registerInteger("Delay", "Delay",1,1,20);
    }

    public void onUpdate() {

        blockBrokeDelay++;
        blockPlacedDelay++;
        jumpDelay++;
        attackDelay++;
        eattingDelay++;
        heldItem = mc.player.getHeldItemMainhand().getDisplayName();

        if(language.getValue().equalsIgnoreCase("English")) {
            walkMessage = "I just walked {blocks} meters thanks to XannaX!";
            placeMessage= "I just placed {amount} {name} thanks to XannaX!";
            jumpMessage= "I just jumped thanks to XannaX!";
            breakMessage= "I just mined {amount} {name} thanks to XannaX!";
            attackMessage= "I just attacked {name} with a {item} thanks to XannaX!";
            eatMessage= "I just ate {amount} {name} thanks to XannaX!";
            guiMessage= "I just opened my advanced GUI thanks to XannaX!";
        }
        if(language.getValue().equalsIgnoreCase("Hebrew")) {
            walkMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05dc\u05db\u05ea\u05d9 {blocks} \u05de\u05d8\u05e8\u05d9\u05dd \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            placeMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05de\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            jumpMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e7\u05e4\u05e6\u05ea\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            breakMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e9\u05d1\u05e8\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            attackMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d4\u05db\u05d9\u05ea\u05d9 \u05d0\u05ea {name} \u05e2\u05dd {item} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            eatMessage = "\u05d4\u05e8\u05d2\u05e2 \u05d0\u05db\u05dc\u05ea\u05d9 {amount} {name} \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
            guiMessage = "\u05d4\u05e8\u05d2\u05e2 \u05e4\u05ea\u05d7\u05ea\u05d9 \u05d0\u05ea \u05d4 \u0047\u0055\u0049 \u05d4\u05de\u05ea\u05e7\u05d3\u05dd \u05e9\u05dc\u05d9 \u05ea\u05d5\u05d3\u05d5\u05ea \u05dc\u0058\u0061\u006e\u006e\u0061\u0058\u0021";
        }
        if(language.getValue().equalsIgnoreCase("German")) {
            walkMessage = "Ich bin grade {blocks} meter gelaufen dank XannaX!";
            placeMessage = "Ich habe grade {amount}{name} plaziert dank XannaX!";
            jumpMessage = "Ich bin grade gesprungen dank XannaX!";
            breakMessage = "Ich habe grade {amount}{name} gemined dank XannaX!";
            attackMessage = "Ich habe grade {name} mit einem {item} attackiert dank XannaX!";
            eatMessage = "Ich habe grade {amount} {name} gegessen dank XannaX!";
            guiMessage = "Ich habe grade mein erweitertes GUI ge\u00f6ffnet dank XannaX!";
        }
        if(language.getValue().equalsIgnoreCase("Spanish")) {
            walkMessage = "Acabo de andar {blocks} metros gracias a XannaX!";
            placeMessage = "Acabo de colocar {amount} {name} gracias a XannaX!";
            jumpMessage = "Acabo de saltar gracias a XannaX!";
            breakMessage = "Acabo de minar {amount} {name} gracias a XannaX!";
            attackMessage = "Acabo de atacar a {name} con {item} gracias a XannaX!";
            eatMessage = "Acabo de comer {amount} {name} gracias a XannaX!";
            guiMessage = "Acabo de abrir mi GUI avanzado gracias a XannaX!";
        }
        if(language.getValue().equalsIgnoreCase("Swag")) {
            walkMessage = "a young nigga jus stepped {blocks} meters because of muffukin XannaX!";
            placeMessage = "a young nigga jus placed {amount} {name} because of muffukin XannaX!";
            jumpMessage = "a young nigga jus jumped because of muffukin XannaX!";
            breakMessage = "a young nigga jus mined {amount} {name} because of muffukin XannaX!";
            attackMessage = "a young nigga jus jumped {name} with a muffukin {item} because of muffukin XannaX!";
            eatMessage = "a young nigga jus smoked {amount} weed because of muffukin XannaX!";
            guiMessage = "a young nigga just open da muffukin advanced GUI because of muffukin XannaX!";
        }
        if(language.getValue().equalsIgnoreCase("Dutch")) {
            walkMessage = "Ik heb net {blocks} gelopen door XannaX!";
            placeMessage = "Ik heb net {amount} {name} geplaatst door XannaX!";
            jumpMessage = "Ik sprong door XannaX!";
            breakMessage = "Ik heb {amount} {name} gebroken door XannaX!";
            attackMessage = "Ik heb zojuist {naam} aangevallen met een {item} dankzij XannaX!";
            eatMessage = "Ik heb net {amount} {name} gegeten door XannaX!";
            guiMessage = "Ik heb mijn hackerman menu geopend door XannaX!";
        }
        if(language.getValue().equalsIgnoreCase("Portuguese")) {
            walkMessage = "Eu acabei de andar {blocks} gracas a XannaX!";
            placeMessage = "Eu acabei de colocar {amount} {name} gracas a XannaX!";
            jumpMessage = "Eu acabei de pular gracas a XannaX!";
            breakMessage = "Eu acabei de minerar {amount} {name} gracas a XannaX!";
            attackMessage = "Eu acabei de atacar {name} com {item} gracas a XannaX!";
            eatMessage = "Eu acabei de comer {amount} {name} gracas a XannaX!";
            guiMessage = "Eu acabei de abrir a minha avançada GUI gracas a XannaX!";
        }

        if (walk.getValue()){
            if (lastPositionUpdate + (5000L * delay.getValue()) < System.currentTimeMillis()){

                double d0 = lastPositionX - mc.player.lastTickPosX;
                double d2 = lastPositionY - mc.player.lastTickPosY;
                double d3 = lastPositionZ - mc.player.lastTickPosZ;


                speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);

                if (!(speed <= 1) && !(speed > 5000)){
                    String walkAmount = new DecimalFormat("0.00").format(speed);

                    Random random = new Random();
                    if (clientSide.getValue()){
                        Command.sendClientMessage(walkMessage.replace("{blocks}", walkAmount));
                    } else{
                        mc.player.sendChatMessage(walkMessage.replace("{blocks}", walkAmount));
                    }
                    lastPositionUpdate = System.currentTimeMillis();
                    lastPositionX = mc.player.lastTickPosX;
                    lastPositionY = mc.player.lastTickPosY;
                    lastPositionZ = mc.player.lastTickPosZ;
                }
            }
        }

    }

    @EventHandler
    private final Listener<LivingEntityUseItemEvent.Finish> eatListener = new Listener<>(event -> {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (event.getEntity() == mc.player){
            if (event.getItem().getItem() instanceof ItemFood || event.getItem().getItem() instanceof ItemAppleGold){
                eaten++;
                if (eattingDelay >= 300 * delay.getValue()){
                    if (eat.getValue() && eaten > randomNum){
                        Random random = new Random();
                        if (clientSide.getValue()){
                            Command.sendClientMessage
                                    (eatMessage.replace("{amount}", eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
                        } else{
                            mc.player.sendChatMessage
                                    (eatMessage.replace("{amount}", eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
                        }
                        eaten = 0;
                        eattingDelay = 0;
                    }
                }
            }
        }
    });

    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock){
            blocksPlaced++;
            int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
            if (blockPlacedDelay >= 150 * delay.getValue()){
                if (place.getValue() && blocksPlaced > randomNum){
                    Random random = new Random();
                    String msg = placeMessage.replace("{amount}", blocksPlaced + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName());
                    if (clientSide.getValue()){
                        Command.sendClientMessage(msg);
                    } else{
                        mc.player.sendChatMessage(msg);
                    }
                    blocksPlaced = 0;
                    blockPlacedDelay = 0;
                }
            }
        }
    });

    @EventHandler
    private final Listener<DestroyBlockEvent> destroyListener = new Listener<>(event -> {
        blocksBroken++;
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        if (blockBrokeDelay >= 300 * delay.getValue()){
            if (breaking.getValue() && blocksBroken > randomNum){
                Random random = new Random();
                String msg = breakMessage
                        .replace("{amount}", blocksBroken + "")
                        .replace("{name}", mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
                if (clientSide.getValue()){
                    Command.sendClientMessage(msg);
                } else{
                    mc.player.sendChatMessage(msg);
                }
                blocksBroken = 0;
                blockBrokeDelay = 0;
            }
        }
    });

    @EventHandler
    private final Listener<AttackEntityEvent> attackListener = new Listener<>(event -> {
        if (attack.getValue() && !(event.getTarget() instanceof EntityEnderCrystal)){
            if (attackDelay >= 300 * delay.getValue()){
                String msg = attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", mc.player.getHeldItemMainhand().getDisplayName());
                if (clientSide.getValue()){
                    Command.sendClientMessage(msg);
                } else{
                    mc.player.sendChatMessage(msg);
                }
                attackDelay = 0;
            }
        }
    });

    @EventHandler
    private final Listener<PlayerJumpEvent> jumpListener = new Listener<>(event -> {
        if (jump.getValue()){
            if (jumpDelay >= 300 * delay.getValue()){
                if (clientSide.getValue()){
                    Random random = new Random();
                    Command.sendClientMessage(jumpMessage);
                } else{
                    Random random = new Random();
                    mc.player.sendChatMessage(jumpMessage);
                }
                jumpDelay = 0;
            }
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
        blocksPlaced = 0;
        blocksBroken = 0;
        eaten = 0;
        speed = 0;
        blockBrokeDelay = 0;
        blockPlacedDelay = 0;
        jumpDelay = 0;
        attackDelay = 0;
        eattingDelay = 0;
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
