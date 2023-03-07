package io.github.mineria_mc.mineria.common.effects;

import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.sounds.SoundSource;

public class BowelSoundsEffect extends MobEffect {
    public BowelSoundsEffect() {
        super(MobEffectCategory.NEUTRAL, 9139767);
    }

    @Deprecated
    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        this.applyEffectTick(entityLivingBaseIn, amplifier, 2.0F);
    }

    public void applyEffectTick(LivingEntity living, int amplifier, float volume) {
        living.getCommandSenderWorld().playSound(living instanceof Player ? (Player) living : null, living.getX(), living.getY(), living.getZ(), MineriaSounds.BOWEL_SOUNDS.get(), SoundSource.NEUTRAL, volume, 1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 200 == 0;
    }
}
