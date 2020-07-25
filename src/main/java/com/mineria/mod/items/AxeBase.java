package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.ItemAxe;

public class AxeBase extends ItemAxe
{
	public AxeBase(String name, ToolMaterial material, float speed , float damage)
	{
		super(material, damage, speed);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
}
