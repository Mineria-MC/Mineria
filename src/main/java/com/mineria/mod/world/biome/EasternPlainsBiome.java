package com.mineria.mod.world.biome;

import com.mineria.mod.world.feature.MineriaConfiguredFeatures;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
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
                .category(Biome.Category.NONE)
                .depth(0.125F).scale(0.05F)
                .temperature(0.9F).downfall(0.4F)
                .setEffects((new BiomeAmbience.Builder())
                        .setWaterColor(4159204)
                        .setWaterFogColor(329011)
                        .setFogColor(12638463)
                        .withFoliageColor(8958511)
                        .withGrassColor(8958511)
                        .withSkyColor(getSkyColorWithTemperatureModifier(0.8F))
                        .setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build())
                .withMobSpawnSettings(spawnInfoBuilder.build())
                .withGenerationSettings(settingsBuilder.build()).build());
    }

    // TODO Add larger lakes features and change bamboo feature
    @Override
    public void addFeatures(BiomeGenerationSettingsBuilder builder)
    {
        DefaultBiomeFeatures.withStrongholdAndMineshaft(builder);
        builder.withStructure(StructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.withCavesAndCanyons(builder);
        //DefaultBiomeFeatures.withLavaAndWaterLakes(builder);
        builder.withFeature(GenerationStage.Decoration.LAKES, Features.LAKE_LAVA.withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(120))));
        DefaultBiomeFeatures.withLightBambooVegetation(builder);
        DefaultBiomeFeatures.withMonsterRoom(builder);
        DefaultBiomeFeatures.withNoiseTallGrass(builder);

        DefaultBiomeFeatures.withCommonOverworldBlocks(builder);
        DefaultBiomeFeatures.withOverworldOres(builder);
        DefaultBiomeFeatures.withDisks(builder);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAKURA_TREE);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.FLOWER_PLAIN_DECORATED);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.RHUBARB_EASTERN_PLAINS);
        //builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.LYCIUM_BARBARUM_EASTERN_PLAINS);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SAUSSUREA_COSTUS_EASTERN_PLAINS);
        //builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.SCHISANDRA_CHINENSIS_EASTERN_PLAINS);
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.PULSATILLA_CHINENSIS_EASTERN_PLAINS);

        DefaultBiomeFeatures.withNormalMushroomGeneration(builder);
        DefaultBiomeFeatures.withSugarCaneAndPumpkins(builder);

        DefaultBiomeFeatures.withLavaAndWaterSprings(builder);
        DefaultBiomeFeatures.withFrozenTopLayer(builder);
    }

    @Override
    public void addSpawns(MobSpawnInfoBuilder builder)
    {
        DefaultBiomeFeatures.withSpawnsWithHorseAndDonkey(builder);
        builder.isValidSpawnBiomeForPlayer();
    }

    @Override
    public void addTypes(List<Type> types)
    {
        types.add(Type.fromVanilla(Biome.Category.PLAINS));
    }
}