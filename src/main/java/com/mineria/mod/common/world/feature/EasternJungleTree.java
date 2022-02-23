package com.mineria.mod.common.world.feature;

import com.google.common.collect.ImmutableList;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.world.feature.decorators.LeavePlantTreeDecorator;
import net.minecraft.data.worldgen.Features;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class EasternJungleTree extends AbstractTreeGrower
{
    public static final Supplier<TreeConfiguration> CONFIG = () -> new TreeConfiguration.TreeConfigurationBuilder(new SimpleStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
            new StraightTrunkPlacer(4, 8, 0),
            new SimpleStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
            new SimpleStateProvider(Blocks.JUNGLE_SAPLING.defaultBlockState()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1))
            .decorators(ImmutableList.of(new CocoaDecorator(0.2F), new LeavePlantTreeDecorator((VineBlock) MineriaBlocks.SCHISANDRA_CHINENSIS)))
            .ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random rand, boolean largeHive)
    {
        return Feature.TREE.configured(CONFIG.get());
    }
}
