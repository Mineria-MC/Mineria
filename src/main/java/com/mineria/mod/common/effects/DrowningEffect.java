package com.mineria.mod.common.effects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class DrowningEffect extends MobEffect
{
    public DrowningEffect()
    {
        super(MobEffectCategory.HARMFUL, 37);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier)
    {
        if(living.getAirSupply() < 0)
        {
            Vec3 delta = living.getDeltaMovement();

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
