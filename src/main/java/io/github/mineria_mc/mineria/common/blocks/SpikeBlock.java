package io.github.mineria_mc.mineria.common.blocks;

import io.github.mineria_mc.mineria.common.init.MineriaCriteriaTriggers;
import io.github.mineria_mc.mineria.common.init.MineriaDamageTypes;
import io.github.mineria_mc.mineria.util.MineriaDamageSourceBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SpikeBlock extends Block {
    protected static final VoxelShape SPIKE_COLLISION_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.7375D * 16.0D, 16.0D);
    protected static final VoxelShape SPIKE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.8D * 16.0D, 16.0D);
    private final float attackDamage;

    public SpikeBlock(float hardness, float resistance, float attackDamage) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(hardness, resistance).sound(SoundType.METAL).requiresCorrectToolForDrops());
        this.attackDamage = attackDamage;
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, Entity entityIn) {
        boolean damaged = entityIn.hurt(MineriaDamageSourceBuilder.get(worldIn).ofType(MineriaDamageTypes.SPIKE), this.attackDamage);
        if (damaged && entityIn instanceof ServerPlayer) {
            MineriaCriteriaTriggers.DAMAGE_FROM_SPIKE.trigger((ServerPlayer) entityIn, this, this.attackDamage);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SPIKE_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SPIKE_COLLISION_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter reader, BlockPos pos, @NotNull CollisionContext context) {
        return SPIKE_SHAPE.move(pos.getX(), pos.getY(), pos.getZ());
    }
}
