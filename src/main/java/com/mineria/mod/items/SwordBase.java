package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.ItemSword;

public class SwordBase extends ItemSword
{
	public SwordBase(String name, ToolMaterial material)
	{
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
}
