package com.mineria.mod.util.compat.jei.titane_extractor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import com.mineria.mod.blocks.titane_extractor.TitaneExtractorRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

public class TitaneExtractorRecipeMaker
{
	public static List<TitaneExtractorRecipe> getRecipes(IJeiHelpers helpers)
	{
		IStackHelper stackHelper = helpers.getStackHelper();
		TitaneExtractorRecipes instance = TitaneExtractorRecipes.instance();
		Table<ItemStack, ItemStack, ItemStack> recipes = instance.getDualExtractingList();
		List<TitaneExtractorRecipe> jeiRecipes = Lists.newArrayList();
		
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : recipes.columnMap().entrySet())
		{
			for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet())
			{
				ItemStack input1 = entry.getKey();
				ItemStack input2 = ent.getKey();
				ItemStack output = ent.getValue();
				TitaneExtractorRecipe recipe = new TitaneExtractorRecipe(input1, output);
				jeiRecipes.add(recipe);
			}
		}
		return jeiRecipes;
	}
}
