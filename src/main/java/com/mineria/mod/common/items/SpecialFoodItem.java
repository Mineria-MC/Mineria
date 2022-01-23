package com.mineria.mod.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

public class SpecialFoodItem extends Item
{
    private final TriConsumer<ItemStack, World, LivingEntity> onFoodEaten;

    public SpecialFoodItem(Properties properties, TriConsumer<ItemStack, World, LivingEntity> onFoodEaten)
    {
        super(properties);
        this.onFoodEaten = onFoodEaten;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity living)
    {
        if(this.isEdible())
            onFoodEaten.accept(stack, world, living);
        return super.finishUsingItem(stack, world, living);
    }
}
