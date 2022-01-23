package com.mineria.mod.common.effects;

import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;

public class BowelSoundsEffect extends Effect
{
    public BowelSoundsEffect()
    {
        super(EffectType.NEUTRAL, 9139767);
    }

    @Deprecated
    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier)
    {
        this.applyEffectTick(entityLivingBaseIn, amplifier, 2.0F);
    }

    public void applyEffectTick(LivingEntity living, int amplifier, float volume)
    {
        living.getCommandSenderWorld().playSound(living instanceof PlayerEntity ? (PlayerEntity) living : null, living.getX(), living.getY(), living.getZ(), MineriaSounds.BOWEL_SOUNDS.get(), SoundCategory.NEUTRAL, volume, 1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return duration % 200 == 0;
    }
}
