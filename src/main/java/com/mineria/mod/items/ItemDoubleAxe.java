package com.mineria.mod.items;

import com.mineria.mod.Mineria;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemDoubleAxe extends ItemAxe
{
	public ItemDoubleAxe(float attackDamage, float attackSpeed, ToolMaterial material)
	{
		super(material, 0, 0);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
		setCreativeTab(Mineria.MINERIA_TAB);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return repair.getItem().equals(ItemsInit.getToolRepairItems().get(toolMaterial)) || super.getIsRepairable(toRepair, repair);
	}
}
