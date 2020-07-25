package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.ItemSpade;

public class ShovelBase extends ItemSpade
{
	public ShovelBase(String name, ToolMaterial material)
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
}
