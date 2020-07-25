package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.Item;

public class ItemFilter extends Item
{
	public ItemFilter(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setMaxStackSize(4);
	}
}
