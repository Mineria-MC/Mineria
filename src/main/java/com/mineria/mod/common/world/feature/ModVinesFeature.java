package com.mineria.mod.common.world.feature;

import net.minecraft.world.level.block.VineBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModVinesFeature extends Feature<ModVinesFeatureConfig>
{
    public ModVinesFeature()
    {
        super(ModVinesFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<ModVinesFeatureConfig> pContext)
    {
        BlockPos pos = pContext.origin();
        Random rand = pContext.random();
        WorldGenLevel reader = pContext.level();
        ModVinesFeatureConfig config = pContext.config();
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        for (int i = 64; i < 256; ++i)
        {
            mutablePos.set(pos);
            mutablePos.move(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
            mutablePos.setY(i);
            if (reader.isEmptyBlock(mutablePos))
            {
                for (Direction direction : Direction.values())
                {
                    if (config.acceptedDirections.contains(direction) && VineBlock.isAcceptableNeighbour(reader, mutablePos.relative(direction), direction))
                    {
                        int count = config.minHeight == -1 ? config.maxHeight : rand.nextInt((config.maxHeight - config.minHeight) + 1) + config.minHeight;

                        if(direction.getAxis().isHorizontal())
                            generateVines(reader, config, mutablePos, direction, count);
                        else
                            reader.setBlock(mutablePos, config.state.setValue(VineBlock.getPropertyForFace(direction), true), 2);
                        break;
                    }
                }
            }
        }

        return true;
    }

    private static void generateVines(WorldGenLevel reader, ModVinesFeatureConfig config, BlockPos.MutableBlockPos mutablePos, Direction direction, int count)
    {
        List<BlockPos> generatingPositions = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            BlockPos below = mutablePos.below(i);

            if(reader.isEmptyBlock(below))
            {
                if (VineBlock.isAcceptableNeighbour(reader, below.relative(direction), direction) || reader.isEmptyBlock(below.relative(direction.getOpposite())))
                {
                    generatingPositions.add(below);
                }
                else
                    break;
            }
            else
                break;
        }

        int minCount = config.minHeight == -1 ? 1 : config.minHeight;
        if(generatingPositions.size() == count || (generatingPositions.size() >= minCount && !config.strictCount))
        {
            generatingPositions.forEach(pos -> reader.setBlock(pos, config.state.setValue(VineBlock.getPropertyForFace(direction), true), 2));
        }
    }
}
