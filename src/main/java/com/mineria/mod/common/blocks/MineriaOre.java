package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class MineriaOre extends OreBlock
{
	public MineriaOre(int harvestLevel, float hardness, float resistance, SoundType sound)
	{
		super(AbstractBlock.Properties.of(Material.STONE).strength(hardness, resistance).requiresCorrectToolForDrops().sound(sound).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE));
	}

	@Override
	protected int xpOnDrop(Random rand)
	{
		return this == MineriaBlocks.LONSDALEITE_ORE ? MathHelper.nextInt(rand, 4, 10) : super.xpOnDrop(rand);
	}
}
