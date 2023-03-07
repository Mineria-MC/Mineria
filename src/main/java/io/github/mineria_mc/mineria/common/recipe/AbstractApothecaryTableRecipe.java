package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public abstract class AbstractApothecaryTableRecipe implements Recipe<ApothecaryTableInventoryWrapper> {
    protected final ResourceLocation id;
    protected final Ingredient input;

    public AbstractApothecaryTableRecipe(ResourceLocation id, Ingredient input) {
        this.id = id;
        this.input = input;
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MineriaRecipeTypes.APOTHECARY_TABLE_TYPE.get();
    }

    public boolean renderInJEI() {
        return true;
    }
}
