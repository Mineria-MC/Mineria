package com.mineria.mod.common.world.feature;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

public class MineriaConfiguredFeatures
{
    public static final ConfiguredFeature<?, ?> SAKURA_TREE = register("sakura_tree", Feature.TREE.configured(SakuraTree.CONFIG).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(0, 0.05F, 1))));
    public static final ConfiguredFeature<?, ?> RHUBARB_EASTERN_PLAINS = register("rhubarb_eastern_plains", Feature.FLOWER.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.RHUBARB.defaultBlockState()), new SimpleBlockPlacer()).tries(32).build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(80).range(16).count(2));
    public static final ConfiguredFeature<?, ?> LYCIUM_BARBARUM_EASTERN_PLAINS = register("lycium_barbarum_eastern_plains", Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.LYCIUM_CHINENSE.defaultBlockState()), new SimpleBlockPlacer()).tries(16).xspread(3).yspread(2).zspread(3).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, MineriaBlocks.LYCIUM_CHINENSE)).build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(60).range(32).squared());
    public static final ConfiguredFeature<?, ?> SAUSSUREA_COSTUS_EASTERN_PLAINS = register("saussurea_costus_eastern_plains", Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.SAUSSUREA_COSTUS.defaultBlockState()), new DoublePlantBlockPlacer()).tries(32).noProjection().build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(10).range(16).count(2));
    public static final ConfiguredFeature<?, ?> SCHISANDRA_CHINENSIS_EASTERN_PLAINS = register("schisandra_chinensis_eastern_plains", MineriaFeatures.MOD_VINES.configured(new ModVinesFeatureConfig(MineriaBlocks.SCHISANDRA_CHINENSIS.defaultBlockState(), 1, 10, false, Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new))).squared().chance(80).range(16).count(16));
    public static final ConfiguredFeature<?, ?> PULSATILLA_CHINENSIS_EASTERN_PLAINS = register("pulsatilla_chinensis_eastern_plains", Feature.FLOWER.configured(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.PULSATILLA_CHINENSIS.defaultBlockState()), new SimpleBlockPlacer()).tries(32).build()).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(80).range(16).count(2));
    public static final ConfiguredFeature<?, ?> WATERLILLY_EASTERN_PLAINS = register("waterlilly_eastern_plains", Feature.RANDOM_PATCH.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(4).build()).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
    public static final ConfiguredFeature<?, ?> LILAC_EASTERN_PLAINS = register("lilac_eastern_plains", Feature.RANDOM_PATCH.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILAC.defaultBlockState()), new DoublePlantBlockPlacer())).tries(32).noProjection().build()).count(FeatureSpread.of(-3, 4)).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(5));
    public static final ConfiguredFeature<?, ?> GIROLLE_BASE = register("girolle_base", Feature.RANDOM_PATCH.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.GIROLLE.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> HORN_OF_PLENTY_BASE = register("horn_of_plenty_base", Feature.RANDOM_PATCH.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.HORN_OF_PLENTY.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> PUFFBALL_BASE = register("puffball_base", Feature.RANDOM_PATCH.configured((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(MineriaBlocks.PUFFBALL.defaultBlockState()), SimpleBlockPlacer.INSTANCE)).tries(32).noProjection().build()));
    public static final ConfiguredFeature<?, ?> EASTERN_JUGLE_TREE = register("eastern_jungle_tree", Feature.TREE.configured(EasternJungleTree.CONFIG).decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(0, 0.005F, 1))));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature)
    {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Mineria.MODID, name), feature);
        return feature;
    }
}
