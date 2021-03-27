package com.mineria.mod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class MineriaBlock extends Block
{
	public MineriaBlock(Material materialIn, float hardness, float resistance, SoundType sound, int harvestlevel)
	{
		super(AbstractBlock.Properties.create(materialIn).hardnessAndResistance(hardness, resistance).sound(sound).harvestTool(ToolType.PICKAXE).harvestLevel(harvestlevel));
	}
}