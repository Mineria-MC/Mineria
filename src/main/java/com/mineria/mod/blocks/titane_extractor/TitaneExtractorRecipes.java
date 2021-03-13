package com.mineria.mod.blocks.titane_extractor;

public class TitaneExtractorRecipes
{
	/*
	private static final TitaneExtractorRecipes INSTANCE = new TitaneExtractorRecipes();
	
	private final Table<ItemStack, ItemStack, ItemStack> extractingList = HashBasedTable.create();
	
	public static TitaneExtractorRecipes getInstance()
    {
        return INSTANCE;
    }
	
	private TitaneExtractorRecipes()
	{
		addExtractingRecipe(new ItemStack(BlocksInit.mineral_sand), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.titane_nugget));
	}
	
	public void addExtractingRecipe(ItemStack input, ItemStack input2, ItemStack stack)
    {
        if (getExtractingResult(input, input2) != ItemStack.EMPTY) return;
        this.extractingList.put(input, input2, stack);
    }
	
	public ItemStack getExtractingResult(ItemStack input1, ItemStack input2) 
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.extractingList.rowMap().entrySet())
		{
			if(this.compareItemStacks(input1, entry.getKey()))
			{
				for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) 
				{
					if(this.compareItemStacks(input2, ent.getKey()))
					{
						return ent.getValue();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem();
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getDualExtractingList()
	{
		return this.extractingList;
	}
	 */
}
