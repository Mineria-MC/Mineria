package com.mineria.mod.util.jei.infuser;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InfuserRecipe implements IRecipeWrapper
{
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public InfuserRecipe(List<ItemStack> inputs, ItemStack output)
    {
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
