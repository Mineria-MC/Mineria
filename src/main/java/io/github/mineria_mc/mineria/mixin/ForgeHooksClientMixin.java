package io.github.mineria_mc.mineria.mixin;

import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    @Inject(method = "shouldRenderEffect", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mineria$shouldRender(MobEffectInstance instance, CallbackInfoReturnable<Boolean> cir) {
        if (instance instanceof ModdedMobEffectInstance modded) {
            cir.setReturnValue(modded.shouldRender());
        }
    }
}
