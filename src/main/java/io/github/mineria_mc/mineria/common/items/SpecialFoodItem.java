package io.github.mineria_mc.mineria.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nonnull;

public class SpecialFoodItem extends Item {
    private final TriConsumer<ItemStack, Level, LivingEntity> onFoodEaten;

    public SpecialFoodItem(Properties properties, TriConsumer<ItemStack, Level, LivingEntity> onFoodEaten) {
        super(properties);
        this.onFoodEaten = onFoodEaten;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity living) {
        if (this.isEdible()) {
            onFoodEaten.accept(stack, world, living);
        }
        return super.finishUsingItem(stack, world, living);
    }
}
