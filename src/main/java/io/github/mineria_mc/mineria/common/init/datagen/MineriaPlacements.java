package io.github.mineria_mc.mineria.common.init.datagen;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.Nullable;
import java.util.List;

public final class MineriaPlacements {
    private static final MineriaBootstrapEntries<PlacedFeature, Entry> PLACED_FEATURES = new MineriaBootstrapEntries<>(Registries.PLACED_FEATURE);

    public static final ResourceKey<PlacedFeature> LAVA_LAKE_EASTERN_PLAINS = register("lava_lake_eastern_plains",
            MiscOverworldFeatures.LAKE_LAVA,
            RarityFilter.onAverageOnceEvery(500),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> SAKURA_TREE = register("sakura_tree", MineriaConfiguredFeatures.SAKURA_TREE,
            VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05F, 1)));
    public static final ResourceKey<PlacedFeature> RHUBARB_EASTERN_PLAINS = register("rhubarb_eastern_plains", MineriaConfiguredFeatures.RHUBARB_EASTERN_PLAINS,
            RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> LYCIUM_CHINENSE_EASTERN_PLAINS = register("lycium_chinense_eastern_plains", MineriaConfiguredFeatures.LYCIUM_CHINENSE_EASTERN_PLAINS,
            RarityFilter.onAverageOnceEvery(60), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> SAUSSUREA_COSTUS_EASTERN_PLAINS = register("saussurea_costus_eastern_plains", MineriaConfiguredFeatures.SAUSSUREA_COSTUS_EASTERN_PLAINS,
            RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> SCHISANDRA_CHINENSIS_EASTERN_PLAINS = register("schisandra_chinensis_eastern_plains", MineriaConfiguredFeatures.SCHISANDRA_CHINENSIS_EASTERN_PLAINS,
            CountPlacement.of(16), RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(), BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PULSATILLA_CHINENSIS_EASTERN_PLAINS = register("pulsatilla_chinensis_eastern_plains", MineriaConfiguredFeatures.PULSATILLA_CHINENSIS_EASTERN_PLAINS,
            RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> WATERLILY_EASTERN_PLAINS = register("waterlily_eastern_plains", MineriaConfiguredFeatures.WATERLILY_EASTERN_PLAINS,
            CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> LILAC_EASTERN_PLAINS = register("lilac_eastern_plains", MineriaConfiguredFeatures.LILAC_EASTERN_PLAINS,
            CountPlacement.of(ClampedInt.of(UniformInt.of(-3, 1), 0, 1)), RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> GIROLLE_BASE = register("girolle_base", MineriaConfiguredFeatures.GIROLLE_BASE, getMushroomPlacement(256, null));
    public static final ResourceKey<PlacedFeature> HORN_OF_PLENTY_BASE = register("horn_of_plenty_base", MineriaConfiguredFeatures.HORN_OF_PLENTY_BASE, getMushroomPlacement(256, null));
    public static final ResourceKey<PlacedFeature> PUFFBALL_BASE = register("puffball_base", MineriaConfiguredFeatures.PUFFBALL_BASE, getMushroomPlacement(256, null));
    public static final ResourceKey<PlacedFeature> EASTERN_JUNGLE_TREE = register("eastern_jungle_tree", MineriaConfiguredFeatures.EASTERN_JUNGLE_TREE,
            VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.005F, 1)));


    private static ResourceKey<PlacedFeature> register(String name, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
        return register(name, feature, List.of(modifiers));
    }

    private static ResourceKey<PlacedFeature> register(String name, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        return PLACED_FEATURES.register(name, features -> new PlacedFeature(features.getOrThrow(feature), modifiers));
    }

    private static List<PlacementModifier> getMushroomPlacement(int rarity, @Nullable PlacementModifier modifier) {
        ImmutableList.Builder<PlacementModifier> builder = ImmutableList.builder();
        if (modifier != null) {
            builder.add(modifier);
        }

        if (rarity != 0) {
            builder.add(RarityFilter.onAverageOnceEvery(rarity));
        }

        builder.add(InSquarePlacement.spread());
        builder.add(PlacementUtils.HEIGHTMAP);
        builder.add(BiomeFilter.biome());
        return builder.build();
    }

    public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
        PLACED_FEATURES.registerAll(ctx);
        MineriaBiomeModifications.makePlacedFeatures(ctx);
    }

    @FunctionalInterface
    private interface Entry extends MineriaBootstrapEntries.Entry<PlacedFeature> {
        @Override
        default PlacedFeature create(MineriaBootstrapContext<PlacedFeature> ctx) {
            return create(ctx.lookup(Registries.CONFIGURED_FEATURE));
        }

        PlacedFeature create(HolderGetter<ConfiguredFeature<?, ?>> features);
    }
}
