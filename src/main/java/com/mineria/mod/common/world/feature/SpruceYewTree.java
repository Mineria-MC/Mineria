package com.mineria.mod.common.world.feature;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class SpruceYewTree extends AbstractTreeGrower
{
    public static final Supplier<TreeConfiguration> SPRUCE_YEW_TREE = () -> new TreeConfiguration.TreeConfigurationBuilder(
            new SimpleStateProvider(Blocks.SPRUCE_LOG.defaultBlockState()),
            new StraightTrunkPlacer(5, 2, 1),
            new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(Blocks.SPRUCE_LEAVES.defaultBlockState(), 10).add(MineriaBlocks.SPRUCE_YEW_LEAVES.defaultBlockState(), 3)),
            new SimpleStateProvider(MineriaBlocks.SPRUCE_YEW_SAPLING.defaultBlockState()),
            new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive)
    {
        return Feature.TREE.configured(SPRUCE_YEW_TREE.get());
    }
}
