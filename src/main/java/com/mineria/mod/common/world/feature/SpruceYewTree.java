package com.mineria.mod.common.world.feature;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.SpruceFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class SpruceYewTree extends Tree
{
    public static final BaseTreeFeatureConfig SPRUCE_YEW_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(Blocks.SPRUCE_LOG.defaultBlockState()),
            new WeightedBlockStateProvider().add(Blocks.SPRUCE_LEAVES.defaultBlockState(), 10).add(MineriaBlocks.SPRUCE_YEW_LEAVES.defaultBlockState(), 3),
            new SpruceFoliagePlacer(FeatureSpread.of(2, 1), FeatureSpread.of(0, 2), FeatureSpread.of(1, 1)),
            new StraightTrunkPlacer(5, 2, 1),
            new TwoLayerFeature(2, 0, 2)).ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive)
    {
        return Feature.TREE.configured(SPRUCE_YEW_TREE);
    }
}
