package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.ItemHoe;

public class HoeBase extends ItemHoe
{
	public HoeBase(String name, ToolMaterial material)
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
}
