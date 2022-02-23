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
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Objects;

// TODO
public class WorldGenerationHandler
{
    public static void loadVanillaBiomes(BiomeLoadingEvent event)
    {
        generateOres(event);
        generatePlants(event);
        generateYewTree(event);
//        generateStructures(event); TODO
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
        generateFeaturesForBiomes(event, GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.GIROLLE_BASE.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
        generateFeaturesForBiomes(event, GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.HORN_OF_PLENTY_BASE.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
        generateFeaturesForBiomes(event, GenerationStep.Decoration.VEGETAL_DECORATION, MineriaConfiguredFeatures.PUFFBALL_BASE.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(4),
                Biomes.PLAINS, Biomes.GIANT_TREE_TAIGA, Biomes.BIRCH_FOREST, Biomes.JUNGLE, Biomes.MOUNTAINS, Biomes.DESERT, Biomes.MUSHROOM_FIELDS,
                Biomes.SAVANNA, Biomes.BADLANDS, Biomes.OCEAN, Biomes.FROZEN_OCEAN, Biomes.FOREST, Biomes.TAIGA, Biomes.DARK_FOREST, Biomes.SWAMP,
                Biomes.SNOWY_TUNDRA, Biomes.RIVER, Biomes.BEACH);
    }

    @SafeVarargs
    private static void generatePlantByBiomes(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<ResourceKey<Biome>, Integer>... pairs)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(pairs).forEach(category -> {
            if(category.getLeft().location().equals(event.getName()))
            {
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.FLOWER.configured(
                        new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(state), new SimpleBlockPlacer()).tries(32).build())
                        .decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .rarity(Math.abs(category.getRight() - 99) * getCountFromRarity(rarity)).count(2));
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
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.FLOWER.configured(
                        new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(state), new SimpleBlockPlacer()).tries(32).build())
                        .decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .rarity(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity)).count(2));
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
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(
                        new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(state), new SimpleBlockPlacer())
                                .tries(16).xspread(3).yspread(2).zspread(3).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, state.getBlock())).build())
                        .decorated(Features.Decorators.ADD_32).decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .rarity(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity) * 2).squared()
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
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaFeatures.MOD_VINES
                        .configured(new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
                        .squared().rarity(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity) * 2)
                );
            }
        });
    }

    private static void generateBambooForestVine(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, int chance, int count)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(BiomeType.BAMBOO_FOREST.isBiomeValid(event.getName()))
        {
            settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MineriaFeatures.MOD_VINES
                    .configured(new ModVinesFeatureConfig(state, 3, 10, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST))
                    .squared().rarity(Math.abs(chance - 99) * getCountFromRarity(rarity)).count(count)
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
                settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(
                        new RandomPatchConfiguration.GrassConfigurationBuilder(
                                new SimpleStateProvider(state), new DoublePlantPlacer()).tries(64).noProjection().build())
                        .decorated(Features.Decorators.ADD_32)
                        .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
                        .rarity(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity)).count(2));
            }
        });
    }

    private static int getCountFromRarity(PlantRarity rarity)
    {
        return switch (rarity)
                {
                    case COMMON -> 1;
                    case UNCOMMON -> 3;
                    case RARE -> 5;
                    case VERY_RARE -> 8;
                };
    }

    @SafeVarargs
    private static void generateFeaturesForBiomes(BiomeLoadingEvent event, GenerationStep.Decoration decorationStage, ConfiguredFeature<?, ?> feature, ResourceKey<Biome>... biomes)
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

        if(event.getCategory().equals(Biome.BiomeCategory.NETHER))
        {
            generateOre(settings, OreConfiguration.Predicates.NETHERRACK, MineriaBlocks.GOLDEN_SILVERFISH_NETHERRACK.defaultBlockState(), 5, 32, 128, 16);
        }
        else if(!event.getCategory().equals(Biome.BiomeCategory.THEEND))
        {
            if(event.getCategory().equals(Biome.BiomeCategory.BEACH) || event.getCategory().equals(Biome.BiomeCategory.DESERT))
                generateOre(settings, new BlockMatchTest(Blocks.SAND), MineriaBlocks.MINERAL_SAND.defaultBlockState(), 8, 40, 70, 12);

            generateOre(settings, OreConfiguration.Predicates.NATURAL_STONE, MineriaBlocks.COPPER_ORE.defaultBlockState(), 7, 40, 80, 20);
            generateOre(settings, OreConfiguration.Predicates.NATURAL_STONE, MineriaBlocks.LEAD_ORE.defaultBlockState(), 9, 0, 64, 12);
            generateOre(settings, OreConfiguration.Predicates.NATURAL_STONE, MineriaBlocks.SILVER_ORE.defaultBlockState(), 9, 0, 32, 4);
            generateOre(settings, OreConfiguration.Predicates.NATURAL_STONE, MineriaBlocks.TITANE_ORE.defaultBlockState(), 6, 0, 13, 1);
            generateOre(settings, OreConfiguration.Predicates.NATURAL_STONE, MineriaBlocks.LONSDALEITE_ORE.defaultBlockState(), 3, 0, 10, 1);
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int amount)
    {
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreConfiguration(fillerType, state, veinSize))
                .decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.absolute(minHeight), VerticalAnchor.absolute(maxHeight)))))
                .squared().count(amount));
    }

    private static void generateYewTree(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.BiomeCategory.TAIGA))
            settings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.TREE.configured(SpruceYewTree.SPRUCE_YEW_TREE.get()).rarity(20));
    }

    private static void generateStructures(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(BiomeType.FOREST.isBiomeValid(event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_WIZARD_LABORATORY);
        if(!event.getCategory().equals(Biome.BiomeCategory.NETHER) && !event.getCategory().equals(Biome.BiomeCategory.THEEND) && !event.getCategory().equals(Biome.BiomeCategory.OCEAN) && !event.getCategory().equals(Biome.BiomeCategory.RIVER))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_WIZARD_TOWER);
        if(Objects.equals(MineriaBiomes.EASTERN_PLAINS.getBiome().getRegistryName(), event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_PAGODA);
        if(BiomeType.DARK_FOREST.isBiomeValid(event.getName()))
            settings.addStructureStart(MineriaStructures.Configured.CONFIGURED_RITUAL_STRUCTURE);
    }
}
