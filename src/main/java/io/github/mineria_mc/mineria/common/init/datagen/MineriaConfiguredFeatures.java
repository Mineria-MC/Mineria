package io.github.mineria_mc.mineria.common.init.datagen;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaFeatures;
import io.github.mineria_mc.mineria.common.world.feature.EasternJungleTree;
import io.github.mineria_mc.mineria.common.world.feature.ModVinesFeatureConfig;
import io.github.mineria_mc.mineria.common.world.feature.SakuraTree;
import io.github.mineria_mc.mineria.common.world.feature.SpruceYewTree;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class MineriaConfiguredFeatures {
    private static final MineriaBootstrapEntries.Simple<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = new MineriaBootstrapEntries.Simple<>(Registries.CONFIGURED_FEATURE);

    public static final ResourceKey<ConfiguredFeature<?, ?>> SPRUCE_YEW_TREE = register("spruce_yew_tree", Feature.TREE, SpruceYewTree.SPRUCE_YEW_TREE.get());
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAKURA_TREE = register("sakura_tree", Feature.TREE, SakuraTree.CONFIG.get());
    public static final ResourceKey<ConfiguredFeature<?, ?>> RHUBARB_EASTERN_PLAINS = register("rhubarb_eastern_plains", Feature.RANDOM_PATCH, patch(32, 4, 2, MineriaBlocks.RHUBARB.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> LYCIUM_CHINENSE_EASTERN_PLAINS = register("lycium_chinense_eastern_plains", Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(16, 3, 2,
                    PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(MineriaBlocks.LYCIUM_CHINENSE.get())),
                            BlockPredicate.anyOf(BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), Blocks.GRASS_BLOCK), BlockPredicate.matchesBlocks(Vec3i.ZERO.below(), MineriaBlocks.LYCIUM_CHINENSE.get())))));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SAUSSUREA_COSTUS_EASTERN_PLAINS = register("saussurea_costus_eastern_plains", Feature.RANDOM_PATCH, patch(32, 4, 2, MineriaBlocks.SAUSSUREA_COSTUS.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCHISANDRA_CHINENSIS_EASTERN_PLAINS = register("schisandra_chinensis_eastern_plains", MineriaFeatures.MOD_VINES,
            new ModVinesFeatureConfig(MineriaBlocks.SCHISANDRA_CHINENSIS.get().defaultBlockState(), 1, 10, false, Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new)));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PULSATILLA_CHINENSIS_EASTERN_PLAINS = register("pulsatilla_chinensis_eastern_plains", Feature.FLOWER, patch(32, 4, 2, MineriaBlocks.PULSATILLA_CHINENSIS.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> WATERLILY_EASTERN_PLAINS = register("waterlily_eastern_plains", Feature.RANDOM_PATCH, patch(4, 7, 3, Blocks.LILY_PAD));
    public static final ResourceKey<ConfiguredFeature<?, ?>> LILAC_EASTERN_PLAINS = register("lilac_eastern_plains", Feature.RANDOM_PATCH, patch(32, Blocks.LILAC));
    public static final ResourceKey<ConfiguredFeature<?, ?>> GIROLLE_BASE = register("girolle_base", Feature.RANDOM_PATCH, patch(32, MineriaBlocks.GIROLLE.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> HORN_OF_PLENTY_BASE = register("horn_of_plenty_base", Feature.RANDOM_PATCH, patch(32, MineriaBlocks.HORN_OF_PLENTY.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PUFFBALL_BASE = register("puffball_base", Feature.RANDOM_PATCH, patch(32, MineriaBlocks.PUFFBALL.get()));
    public static final ResourceKey<ConfiguredFeature<?, ?>> EASTERN_JUNGLE_TREE = register("eastern_jungle_tree", Feature.TREE, EasternJungleTree.CONFIG.get());

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> ResourceKey<ConfiguredFeature<?, ?>> register(String name, F feature, FC config) {
        return CONFIGURED_FEATURES.register(name, ctx -> new ConfiguredFeature<>(feature, config));
    }

    public static RandomPatchConfiguration patch(int tries, int xzSpread, int ySpread, Block block) {
        return new RandomPatchConfiguration(tries, xzSpread, ySpread, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block))));
    }

    public static RandomPatchConfiguration patch(int tries, Block block) {
        return patch(tries, 4, 2, block);
    }

    public static RandomPatchConfiguration patch(int tries, BlockState state) {
        return FeatureUtils.simpleRandomPatchConfiguration(tries, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(state))));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        CONFIGURED_FEATURES.registerAll(ctx);
        MineriaBiomeModifications.makeConfiguredFeatures(ctx);
    }
}
