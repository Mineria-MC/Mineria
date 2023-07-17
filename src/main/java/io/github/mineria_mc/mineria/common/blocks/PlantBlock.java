package io.github.mineria_mc.mineria.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

public class PlantBlock extends BushBlock {
    protected static final VoxelShape PLANT_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    private final boolean isBush;

    public PlantBlock(MapColor color, boolean isBush) {
        this(BlockBehaviour.Properties.of().mapColor(color).pushReaction(PushReaction.DESTROY).noCollission().strength(isBush ? 0.5F : 0).sound(SoundType.GRASS), isBush);
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
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.isBush ? super.getShape(state, worldIn, pos, context) : PLANT_SHAPE;
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entityIn) {
        if (this.isBush) {
            entityIn.makeStuckInBlock(state, new Vec3(0.95F, 0.75D, 0.95F));
        }
    }

    public boolean isBush() {
        return isBush;
    }
}
