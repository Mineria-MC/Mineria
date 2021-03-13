package com.mineria.mod.blocks.extractor;

public class ExtractorRecipes
{
	/*
	private static final ExtractorRecipes INSTANCE = new ExtractorRecipes();
	private final Table<ItemStack, ItemStack, Map<Integer, ItemStack>> extractingList = HashBasedTable.create();
	
	public static ExtractorRecipes getInstance()
	{
		return INSTANCE;
	}
	
	private ExtractorRecipes()
	{
		addExtractingRecipe(new ItemStack(BlocksInit.mineral_sand), new ItemStack(BlocksInit.water_barrel),
				addToMap(1, new ItemStack(ItemsInit.lonsdaleite), addToMap(10, new ItemStack(Items.DIAMOND), addToMap(100, new ItemStack(Items.GOLD_INGOT),
						addToMap(120, new ItemStack(ItemsInit.silver_ingot), addToMap(300, new ItemStack(ItemsInit.COPPER_INGOT),
								addToMap(600, new ItemStack(ItemsInit.lead_ingot), addToMap(800, new ItemStack(Items.IRON_INGOT), new HashMap<>()))))))));
	}

	public void addExtractingRecipe(ItemStack input1, ItemStack input2, Map<Integer, ItemStack> result)
	{
		if(result.size() > 7) return;
		if(!getExtractingResult(input1, input2).isEmpty()) return;
		this.extractingList.put(input1, input2, result);
	}
	
	public Map<Integer, ItemStack> getExtractingResult(ItemStack input1, ItemStack input2)
	{
		for(Entry<ItemStack, Map<ItemStack, Map<Integer, ItemStack>>> entry : this.extractingList.rowMap().entrySet())
		{
			if(this.compareItemStacks(input1, entry.getKey()))
			{
				for(Entry<ItemStack, Map<Integer, ItemStack>> ent : entry.getValue().entrySet())
				{
					if(this.compareItemStacks(input2, ent.getKey()))
					{
						return ent.getValue();
					}
				}
			}
		}
		return new HashMap<>();
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

	public static Map<Integer, ItemStack> addToMap(int key, ItemStack value, Map<Integer, ItemStack> map)
	{
		map.put(key, value);
		return map;
	}

	public Table<ItemStack, ItemStack, Map<Integer, ItemStack>> getTripleExtractingList()
	{
		return this.extractingList;
	}

	 */
}
