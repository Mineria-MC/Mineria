package com.mineria.mod.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MineriaBlockMetal extends MineriaBlock
{
	public MineriaBlockMetal(float hardness, float resistance, int harvestLevel)
	{
		super(Material.IRON, new BlockProperties().hardnessAndResistance(hardness, resistance).harvestLevel(harvestLevel, "pickaxe").sounds(SoundType.METAL).beaconBase());
	}
}
