package com.mineria.mod.world.feature;

import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class SakuraTree extends Tree
{
    public static final BaseTreeFeatureConfig SAKURA_TREE = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
            new SimpleBlockStateProvider(BlocksInit.SAKURA_LEAVES.getDefaultState()),
            new BlobFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)).setIgnoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive)
    {
        return Feature.TREE.withConfiguration(SAKURA_TREE);
    }
}
