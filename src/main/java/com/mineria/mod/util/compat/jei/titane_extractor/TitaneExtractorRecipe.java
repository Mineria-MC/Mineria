package com.mineria.mod.util.compat.jei.titane_extractor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class TitaneExtractorRecipe implements IRecipeWrapper
{
	private final ItemStack input;
	private final ItemStack output;
	
	public TitaneExtractorRecipe(ItemStack input, ItemStack output)
	{
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutput(ItemStack.class, output);
	}
}
