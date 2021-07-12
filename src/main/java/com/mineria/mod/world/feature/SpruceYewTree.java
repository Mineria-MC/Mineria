package com.mineria.mod.world.feature;

import com.mineria.mod.init.BlocksInit;
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
            new SimpleBlockStateProvider(Blocks.SPRUCE_LOG.getDefaultState()),
            new WeightedBlockStateProvider().addWeightedBlockstate(Blocks.SPRUCE_LEAVES.getDefaultState(), 10).addWeightedBlockstate(BlocksInit.SPRUCE_YEW_LEAVES.getDefaultState(), 3),
            new SpruceFoliagePlacer(FeatureSpread.create(2, 1), FeatureSpread.create(0, 2), FeatureSpread.create(1, 1)),
            new StraightTrunkPlacer(5, 2, 1),
            new TwoLayerFeature(2, 0, 2)).setIgnoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive)
    {
        return Feature.TREE.withConfiguration(SPRUCE_YEW_TREE);
    }
}
