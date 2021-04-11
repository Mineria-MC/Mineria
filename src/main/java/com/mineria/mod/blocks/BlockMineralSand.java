package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMineralSand extends BlockFalling
{
	public BlockMineralSand()
	{
		super(Material.SAND);
		setCreativeTab(Mineria.MINERIA_TAB);
		setHarvestLevel("shovel", 0);
		setHardness(0.5F);
		setResistance(0.5F);
		setSoundType(SoundType.SAND);
	}
}
