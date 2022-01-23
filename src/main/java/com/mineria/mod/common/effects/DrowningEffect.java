package com.mineria.mod.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

public class DrowningEffect extends Effect
{
    public DrowningEffect()
    {
        super(EffectType.HARMFUL, 37);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier)
    {
        if(living.getAirSupply() < 0)
        {
            Vector3d delta = living.getDeltaMovement();

            for (int i = 0; i < 8; ++i)
            {
                Random rand = living.getRandom();
                double dx = rand.nextDouble() - rand.nextDouble();
                double dy = rand.nextDouble() - rand.nextDouble();
                double dz = rand.nextDouble() - rand.nextDouble();
                living.level.addParticle(ParticleTypes.BUBBLE, living.getX() + dx, living.getY() + dy, living.getZ() + dz, delta.x, delta.y, delta.z);
            }

            living.hurt(DamageSource.DROWN, 2);
        } else
            living.removeEffect(this);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return duration % 15 == 0;
    }
}
