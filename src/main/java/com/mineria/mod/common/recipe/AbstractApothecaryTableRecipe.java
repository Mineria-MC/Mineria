package com.mineria.mod.common.recipe;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractApothecaryTableRecipe implements Recipe<ApothecaryTableInventoryWrapper>
{
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Mineria.MODID, "apothecary_table");

    protected final ResourceLocation id;
    protected final Ingredient input;

    public AbstractApothecaryTableRecipe(ResourceLocation id, Ingredient input)
    {
        this.id = id;
        this.input = input;
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight)
    {
        return false;
    }

    @Override
    public ItemStack getResultItem()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    @Override
    public ResourceLocation getId()
    {
        return this.id;
    }

    @Override
    public RecipeType<?> getType()
    {
        return MineriaRecipeSerializers.APOTHECARY_TABLE_TYPE;
    }

    public boolean renderInJEI()
    {
        return true;
    }
}
