package me.zoom.xannax.module.modules.combat;

import me.zoom.xannax.module.Module;
import net.minecraft.init.Items;


public class EXPFast extends Module {

    public EXPFast() {
        super("EXPFast", "EXPFast", Category.Combat);
    }

    public void onUpdate() {
        if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            mc.rightClickDelayTimer = 0;
        }
    }
}
