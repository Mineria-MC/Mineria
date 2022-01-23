package com.mineria.mod.mixin;

import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin
{
    @Inject(method = "setShiftKeyDown", at = @At("HEAD"))
    public void setShiftKeyDown(boolean keyDownIn, CallbackInfo info)
    {
        if((Entity) (Object) this instanceof LivingEntity)
        {
            LivingEntity living = (LivingEntity) ((Entity) (Object) this);
            if(living.hasEffect(Effects.POISON) && living.getEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                PoisonEffectInstance poison = (PoisonEffectInstance) living.getEffect(Effects.POISON);
                if(poison.doSpasms() && keyDownIn)
                {
                    keyDownIn = false;
                }
            }
        }
    }
}
