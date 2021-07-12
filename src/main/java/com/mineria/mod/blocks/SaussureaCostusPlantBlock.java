package com.mineria.mod.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class SaussureaCostusPlantBlock extends DoublePlantBlock
{
    private static final VoxelShape SHAPE = Block.makeCuboidShape(4, 0, 4, 12, 16, 12);

    public SaussureaCostusPlantBlock()
    {
        super(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().zeroHardnessAndResistance().sound(SoundType.PLANT));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public OffsetType getOffsetType()
    {
        return OffsetType.NONE;
    }
}
