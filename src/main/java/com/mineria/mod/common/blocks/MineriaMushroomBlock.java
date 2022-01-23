package com.mineria.mod.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;

import java.util.Random;

public class MineriaMushroomBlock extends MushroomBlock
{
    public MineriaMushroomBlock(MaterialColor color)
    {
        super(AbstractBlock.Properties.of(Material.PLANT, color).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel((a) -> 1).hasPostProcess((a, b, c) -> true));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
    {
        if (rand.nextInt(25) == 0)
        {
            int i = 5;

            for (BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4)))
            {
                if (world.getBlockState(pos1).is(this))
                {
                    --i;
                    if (i <= 0) return;
                }
            }

            BlockPos nextPos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for (int k = 0; k < 4; ++k)
            {
                if (world.isEmptyBlock(nextPos) && state.canSurvive(world, nextPos) && world.getRawBrightness(nextPos, 0) < 13)
                    pos = nextPos;

                nextPos = pos.offset(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }

            if (world.isEmptyBlock(nextPos) && state.canSurvive(world, nextPos) && world.getRawBrightness(nextPos, 0) < 13)
                world.setBlock(nextPos, state, 2);
        }
    }

    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos)
    {
        return PlantType.CAVE;
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos)
    {
        BlockPos below = pos.below();
        BlockState belowState = world.getBlockState(below);
        return belowState.is(BlockTags.MUSHROOM_GROW_BLOCK) || belowState.canSustainPlant(world, below, Direction.UP, this);
    }
}
