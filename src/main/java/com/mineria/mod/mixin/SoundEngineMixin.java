package com.mineria.mod.mixin;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin
{
    @Inject(method = "calculatePitch", at = @At("HEAD"), cancellable = true)
    private void calculatePitch(ISound sound, CallbackInfoReturnable<Float> cir)
    {
        cir.setReturnValue(MathHelper.clamp(sound.getPitch(), 0.01F, 50));
    }
}
