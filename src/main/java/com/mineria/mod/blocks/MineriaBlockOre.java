package com.mineria.mod.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MineriaBlockOre extends MineriaBlock
{
	public MineriaBlockOre(float hardness, float resistance, int harvestLevel)
	{
		super(Material.ROCK, hardness, resistance, harvestLevel, SoundType.STONE);
	}
}
