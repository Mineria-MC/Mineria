package com.mineria.mod.common.world.biome;

import com.mineria.mod.common.world.feature.MineriaConfiguredFeatures;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.world.level.biome.*;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;

import java.util.List;

public class EasternPlainsBiome extends MineriaBiome
{
    public EasternPlainsBiome()
    {
        super("eastern_plains", 8, BiomeManager.BiomeType.COOL,
                new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .biomeCategory(Biome.BiomeCategory.PLAINS)
                .depth(-0.005F).scale(0.04F)
                .temperature(0.9F).downfall(0.4F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .foliageColorOverride(8958511)
                        .grassColorOverride(8958511)
                        .skyColor(getSkyColorWithTemperatureModifier(0.8F))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(new BiomeGenerationSettings.Builder().surfaceBuilder(SurfaceBuilders.GRASS).build()).build());
    }

    @Override
    public void addFeatures(BiomeGenerationSettingsBuilder builder)
    {
        BiomeDefaultFeatures.addDefaultOverworldLandStructures(builder);
        builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        BiomeDefaultFeatures.addDefaultCarvers(builder);
        builder.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA.decorated(FeatureDecorator.LAVA_LAKE.configured(new ChanceDecoratorConfiguration(120))));
        BiomeDefaultFeatures.addLightBambooVegetation(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addPlainGrass(builder);

        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultOres(builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAKURA_TREE);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.EASTERN_JUGLE_TREE);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.RHUBARB_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.LYCIUM_BARBARUM_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAUSSUREA_COSTUS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SCHISANDRA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.PULSATILLA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.WATERLILLY_EASTERN_PLAINS);
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.LILAC_EASTERN_PLAINS);

        BiomeDefaultFeatures.addDefaultMushrooms(builder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    @Override
    public void addSpawns(MobSpawnInfoBuilder builder)
    {
        BiomeDefaultFeatures.plainsSpawns(builder);
        builder.setPlayerCanSpawn();
    }

    @Override
    public void addTypes(List<Type> types)
    {
        types.add(Type.fromVanilla(Biome.BiomeCategory.PLAINS));
    }
}