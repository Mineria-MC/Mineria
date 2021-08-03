package com.mineria.mod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;

public class BowelSoundsEffect extends Effect
{
    public BowelSoundsEffect()
    {
        super(EffectType.NEUTRAL, 0);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier)
    {
        entityLivingBaseIn.playSound(SoundEvents.ENTITY_PLAYER_BURP, 2.0F, 1.0F);
    }

    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration % 200 == 0;
    }
}
