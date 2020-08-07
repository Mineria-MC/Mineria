package com.mineria.mod.util.compat.jei.titane_extractor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TitaneExtractorRecipe implements IRecipeWrapper
{
	private final List<ItemStack> inputs;
	private final ItemStack output;
	
	public TitaneExtractorRecipe(List<ItemStack> inputs, ItemStack output)
	{
		this.inputs = inputs;
		this.output = output;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutput(ItemStack.class, output);
	}
}
