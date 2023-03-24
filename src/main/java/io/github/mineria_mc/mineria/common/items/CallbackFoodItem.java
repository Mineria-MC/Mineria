package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class CallbackFoodItem extends Item {
    private final FoodEatenCallback onFoodEaten;

    public CallbackFoodItem(Properties properties, FoodEatenCallback onFoodEaten) {
        super(properties);
        this.onFoodEaten = onFoodEaten;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity living) {
        if (this.isEdible()) {
            onFoodEaten.onFoodEaten(stack, world, living);
        }
        return super.finishUsingItem(stack, world, living);
    }

    @FunctionalInterface
    public interface FoodEatenCallback {
        void onFoodEaten(ItemStack stack, Level level, LivingEntity entity);
    }
}
