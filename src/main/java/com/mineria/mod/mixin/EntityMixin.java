package com.mineria.mod.mixin;

import com.mineria.mod.effects.PoisonEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin
{
    @Inject(method = "setSneaking", at = @At("HEAD"), cancellable = true)
    public void setSneaking(boolean keyDownIn, CallbackInfo info)
    {
        if((Entity) (Object) this instanceof LivingEntity)
        {
            LivingEntity living = (LivingEntity) ((Entity) (Object) this);
            if(living.isPotionActive(Effects.POISON) && living.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                PoisonEffectInstance poison = (PoisonEffectInstance) living.getActivePotionEffect(Effects.POISON);
                if(poison.doSpasms() && keyDownIn)
                {
                    keyDownIn = false;
                }
            }
        }
    }
}
