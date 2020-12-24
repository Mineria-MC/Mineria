package com.mineria.mod.util.compat.jei.titane_extractor;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorRecipes;
import com.mineria.mod.init.ItemsInit;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TitaneExtractorRecipeMaker
{
	public static List<TitaneExtractorRecipe> getRecipes(IJeiHelpers helpers)
	{
		IStackHelper stackHelper = helpers.getStackHelper();
		TitaneExtractorRecipes instance = TitaneExtractorRecipes.getInstance();
		Table<ItemStack, ItemStack, ItemStack> recipes = instance.getDualExtractingList();
		List<TitaneExtractorRecipe> jeiRecipes = Lists.newArrayList();
		
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : recipes.rowMap().entrySet())
		{
			for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet())
			{
				ItemStack input1 = entry.getKey();
				ItemStack input2 = ent.getKey();
				ItemStack output = ent.getValue();
				List<ItemStack> inputs = Lists.newArrayList(input1, input2, new ItemStack(ItemsInit.filter));
				TitaneExtractorRecipe recipe = new TitaneExtractorRecipe(inputs, output);
				jeiRecipes.add(recipe);
			}
		}
		return jeiRecipes;
	}
}
