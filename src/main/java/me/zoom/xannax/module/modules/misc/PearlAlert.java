package me.zoom.xannax.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class PearlAlert extends Module {
    public PearlAlert() {
        super("PearlAlert", "PearlAlert", Category.Misc);
    }

    ConcurrentHashMap uuidMap;

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public String getTitle(String var1) {
        if (var1.equalsIgnoreCase("west")) {
            return "east";
        } else {
            return var1.equalsIgnoreCase("east") ? "west" : var1;
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onUpdate() {
        Iterator var1 = mc.world.loadedEntityList.iterator();

        label45:
        while(true) {
            Entity var2;
            do {
                if (!var1.hasNext()) {
                    this.uuidMap.forEach((var1x, var2x) -> {
                        if ((int)var2x <= 0) {
                            this.uuidMap.remove(var1x);
                        } else {
                            this.uuidMap.put(var1x, (int)var2x - 1);
                        }

                    });
                    return;
                }

                var2 = (Entity)var1.next();
            } while(!(var2 instanceof EntityEnderPearl));

            EntityPlayer var3 = null;
            Iterator var4 = mc.world.playerEntities.iterator();

            while(true) {
                EntityPlayer var5;
                do {
                    if (!var4.hasNext()) {
                        if (var3 != null && var3.getDistance(var2) < 2.0F && !this.uuidMap.containsKey(var2.getUniqueID()) && !var3.getName().equalsIgnoreCase(mc.player.getName())) {
                            this.uuidMap.put(var2.getUniqueID(), 200);
                                Command.sendClientMessage(String.valueOf((new StringBuilder()).append(ChatFormatting.RED).append(var3.getName()).append(" threw a pearl towards ").append(this.getTitle(var2.getHorizontalFacing().getName())).append("!")));
                        }
                        continue label45;
                    }

                    var5 = (EntityPlayer)var4.next();
                } while(var3 != null && !(var2.getDistance(var5) < var2.getDistance(var3)));

                var3 = var5;
            }
        }
    }

    public void setup() {
        this.uuidMap = new ConcurrentHashMap();
    }
}
