package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import net.minecraft.item.ItemPickaxe;

public class PickaxeBase extends ItemPickaxe
{
	public PickaxeBase(String name, ToolMaterial material)
	{
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
	}
}
