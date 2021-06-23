package com.mineria.mod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;

// Yup I also patched it :)
public class LonsdaleiteBlock extends Block
{
    public LonsdaleiteBlock()
    {
        super(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(10F, 17.5F).sound(SoundType.METAL).notSolid().harvestTool(ToolType.PICKAXE).harvestLevel(3).setOpaque((e, f, g) -> false).setSuffocates((e, f, g) -> true).setBlocksVision((e, f, g) -> false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
    {
        return adjacentBlockState.matchesBlock(this) || super.isSideInvisible(state, adjacentBlockState, side);
    }
}
