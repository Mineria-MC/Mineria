package io.github.mineria_mc.mineria.common.effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class DrowningEffect extends MobEffect {
    public DrowningEffect() {
        super(MobEffectCategory.HARMFUL, 37);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "25c4299b-d86d-4bcb-8d04-02271f7d1424", -1f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.getAirSupply() < 0) {
            Vec3 delta = living.getDeltaMovement();

            for (int i = 0; i < 8; ++i) {
                RandomSource rand = living.getRandom();
                double dx = rand.nextDouble() - rand.nextDouble();
                double dy = rand.nextDouble() - rand.nextDouble();
                double dz = rand.nextDouble() - rand.nextDouble();
                living.level().addParticle(ParticleTypes.BUBBLE, living.getX() + dx, living.getY() + dy, living.getZ() + dz, delta.x, delta.y, delta.z);
            }

            living.hurt(living.damageSources().drown(), 2);
        } else {
            living.removeEffect(this);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 15 == 0;
    }
}
