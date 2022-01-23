package com.mineria.mod.common.world.gen;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.common.blocks.StrychnosPlantBlock;
import com.mineria.mod.common.init.MineriaBiomes;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaFeatures;
import com.mineria.mod.common.init.MineriaStructures;
import com.mineria.mod.common.world.biome.BiomeUtil.BiomeType;
import com.mineria.mod.common.world.feature.MineriaConfiguredFeatures;
import com.mineria.mod.common.world.feature.ModVinesFeatureConfig;
import com.mineria.mod.common.world.feature.SpruceYewTree;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Objects;

public class WorldGenerationHandler
{
    public static void loadVanillaBiomes(BiomeLoadingEvent event)
    {
        generateOres(event);
        generatePlants(event);
        generateYewTree(event);
        generateStructures(event);
    }

    private enum PlantRarity { COMMON, UNCOMMON, RARE, VERY_RARE }

    private static void generatePlants(BiomeLoadingEvent event)
    {
        generatePlantByType(event, MineriaBlocks.PLANTAIN.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 100));
        generatePlantByType(event, MineriaBlocks.MINT.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 60), Pair.of(BiomeType.FOREST, 30), Pair.of(BiomeType.JUNGLE, 10));
        generatePlantByType(event, MineriaBlocks.THYME.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 20), Pair.of(BiomeType.SAVANNA, 70), Pair.of(BiomeType.MOUNTAINS, 10));
        generatePlantByType(event, MineriaBlocks.NETTLE.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.FOREST, 25), Pair.of(BiomeType.JUNGLE, 70));
        generatePlantByType(event, MineriaBlocks.PULMONARY.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 20), Pair.of(BiomeType.FOREST, 80));
        generatePlantByType(event, MineriaBlocks.RHUBARB.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 40), Pair.of(BiomeType.PLAINS, 30), Pair.of(BiomeType.BAMBOO_FOREST, 10));
        generatePlantByType(event, MineriaBlocks.SENNA.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.SAVANNA, 35), Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.WOODED_BADLANDS, 10));
        generateBushByType(event, MineriaBlocks.SENNA_BUSH.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.SAVANNA, 35), Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.WOODED_BADLANDS, 10));
        generateBushByType(event, MineriaBlocks.BLACK_ELDERBERRY_BUSH.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.FOREST, 70), Pair.of(BiomeType.PLAINS, 30));
        generateBushByType(event, MineriaBlocks.ELDERBERRY_BUSH.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.FOREST, 80), Pair.of(BiomeType.PLAINS, 20));
        generatePlantByType(event, MineriaBlocks.BELLADONNA.defaultBlockState(), PlantRarity.UNCOMMON, Pair.of(BiomeType.DARK_FOREST, 10), Pair.of(BiomeType.FOREST, 60), Pair.of(BiomeType.PLAINS, 30));
        generatePlantByType(event, MineriaBlocks.MANDRAKE.defaultBlockState(), PlantRarity.RARE, Pair.of(BiomeType.DARK_FOREST, 70), Pair.of(BiomeType.FOREST, 20), Pair.of(BiomeType.PLAINS, 10));
        generateVineByType(event, MineriaBlocks.STRYCHNOS_TOXIFERA.defaultBlockState().setValue(StrychnosPlantBlock.AGE, 2), PlantRarity.VERY_RARE, Pair.of(BiomeType.JUNGLE, 100));
        generateVineByType(event, MineriaBlocks.STRYCHNOS_NUX_VOMICA.defaultBlockState().setValue(StrychnosPlantBlock.AGE, 2), PlantRarity.RARE, Pair.of(BiomeType.JUNGLE, 100));
        generateBushByType(event, MineriaBlocks.LYCIUM_CHINENSE.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 60));
        generateDoublePlantByType(event, MineriaBlocks.SAUSSUREA_COSTUS.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 10));
        generateBambooForestVine(event, MineriaBlocks.SCHISANDRA_CHINENSIS.defaultBlockState(), PlantRarity.COMMON, 80, 32);
        generatePlantByType(event, MineriaBlocks.PULSATILLA_CHINENSIS.defaultBlockState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 30));
        generateFeaturesForBiomes(event, GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.GIROLLE_BASE.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
        generateFeaturesForBiomes(event, GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.HORN_OF_PLENTY_BASE.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
        generateFeaturesForBiomes(event, GenerationStage.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.PUFFBALL_BASE.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
    }

    @SafeVarargs
    private static void generatePlantByBiomes(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<RegistryKey<Biome>, Integer>... pairs)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(pairs).forEach(category -> {
            if(category.getLeft().location().equals(event.getName()))
            {
                settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.configured(
                        new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer()).tries(32).build())
                        .decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .chance(Math.abs(category.getRight() - 99) * getCountFromRarity(rarity)).range(16).count(2));
            }
        });
    }

    @SafeVarargs
    private static void generatePlantByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(Objects.requireNonNull(event.getName())))
            {
                settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.configured(
                        new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer()).tries(32).build())
                        .decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .chance(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity)).range(16).count(2));
            }
        });
    }

    @SafeVarargs
    private static void generateBushByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(event.getName()))
            {
                settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(
                        new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer())
                                .tries(16).xspread(3).yspread(2).zspread(3).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, state.getBlock())).build())
                        .decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .chance(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity) * 2).range(32).squared()
                );
            }
        });
    }

    @SafeVarargs
    private static void generateVineByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(event.getName()))
            {
                settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaFeatures.MOD_VINES
                        .configured(new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
                        .squared().chance(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity) * 2)
                );
            }
        });
    }

    private static void generateBambooForestVine(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, int chance, int count)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(BiomeType.BAMBOO_FOREST.isBiomeValid(event.getName()))
        {
            settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MineriaFeatures.MOD_VINES
                    .configured(new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
                    .squared().chance(Math.abs(chance - 99) * getCountFromRarity(rarity)).range(16).count(count)
            );
        }
    }

    @SafeVarargs
    private static void generateDoublePlantByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(Objects.requireNonNull(event.getName())))
            {
                settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(
                        new BlockClusterFeatureConfig.Builder(
                                new SimpleBlockStateProvider(state), new DoublePlantBlockPlacer()).tries(64).noProjection().build())
                        .decorated(Features.Placements.ADD_32)
                        .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .chance(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity)).range(16).count(2));
            }
        });
    }

    private static int getCountFromRarity(PlantRarity rarity)
    {
        switch (rarity)
        {
            case COMMON:
                return 1;
            case UNCOMMON:
                return 3;
            case RARE:
                return 5;
            case VERY_RARE:
                return 8;
        }
        return 0;
    }

    @SafeVarargs
    private static void generateFeaturesForBiomes(BiomeLoadingEvent event, GenerationStage.Decoration decorationStage, ConfiguredFeature<?, ?> feature, RegistryKey<Biome>... biomes)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(biomes).forEach(key -> {
            if(key.location().equals(event.getName()))
            {
                settings.addFeature(decorationStage, feature);
            }
        });
    }

    private static void generateOres(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.Category.NETHER))
        {
            generateOre(settings, OreFeatureConfig.FillerBlockType.NETHERRACK, MineriaBlocks.GOLDEN_SILVERFISH_NETHERRACK.defaultBlockState(), 5, 32, 128, 16);
        }
        else if(!event.getCategory().equals(Biome.Category.THEEND))
        {
            if(event.getCategory().equals(Biome.Category.BEACH) || event.getCategory().equals(Biome.Category.DESERT))
                generateOre(settings, new BlockMatchRuleTest(Blocks.SAND), MineriaBlocks.MINERAL_SAND.defaultBlockState(), 8, 40, 70, 12);

            generateOre(settings, OreFeatureConfig.FillerBlockType.NATURAL_STONE, MineriaBlocks.COPPER_ORE.defaultBlockState(), 7, 40, 80, 20);
            generateOre(settings, OreFeatureConfig.FillerBlockType.NATURAL_STONE, MineriaBlocks.LEAD_ORE.defaultBlockState(), 9, 0, 64, 12);
            generateOre(settings, OreFeatureConfig.FillerBlockType.NATURAL_STONE, MineriaBlocks.SILVER_ORE.defaultBlockState(), 9, 0, 32, 4);
            generateOre(settings, OreFeatureConfig.FillerBlockType.NATURAL_STONE, MineriaBlocks.TITANE_ORE.defaultBlockState(), 6, 0, 13, 1);
            generateOre(settings, OreFeatureConfig.FillerBlockType.NATURAL_STONE, MineriaBlocks.LONSDALEITE_ORE.defaultBlockState(), 3, 0, 10, 1);
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int amount)
    {
        settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(fillerType, state, veinSize))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                .squared().count(amount));
    }

    private static void generateYewTree(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.Category.TAIGA))
            settings.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.TREE.configured(SpruceYewTree.SPRUCE_YEW_TREE).chance(20));
    }

    private static void generateStructures(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(BiomeType.FOREST.isBiomeValid(event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_WIZARD_LABORATORY);
        if(!event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND) && !event.getCategory().equals(Biome.Category.OCEAN) && !event.getCategory().equals(Biome.Category.RIVER))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_WIZARD_TOWER);
        if(Objects.equals(MineriaBiomes.EASTERN_PLAINS.getBiome().getRegistryName(), event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_PAGODA);
        if(BiomeType.DARK_FOREST.isBiomeValid(event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_RITUAL_STRUCTURE);
    }
}
