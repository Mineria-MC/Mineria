package io.github.mineria_mc.mineria.common.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FuelSlot extends SlotItemHandler {
    @Nullable
    private final RecipeType<?> recipeType;

    public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(itemHandler, index, xPosition, yPosition, null);
    }

    public FuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, @Nullable RecipeType<?> recipeType) {
        super(itemHandler, index, xPosition, yPosition);
        this.recipeType = recipeType;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, recipeType) > 0;
    }
}
