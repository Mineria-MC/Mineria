package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item
{
	public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return this == ItemsInit.lonsdaleite;
	}
}
