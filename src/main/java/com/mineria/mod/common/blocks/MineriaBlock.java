package com.mineria.mod.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class MineriaBlock extends Block
{
	public MineriaBlock(Material materialIn, float hardness, float resistance, SoundType sound, int harvestLevel)
	{
		this(materialIn, hardness, resistance, sound, harvestLevel, ToolType.PICKAXE);
	}

	public MineriaBlock(Material materialIn, float hardness, float resistance, SoundType sound, int harvestLevel, ToolType toolType)
	{
		super(AbstractBlock.Properties.of(materialIn).strength(hardness, resistance).sound(sound).requiresCorrectToolForDrops().harvestTool(toolType).harvestLevel(harvestLevel));
	}
}