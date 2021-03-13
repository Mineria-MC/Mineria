package com.mineria.mod.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FoodEffectBase extends Item
{
	private final int useDuration;

	public FoodEffectBase(Item.Properties properties, int hunger, float saturation, boolean isWolfFood, boolean alwaysEdible, int useDuration, Supplier<EffectInstance>... effects)
	{
		super(properties.food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
		ObfuscationReflectionHelper.setPrivateValue(Food.class, this.getFood(), isWolfFood, "meat");
		ObfuscationReflectionHelper.setPrivateValue(Food.class, this.getFood(), alwaysEdible, "canEatWhenFull");
		List<Pair<Supplier<EffectInstance>, Float>> effectslist = Arrays.stream(effects).map((effect) -> Pair.of(effect, 1F)).collect(Collectors.toList());
		ObfuscationReflectionHelper.setPrivateValue(Food.class, this.getFood(), effectslist, "effects");
		this.useDuration = useDuration;
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return this.useDuration;
	}
}
