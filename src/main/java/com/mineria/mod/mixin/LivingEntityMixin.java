package com.mineria.mod.mixin;

import com.mineria.mod.effects.PoisonEffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    @Inject(method = "onFinishedPotionEffect", at = @At("TAIL"))
    public void onFinishedPotionEffect(EffectInstance effect, CallbackInfo ci)
    {
        if (effect instanceof PoisonEffectInstance)
        {
            PoisonEffectInstance poisonEffect = (PoisonEffectInstance) effect;
            int potionClass = poisonEffect.getPotionClass();
            ((LivingEntity) (Object) this).removePotionEffect(Effects.NAUSEA);
            if (potionClass > 0) ((LivingEntity) (Object) this).removePotionEffect(Effects.SLOWNESS);
        }
    }
}
