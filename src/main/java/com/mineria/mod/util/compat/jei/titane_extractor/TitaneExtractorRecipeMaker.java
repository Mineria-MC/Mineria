package com.mineria.mod.util.compat.jei.titane_extractor;

public class TitaneExtractorRecipeMaker
{
	/*
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
				List<ItemStack> inputs = Lists.newArrayList(input1, input2, new ItemStack(ItemsInit.FILTER));
				TitaneExtractorRecipe recipe = new TitaneExtractorRecipe(inputs, output);
				jeiRecipes.add(recipe);
			}
		}
		return jeiRecipes;
	}

	 */
}
