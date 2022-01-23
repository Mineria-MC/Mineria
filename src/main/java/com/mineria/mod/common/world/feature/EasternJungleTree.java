package com.mineria.mod.common.world.feature;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.world.feature.decorators.LeavePlantTreeDecorator;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class EasternJungleTree extends Tree
{
    public static final BaseTreeFeatureConfig CONFIG = new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
            new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
            new StraightTrunkPlacer(4, 8, 0),
            new TwoLayerFeature(1, 0, 1))
            .decorators(ImmutableList.of(new CocoaTreeDecorator(0.2F), new LeavePlantTreeDecorator((VineBlock) MineriaBlocks.SCHISANDRA_CHINENSIS)))
            .ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random rand, boolean largeHive)
    {
        return Feature.TREE.configured(CONFIG);
    }
}
