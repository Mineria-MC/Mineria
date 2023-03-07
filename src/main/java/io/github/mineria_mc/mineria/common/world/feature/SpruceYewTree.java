package io.github.mineria_mc.mineria.common.world.feature;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaConfiguredFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpruceYewTree extends AbstractTreeGrower {
    public static final Lazy<TreeConfiguration> SPRUCE_YEW_TREE = Lazy.of(() -> new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.SPRUCE_LOG.defaultBlockState()),
            new StraightTrunkPlacer(5, 2, 1),
            new WeightedStateProvider(new SimpleWeightedRandomList.Builder<BlockState>().add(Blocks.SPRUCE_LEAVES.defaultBlockState(), 10).add(MineriaBlocks.SPRUCE_YEW_LEAVES.get().defaultBlockState(), 3)),
            new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
            new TwoLayersFeatureSize(2, 0, 2)
    ).ignoreVines().build());

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@Nonnull RandomSource random, boolean hasFlower) {
        return MineriaConfiguredFeatures.SPRUCE_YEW_TREE;
    }
}
