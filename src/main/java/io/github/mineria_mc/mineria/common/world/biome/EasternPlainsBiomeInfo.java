package io.github.mineria_mc.mineria.common.world.biome;

import io.github.mineria_mc.mineria.common.init.datagen.MineriaPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EasternPlainsBiomeInfo extends MineriaBiomeInfo {
    public EasternPlainsBiomeInfo(ResourceKey<Biome> biomeKey) {
        super(biomeKey);
    }

    @Override
    protected Biome.BiomeBuilder create() {
        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .temperature(0.9F)
                .downfall(0.4F);
    }

    @Override
    protected BiomeGenerationSettings.Builder configureGeneration(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers);

        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        builder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
        builder.addFeature(GenerationStep.Decoration.LAKES, MineriaPlacements.LAVA_LAKE_EASTERN_PLAINS);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);

        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.SAKURA_TREE);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.EASTERN_JUNGLE_TREE);
        BiomeDefaultFeatures.addPlainGrass(builder);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder);

        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.RHUBARB_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.LYCIUM_CHINENSE_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.SAUSSUREA_COSTUS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.SCHISANDRA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.PULSATILLA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.WATERLILY_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaPlacements.LILAC_EASTERN_PLAINS);
        BiomeDefaultFeatures.addLightBambooVegetation(builder);
        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

        return builder;
    }

    @Override
    protected BiomeSpecialEffects.Builder configureEffects() {
        return new BiomeSpecialEffects.Builder()
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(12638463)
                .foliageColorOverride(8958511)
                .grassColorOverride(8958511)
                .skyColor(getSkyColorWithTemperatureModifier(0.8F))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS);
    }

    @Override
    protected MobSpawnSettings.Builder configureMobSpawns() {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(builder);
        return builder;
    }

    @Override
    public int getSpawnWeight() {
        return 2;
    }

    @Override
    public boolean canSpawnUnderTemperature(Climate.Parameter temperature) {
        return temperature.min() == 2000F && temperature.max() == 5500F;
    }

    @Override
    public boolean canSpawnUnderHumidity(Climate.Parameter humidity) {
        return humidity.min() == -1000F || humidity.max() == 1000F;
    }
}