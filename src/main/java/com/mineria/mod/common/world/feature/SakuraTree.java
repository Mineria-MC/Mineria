package com.mineria.mod.common.world.feature;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class SakuraTree extends AbstractTreeGrower
{
    public static final Supplier<TreeConfiguration> CONFIG = () -> new TreeConfiguration.TreeConfigurationBuilder(
            new SimpleStateProvider(Blocks.OAK_LOG.defaultBlockState()),
            new StraightTrunkPlacer(4, 2, 0),
            new SimpleStateProvider(MineriaBlocks.SAKURA_LEAVES.defaultBlockState()),
            new SimpleStateProvider(MineriaBlocks.SAKURA_SAPLING.defaultBlockState()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)).ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive)
    {
        return Feature.TREE.configured(CONFIG.get());
    }
}
