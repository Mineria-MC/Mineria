package com.mineria.mod.world.feature;

import com.mineria.mod.References;
import com.mineria.mod.init.BlocksInit;
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
    public static final ConfiguredFeature<?, ?> SAKURA_TREE = register("sakura_tree", Feature.TREE.withConfiguration(SakuraTree.CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.05F, 1))));
    public static final ConfiguredFeature<?, ?> RHUBARB_EASTERN_PLAINS = register("rhubarb_eastern_plains", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlocksInit.RHUBARB.getDefaultState()), new SimpleBlockPlacer()).tries(64).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(80).range(16).count(2));
    public static final ConfiguredFeature<?, ?> LYCIUM_BARBARUM_EASTERN_PLAINS = null; // chance : 60
    public static final ConfiguredFeature<?, ?> SAUSSUREA_COSTUS_EASTERN_PLAINS = register("saussurea_costus_eastern_plains", Feature.RANDOM_PATCH.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlocksInit.SAUSSUREA_COSTUS.getDefaultState()), new DoublePlantBlockPlacer()).tries(64).preventProjection().build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(10).range(16).count(2));
    public static final ConfiguredFeature<?, ?> SCHISANDRA_CHINENSIS_EASTERN_PLAINS = null; // chance : 80
    public static final ConfiguredFeature<?, ?> PULSATILLA_CHINENSIS_EASTERN_PLAINS = register("pulsatilla_chinensis_eastern_plains", Feature.FLOWER.withConfiguration(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlocksInit.PULSATILLA_CHINENSIS.getDefaultState()), new SimpleBlockPlacer()).tries(64).build()).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(80).range(16).count(2));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> feature)
    {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(References.MODID, name), feature);
        return feature;
    }
}
