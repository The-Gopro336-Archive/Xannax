package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.event.events.PlayerJumpEvent;
import me.zoom.xannax.event.events.WaterPushEvent;
import me.zoom.xannax.Xannax;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {

    @Shadow public abstract String getName();

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo ci){
        if (Minecraft.getMinecraft().player.getName() == this.getName()){
            Xannax.EVENT_BUS.post(new PlayerJumpEvent());
        }
    }

    @Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
    private void onPushedByWater(CallbackInfoReturnable<Boolean> cir){
        WaterPushEvent event = new WaterPushEvent();
        Xannax.EVENT_BUS.post(event);
        if (event.isCancelled()){
            cir.setReturnValue(false);
        }
    }
}
