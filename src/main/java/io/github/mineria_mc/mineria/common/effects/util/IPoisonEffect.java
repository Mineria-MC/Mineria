package io.github.mineria_mc.mineria.common.effects.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IPoisonEffect {
    void applyEffectTick(LivingEntity living, int amplifier, int duration, int maxDuration, int potionClass);

    boolean doSpasms(int duration, int maxDuration, int potionClass);

    boolean doConvulsions(int duration, int maxDuration, int potionClass);

    boolean isDurationEffectTick(int duration, int amplifier, int potionClass);

    List<ItemStack> getCurativeItems(int potionClass, int amplifier, int maxDuration, int duration, PoisonSource source);

    void removeMovementSpeedModifier(LivingEntity living);
}
