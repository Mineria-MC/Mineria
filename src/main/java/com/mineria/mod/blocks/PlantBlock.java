package com.mineria.mod.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.PlantType;

public class PlantBlock extends BushBlock
{
    protected static final VoxelShape PLANT_SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    private final boolean isBush;

    public PlantBlock(MaterialColor color, boolean isBush)
    {
        this(AbstractBlock.Properties.create(Material.PLANTS, color).doesNotBlockMovement().hardnessAndResistance(isBush ? 0.5F : 0).sound(SoundType.PLANT), isBush);
    }

    protected PlantBlock(AbstractBlock.Properties properties, boolean isBush)
    {
        super(properties);
        this.isBush = isBush;
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos)
    {
        return PlantType.PLAINS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return this.isBush ? super.getShape(state, worldIn, pos, context) : PLANT_SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if(this.isBush)
            entityIn.setMotionMultiplier(state, new Vector3d(0.95F, 0.75D, 0.95F));
    }
}
