package com.mineria.mod.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.util.TriConsumer;

import net.minecraft.world.item.Item.Properties;

public class SpecialFoodItem extends Item
{
    private final TriConsumer<ItemStack, Level, LivingEntity> onFoodEaten;

    public SpecialFoodItem(Properties properties, TriConsumer<ItemStack, Level, LivingEntity> onFoodEaten)
    {
        super(properties);
        this.onFoodEaten = onFoodEaten;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living)
    {
        if(this.isEdible())
            onFoodEaten.accept(stack, world, living);
        return super.finishUsingItem(stack, world, living);
    }
}
