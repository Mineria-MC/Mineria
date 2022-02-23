package com.mineria.mod.common.world.feature;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;

public class MineriaConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> SAKURA_TREE = register("sakura_tree", Feature.TREE.configured(SakuraTree.CONFIG.get()).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.05F, 1))));
    public static final ConfiguredFeature<?, ?> RHUBARB_EASTERN_PLAINS = register("rhubarb_eastern_plains", Feature.FLOWER.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.RHUBARB.defaultBlockState()), new SimpleBlockPlacer()).tries(32).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(80).count(2));
    public static final ConfiguredFeature<?, ?> LYCIUM_BARBARUM_EASTERN_PLAINS = register("lycium_barbarum_eastern_plains", Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.LYCIUM_CHINENSE.defaultBlockState()), new SimpleBlockPlacer()).tries(16).xspread(3).yspread(2).zspread(3).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, MineriaBlocks.LYCIUM_CHINENSE)).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(60).squared());
    public static final ConfiguredFeature<?, ?> SAUSSUREA_COSTUS_EASTERN_PLAINS = register("saussurea_costus_eastern_plains", Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.SAUSSUREA_COSTUS.defaultBlockState()), new DoublePlantPlacer()).tries(32).noProjection().build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(10).count(2));
    public static final ConfiguredFeature<?, ?> SCHISANDRA_CHINENSIS_EASTERN_PLAINS = register("schisandra_chinensis_eastern_plains", MineriaFeatures.MOD_VINES.configured(new ModVinesFeatureConfig(MineriaBlocks.SCHISANDRA_CHINENSIS.defaultBlockState(), 1, 10, false, Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new))).squared().rarity(80).count(16));
    public static final ConfiguredFeature<?, ?> PULSATILLA_CHINENSIS_EASTERN_PLAINS = register("pulsatilla_chinensis_eastern_plains", Feature.FLOWER.configured(new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.PULSATILLA_CHINENSIS.defaultBlockState()), new SimpleBlockPlacer()).tries(32).build()).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(80).count(2));
    public static final ConfiguredFeature<?, ?> WATERLILLY_EASTERN_PLAINS = register("waterlilly_eastern_plains", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(Blocks.LILY_PAD.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(4).build()).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(2));
    public static final ConfiguredFeature<?, ?> LILAC_EASTERN_PLAINS = register("lilac_eastern_plains", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(Blocks.LILAC.defaultBlockState()), new DoublePlantPlacer())).tries(32).noProjection().build()).count(UniformInt.of(-3, 4)).decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE).count(5));
    public static final ConfiguredFeature<?, ?> GIROLLE_BASE = register("girolle_base", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.GIROLLE.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> HORN_OF_PLENTY_BASE = register("horn_of_plenty_base", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.HORN_OF_PLENTY.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> PUFFBALL_BASE = register("puffball_base", Feature.RANDOM_PATCH.configured((new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(MineriaBlocks.PUFFBALL.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> EASTERN_JUGLE_TREE = register("eastern_jungle_tree", Feature.TREE.configured(EasternJungleTree.CONFIG.get()).decorated(Features.Decorators.HEIGHTMAP_SQUARE).decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(0, 0.005F, 1))));

    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature)
    {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Mineria.MODID, name), feature);
        return feature;
    }
}
