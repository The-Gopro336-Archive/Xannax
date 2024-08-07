package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.event.events.DamageBlockEvent;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.DestroyBlockEvent;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.player.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true)
    private void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info){
        Xannax.EVENT_BUS.post(new DestroyBlockEvent(pos));
    }

    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
    private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        Xannax.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "resetBlockRemoving", at = @At("HEAD"), cancellable = true)
    private void resetBlock(CallbackInfo ci){
        if(ModuleManager.isModuleEnabled("MultiTask")) ci.cancel();
    }

    @Inject(method = {"getBlockReachDistance"}, at = {@At("RETURN")}, cancellable = true)
    private void getReachDistanceHook(CallbackInfoReturnable<Float> distance) {
        if (ModuleManager.isModuleEnabled("Reach")) {
            float range = (distance.getReturnValue()).floatValue();
            distance.setReturnValue(Reach.override.getValue() ? (float) Reach.reach.getValue() : Float.valueOf(range + (float) Reach.add.getValue()));
        }
    }
}
