package com.mineria.mod.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

public class PlantBlock extends BushBlock
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public PlantBlock(MaterialColor color)
    {
        super(AbstractBlock.Properties.create(Material.PLANTS, color).doesNotBlockMovement().zeroHardnessAndResistance().sound(SoundType.PLANT));
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos)
    {
        return PlantType.PLAINS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }
}
