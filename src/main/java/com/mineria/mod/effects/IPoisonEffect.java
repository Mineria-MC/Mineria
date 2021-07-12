package com.mineria.mod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IPoisonEffect
{
    void performEffect(LivingEntity living, int amplifier, int duration, int maxDuration, int potionClass);

    boolean isReady(int duration, int amplifier, int potionClass);

    List<ItemStack> getCurativeItems(int potionClass, int amplifier, int maxDuration, int duration, PoisonSource source);
}
