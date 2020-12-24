package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBase extends Block
{
	public BlockBase(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHarvestLevel("pickaxe", harvestlevel);
		setHardness(hardness);
		setResistance(resistance);
		setSoundType(sound);
	}
}