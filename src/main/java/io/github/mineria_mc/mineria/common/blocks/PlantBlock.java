package io.github.mineria_mc.mineria.common.blocks;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.PlantType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PlantBlock extends BushBlock {
    protected static final VoxelShape PLANT_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    private final boolean isBush;

    public PlantBlock(MaterialColor color, boolean isBush) {
        this(BlockBehaviour.Properties.of(Material.PLANT, color).noCollission().strength(isBush ? 0.5F : 0).sound(SoundType.GRASS), isBush);
    }

    protected PlantBlock(BlockBehaviour.Properties properties, boolean isBush) {
        super(properties);
        this.isBush = isBush;
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.PLAINS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.isBush ? super.getShape(state, worldIn, pos, context) : PLANT_SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (this.isBush) {
            entityIn.makeStuckInBlock(state, new Vec3(0.95F, 0.75D, 0.95F));
        }
    }

    public boolean isBush() {
        return isBush;
    }
}
