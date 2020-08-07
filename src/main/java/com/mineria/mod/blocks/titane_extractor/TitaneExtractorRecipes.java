package com.mineria.mod.blocks.titane_extractor;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TitaneExtractorRecipes
{
	private static final TitaneExtractorRecipes EXTRACTING_BASE = new TitaneExtractorRecipes();
	
	private final Table<ItemStack, ItemStack, ItemStack> extractingList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	
	public static TitaneExtractorRecipes instance()
    {
        return EXTRACTING_BASE;
    }
	
	private TitaneExtractorRecipes()
	{
		addExtractingRecipe(new ItemStack(BlocksInit.water_barrel), new ItemStack(BlocksInit.mineral_sand), new ItemStack(ItemsInit.titane_nugget));
	}
	
	public void addExtractingRecipe(ItemStack input, ItemStack input2, ItemStack stack)
    {
        if (getExtractingResult(input, input2) != ItemStack.EMPTY) return;
        this.extractingList.put(input, input2, stack);
    }
	
	public ItemStack getExtractingResult(ItemStack input1, ItemStack input2) 
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.extractingList.columnMap().entrySet()) 
		{
			if(this.compareItemStacks(input1, (ItemStack)entry.getKey())) 
			{
				for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) 
				{
					if(this.compareItemStacks(input2, (ItemStack)ent.getKey())) 
					{
						return (ItemStack)ent.getValue();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getDualExtractingList()
	{
		return this.extractingList;
	}
}
