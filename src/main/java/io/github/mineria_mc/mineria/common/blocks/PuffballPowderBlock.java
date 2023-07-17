package io.github.mineria_mc.mineria.common.blocks;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class PuffballPowderBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public PuffballPowderBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).replaceable().pushReaction(PushReaction.DESTROY).strength(1.2F).randomTicks().sound(SoundType.WEEPING_VINES).noLootTable());
    }

    @Override
    public void randomTick(@Nonnull BlockState state, ServerLevel world, BlockPos pos, @Nonnull RandomSource rand) {
        BlockState underneath = world.getBlockState(pos.below());
        if (underneath.is(Blocks.GRASS_BLOCK)) {
            world.setBlock(pos.below(), Blocks.MYCELIUM.defaultBlockState(), 3);
        }
    }

    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext ctx) {
        return SHAPE;
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState state, @Nonnull Direction direction, @Nonnull BlockState newState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos newPos) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, newState, world, pos, newPos);
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, LevelReader world, BlockPos pos) {
        return !world.isEmptyBlock(pos.below());
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(MineriaBlocks.PUFFBALL.get());
    }
}
