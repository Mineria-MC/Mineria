package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMineralSand extends BlockFalling
{
	public BlockMineralSand(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHarvestLevel("shovel", harvestlevel);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
	}
}
