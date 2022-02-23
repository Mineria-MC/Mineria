package com.mineria.mod.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin
{
    @Inject(method = "calculatePitch", at = @At("HEAD"), cancellable = true)
    private void calculatePitch(SoundInstance sound, CallbackInfoReturnable<Float> cir)
    {
        cir.setReturnValue(Mth.clamp(sound.getPitch(), 0.01F, 50));
    }
}
