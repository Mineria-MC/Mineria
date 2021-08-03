package com.mineria.mod.world.feature;

import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class ModVinesFeature extends Feature<BlockStateFeatureConfig>
{
    public ModVinesFeature()
    {
        super(BlockStateFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config)
    {
        BlockPos.Mutable mutablePos = pos.toMutable();

        for (int i = 64; i < 256; ++i)
        {
            mutablePos.setPos(pos);
            mutablePos.move(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            mutablePos.setY(i);
            if (reader.isAirBlock(mutablePos))
            {
                for (Direction direction : Direction.values())
                {
                    if (direction != Direction.DOWN && VineBlock.canAttachTo(reader, mutablePos, direction))
                    {
                        reader.setBlockState(mutablePos, config.state.with(VineBlock.getPropertyFor(direction), true), 2);
                        break;
                    }
                }
            }
        }

        return true;
    }
}
