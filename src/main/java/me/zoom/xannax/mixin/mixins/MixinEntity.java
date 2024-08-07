package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.movement.Velocity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class MixinEntity {

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void velocity(Entity entity, double x, double y, double z) {
        if (((Velocity)ModuleManager.getModuleByName("Velocity")).noPush.getValue() == false) {
            entity.motionX += x;
            entity.motionY += y;
            entity.motionZ += z;
            entity.isAirBorne = true;
        }
    }
}
