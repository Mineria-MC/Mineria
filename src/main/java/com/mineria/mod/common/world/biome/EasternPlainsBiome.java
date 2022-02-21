package com.mineria.mod.common.world.biome;

import com.mineria.mod.common.world.feature.MineriaConfiguredFeatures;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
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
                new Biome.Builder()
                .precipitation(Biome.RainType.RAIN)
                .biomeCategory(Biome.Category.PLAINS)
                .depth(-0.005F).scale(0.04F)
                .temperature(0.9F).downfall(0.4F)
                .specialEffects((new BiomeAmbience.Builder())
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .foliageColorOverride(8958511)
                        .grassColorOverride(8958511)
                        .skyColor(getSkyColorWithTemperatureModifier(0.8F))
                        .ambientMoodSound(MoodSoundAmbience.LEGACY_CAVE_SETTINGS).build())
                .mobSpawnSettings(new MobSpawnInfo.Builder().build())
                .generationSettings(new BiomeGenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS).build()).build());
    }

    @Override
    public void addFeatures(BiomeGenerationSettingsBuilder builder)
    {
        DefaultBiomeFeatures.addDefaultOverworldLandStructures(builder);
        builder.addStructureStart(StructureFeatures.RUINED_PORTAL_STANDARD);
        DefaultBiomeFeatures.addDefaultCarvers(builder);
        builder.addFeature(GenerationStage.Decoration.LAKES, Features.LAKE_LAVA.decorated(Placement.LAVA_LAKE.configured(new ChanceConfig(120))));
        DefaultBiomeFeatures.addLightBambooVegetation(builder);
        DefaultBiomeFeatures.addDefaultMonsterRoom(builder);
        DefaultBiomeFeatures.addPlainGrass(builder);

        DefaultBiomeFeatures.addDefaultUndergroundVariety(builder);
        DefaultBiomeFeatures.addDefaultOres(builder);
        DefaultBiomeFeatures.addDefaultSoftDisks(builder);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAKURA_TREE);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.EASTERN_JUGLE_TREE);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.RHUBARB_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.LYCIUM_BARBARUM_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAUSSUREA_COSTUS_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SCHISANDRA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.PULSATILLA_CHINENSIS_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.WATERLILLY_EASTERN_PLAINS);
        builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.LILAC_EASTERN_PLAINS);

        DefaultBiomeFeatures.addDefaultMushrooms(builder);
        DefaultBiomeFeatures.addDefaultExtraVegetation(builder);

        DefaultBiomeFeatures.addDefaultSprings(builder);
        DefaultBiomeFeatures.addSurfaceFreezing(builder);
    }

    @Override
    public void addSpawns(MobSpawnInfoBuilder builder)
    {
        DefaultBiomeFeatures.plainsSpawns(builder);
        builder.setPlayerCanSpawn();
    }

    @Override
    public void addTypes(List<Type> types)
    {
        types.add(Type.fromVanilla(Biome.Category.PLAINS));
    }
}