package io.github.mineria_mc.mineria.common.world.feature;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.datagen.MineriaConfiguredFeatures;
import io.github.mineria_mc.mineria.common.world.feature.decorators.LeavePlantTreeDecorator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public class EasternJungleTree extends AbstractTreeGrower {
    public static final Lazy<TreeConfiguration> CONFIG = Lazy.of(() -> new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.JUNGLE_LOG.defaultBlockState()),
            new StraightTrunkPlacer(4, 8, 0),
            BlockStateProvider.simple(Blocks.JUNGLE_LEAVES.defaultBlockState()),
            new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
    ).decorators(ImmutableList.of(new CocoaDecorator(0.2F), new LeavePlantTreeDecorator((VineBlock) MineriaBlocks.SCHISANDRA_CHINENSIS.get()))).ignoreVines().build());

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return MineriaConfiguredFeatures.EASTERN_JUNGLE_TREE;
    }
}
