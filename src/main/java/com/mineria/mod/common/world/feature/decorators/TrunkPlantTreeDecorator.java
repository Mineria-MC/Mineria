package com.mineria.mod.common.world.feature.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class TrunkPlantTreeDecorator extends TrunkVineDecorator
{
    public static final Codec<TrunkPlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, TrunkPlantTreeDecorator::new));

    private final BlockState state;

    public TrunkPlantTreeDecorator(BlockState state)
    {
        this.state = state;
    }

    @Override
    public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, List<BlockPos> pLogPositions, List<BlockPos> pLeafPositions)
    {
        pLogPositions.forEach((p_161764_) ->
        {
            if (pRandom.nextInt(3) > 0)
            {
                BlockPos blockpos = p_161764_.west();
                if (Feature.isAir(pLevel, blockpos))
                {
                    addVine(pBlockSetter, blockpos);
                }
            }

            if (pRandom.nextInt(3) > 0)
            {
                BlockPos blockpos1 = p_161764_.east();
                if (Feature.isAir(pLevel, blockpos1))
                {
                    addVine(pBlockSetter, blockpos1);
                }
            }

            if (pRandom.nextInt(3) > 0)
            {
                BlockPos blockpos2 = p_161764_.north();
                if (Feature.isAir(pLevel, blockpos2))
                {
                    addVine(pBlockSetter, blockpos2);
                }
            }

            if (pRandom.nextInt(3) > 0)
            {
                BlockPos blockpos3 = p_161764_.south();
                if (Feature.isAir(pLevel, blockpos3))
                {
                    addVine(pBlockSetter, blockpos3);
                }
            }

        });
    }

    protected void addVine(BiConsumer<BlockPos, BlockState> pBlockSetter, BlockPos pPos)
    {
        pBlockSetter.accept(pPos, state);
    }
}
