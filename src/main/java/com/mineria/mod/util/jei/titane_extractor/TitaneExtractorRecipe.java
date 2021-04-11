package com.mineria.mod.util.jei.titane_extractor;

import com.google.common.collect.Lists;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TitaneExtractorRecipe implements IRecipeWrapper
{
	public static final TitaneExtractorRecipe DEFAULT_RECIPE = new TitaneExtractorRecipe(Lists.newArrayList(
			new ItemStack(BlocksInit.MINERAL_SAND), Item.getItemFromBlock(BlocksInit.WATER_BARREL).getDefaultInstance(), new ItemStack(ItemsInit.FILTER)),
			new ItemStack(ItemsInit.TITANE_NUGGET));

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
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}
}
