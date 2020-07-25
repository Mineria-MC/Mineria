package com.mineria.mod.blocks.extractor;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ExtractorRecipes
{
	private static final ExtractorRecipes INSTANCE = new ExtractorRecipes();
	private final Table<ItemStack, ItemStack, ItemStack> extractingList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	private final Table<ItemStack, ItemStack, ItemStack> extractingResultList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	
	public static ExtractorRecipes getInstance()
	{
		return INSTANCE;
	}
	
	private ExtractorRecipes()
	{
		addExtractingingRecipe(new ItemStack(BlocksInit.mineral_sand), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.filter), new ItemStack(Items.IRON_INGOT));
	}
	
	public void addExtractingingRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack result)
	{
		if(getExtractingResult(input1, input2, input3) != ItemStack.EMPTY) return;
		this.extractingList.put(input1, input2, input3);
		this.extractingResultList.put(result, result, result);
	}
	
	public ItemStack getExtractingResult(ItemStack input1, ItemStack input2, ItemStack input3)
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.extractingList.columnMap().entrySet())
		{
			if(this.compareItemStacks(input1, input2, (ItemStack)entry.getKey()))
			{
				for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet())
				{
					if(this.compareItemStacks(input3, input1, (ItemStack)ent.getKey())) 
					{
						if(this.compareItemStacks(input3, input2, (ItemStack)ent.getKey()))
						{
							return (ItemStack)ent.getValue();
						}
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2, ItemStack stack3)
	{
		return stack3.getItem() == stack2.getItem() && stack2.getItem() == stack1.getItem() && (stack3.getMetadata() == 32767 || stack3.getMetadata() == stack2.getMetadata() && stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata()); 
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getTripleExtractingList()
	{
		return this.extractingList;
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getTripleExtractingResultList()
	{
		return this.extractingResultList;
	}
}
