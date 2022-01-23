package com.mineria.mod.mixin;

import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity
{
    protected ClientPlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    @Inject(method = "isShiftKeyDown", at = @At("HEAD"), cancellable = true)
    public void mixIsShiftKeyDown(CallbackInfoReturnable<Boolean> cir)
    {
        if(this.hasEffect(Effects.POISON) && this.getEffect(Effects.POISON) instanceof PoisonEffectInstance)
        {
            PoisonEffectInstance poison = (PoisonEffectInstance) this.getEffect(Effects.POISON);
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
        return 0.4F * (this.getEffect(Effects.CONFUSION).getAmplifier() + 1);
    }
}
