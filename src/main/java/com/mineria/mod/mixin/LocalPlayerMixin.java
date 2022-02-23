package com.mineria.mod.mixin;

import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntity
{
    protected LocalPlayerMixin(EntityType<? extends LivingEntity> type, Level worldIn)
    {
        super(type, worldIn);
    }

    @Inject(method = "isShiftKeyDown", at = @At("HEAD"), cancellable = true)
    public void mixIsShiftKeyDown(CallbackInfoReturnable<Boolean> cir)
    {
        if(this.hasEffect(MobEffects.POISON) && this.getEffect(MobEffects.POISON) instanceof PoisonEffectInstance poison)
        {
            if(poison.doSpasms())
            {
                cir.setReturnValue(false);
            }
        }
    }

    @ModifyConstant(method = "handleNetherPortalClient", constant = @Constant(floatValue = 1.0F, ordinal = 2))
    private float modifyMaxTime(float constant)
    {
        return calculateMaxPortalTime();
    }

    @ModifyConstant(method = "handleNetherPortalClient", constant = @Constant(floatValue = 1.0F, ordinal = 3))
    private float modifyMaxTime2(float constant)
    {
        return calculateMaxPortalTime();
    }

    private float calculateMaxPortalTime()
    {
        return 0.4F * (this.getEffect(MobEffects.CONFUSION).getAmplifier() + 1);
    }
}
