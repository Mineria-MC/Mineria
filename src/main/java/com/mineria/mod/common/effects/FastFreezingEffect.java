package com.mineria.mod.common.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.damagesource.DamageSource;

public class FastFreezingEffect extends MobEffect
{
    private static final String FREEZING_SLOWDOWN_UUID = "0d110441-e43e-4848-b954-9cf6883f1e24";

    public FastFreezingEffect()
    {
        super(MobEffectCategory.HARMFUL, 9226482);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FREEZING_SLOWDOWN_UUID, -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier)
    {
        living.hurt(DamageSource.MAGIC, 1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return duration % 30 == 0;
    }
}
