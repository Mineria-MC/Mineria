package com.mineria.mod.tab;

import com.mineria.mod.init.BlocksInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class MineriaTab extends CreativeTabs
{
	public MineriaTab(String label)
	{
		super(label);
		setBackgroundImageName("item_search.png");
	}
	
	public ItemStack getTabIconItem()
	{
		return new ItemStack(BlocksInit.lonsdaleite_ore);
	}
	
	public boolean hasSearchBar()
	{
		return true;
	}
}
