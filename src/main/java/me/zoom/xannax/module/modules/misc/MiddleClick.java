package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MiddleClick extends Module {
    public MiddleClick() {
        super("MiddleClick", "Ez add friends", Category.Misc);
    }

    @EventHandler
    private final Listener<InputEvent.MouseInputEvent> listener = new Listener<>(event -> {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof EntityPlayer && Mouse.getEventButton() == 2) {
            if (Friends.isFriend(mc.objectMouseOver.entityHit.getName())) {
                Xannax.getInstance().friends.delFriend(mc.objectMouseOver.entityHit.getName());
                Command.sendClientMessage(ChatFormatting.RED + "Removed " + mc.objectMouseOver.entityHit.getName() + " from friends list");
            } else {
                Xannax.getInstance().friends.addFriend(mc.objectMouseOver.entityHit.getName());
                Command.sendClientMessage(ChatFormatting.GREEN + "Added " + mc.objectMouseOver.entityHit.getName() + " to friends list");
            }
        }
    });

    public void onEnable(){
        Xannax.EVENT_BUS.subscribe(this);
    }

    public void onDisable(){
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
