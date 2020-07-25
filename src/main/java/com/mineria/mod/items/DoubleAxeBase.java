package com.mineria.mod.items;

import java.util.Set;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;

public class DoubleAxeBase extends ItemTool
{
	private final Set<Block> effectiveBlocks;
	
	public DoubleAxeBase(String name, float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocksIn)
	{
		super(attackSpeed, attackSpeed, material, effectiveBlocksIn);
		this.attackDamage = attackDamage;
		this.attackSpeed = attackSpeed;
		this.toolMaterial = material;
		setCreativeTab(Mineria.mineriaTab);
		setUnlocalizedName(name);
		setRegistryName(name);
		this.effectiveBlocks = effectiveBlocksIn;
	}
}
