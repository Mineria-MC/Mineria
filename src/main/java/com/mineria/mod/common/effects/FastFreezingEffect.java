package com.mineria.mod.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class FastFreezingEffect extends Effect
{
    private static final String FREEZING_SLOWDOWN_UUID = "0d110441-e43e-4848-b954-9cf6883f1e24";

    public FastFreezingEffect()
    {
        super(EffectType.HARMFUL, 9226482);
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
