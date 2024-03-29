package io.github.mineria_mc.mineria.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

public class MineriaMushroomBlock extends MushroomBlock {
    public MineriaMushroomBlock(MapColor color) {
        super(BlockBehaviour.Properties.of().mapColor(color).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel((a) -> 1).hasPostProcess((a, b, c) -> true), null);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, RandomSource rand) {
        if (rand.nextInt(25) == 0) {
            int i = 5;

            for (BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (world.getBlockState(pos1).is(this)) {
                    --i;
                    if (i <= 0) return;
                }
            }

            BlockPos nextPos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for (int k = 0; k < 4; ++k) {
                if (world.isEmptyBlock(nextPos) && state.canSurvive(world, nextPos) && world.getRawBrightness(nextPos, 0) < 13)
                    pos = nextPos;

                nextPos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }

            if (world.isEmptyBlock(nextPos) && state.canSurvive(world, nextPos) && world.getRawBrightness(nextPos, 0) < 13)
                world.setBlock(nextPos, state, 2);
        }
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.CAVE;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = world.getBlockState(below);
        return belowState.is(BlockTags.MUSHROOM_GROW_BLOCK) || belowState.canSustainPlant(world, below, Direction.UP, this);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean pIsClient) {
        return false;
    }

    @Override
    public boolean growMushroom(@NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull RandomSource pRandom) {
        return false;
    }
}
