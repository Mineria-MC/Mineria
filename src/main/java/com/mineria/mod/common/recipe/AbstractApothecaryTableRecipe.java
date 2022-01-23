package com.mineria.mod.common.recipe;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractApothecaryTableRecipe implements IRecipe<ApothecaryTableInventoryWrapper>
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
    public IRecipeType<?> getType()
    {
        return MineriaRecipeSerializers.APOTHECARY_TABLE_TYPE;
    }

    public boolean renderInJEI()
    {
        return true;
    }
}
