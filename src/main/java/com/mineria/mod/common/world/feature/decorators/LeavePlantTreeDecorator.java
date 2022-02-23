package com.mineria.mod.common.world.feature.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class LeavePlantTreeDecorator extends LeaveVineDecorator
{
    public static final Codec<LeavePlantTreeDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(dec -> dec.state)
    ).apply(instance, state1 -> new LeavePlantTreeDecorator((VineBlock) state1.getBlock())));

    private final BlockState state;

    public LeavePlantTreeDecorator(VineBlock vine)
    {
        this.state = vine.defaultBlockState();
    }

    @Override
    public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, Random pRandom, List<BlockPos> pLogPositions, List<BlockPos> pLeafPositions)
    {
        pLeafPositions.forEach((p_161744_) ->
        {
            if (pRandom.nextInt(4) == 0)
            {
                BlockPos blockpos = p_161744_.west();
                if (Feature.isAir(pLevel, blockpos))
                {
                    addHangingVine(pLevel, blockpos, pBlockSetter);
                }
            }

            if (pRandom.nextInt(4) == 0)
            {
                BlockPos blockpos1 = p_161744_.east();
                if (Feature.isAir(pLevel, blockpos1))
                {
                    addHangingVine(pLevel, blockpos1, pBlockSetter);
                }
            }

            if (pRandom.nextInt(4) == 0)
            {
                BlockPos blockpos2 = p_161744_.north();
                if (Feature.isAir(pLevel, blockpos2))
                {
                    addHangingVine(pLevel, blockpos2, pBlockSetter);
                }
            }

            if (pRandom.nextInt(4) == 0)
            {
                BlockPos blockpos3 = p_161744_.south();
                if (Feature.isAir(pLevel, blockpos3))
                {
                    addHangingVine(pLevel, blockpos3, pBlockSetter);
                }
            }
        });
    }

    private void addHangingVine(LevelSimulatedReader pLevel, BlockPos pPos, BiConsumer<BlockPos, BlockState> pBlockSetter)
    {
        addVine(pBlockSetter, pPos);
        int i = 4;

        for (BlockPos blockpos = pPos.below(); Feature.isAir(pLevel, blockpos) && i > 0; --i)
        {
            addVine(pBlockSetter, blockpos);
            blockpos = blockpos.below();
        }
    }

    protected void addVine(BiConsumer<BlockPos, BlockState> pBlockSetter, BlockPos pPos)
    {
        pBlockSetter.accept(pPos, state);
    }
}
