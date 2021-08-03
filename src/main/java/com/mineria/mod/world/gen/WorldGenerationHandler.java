package com.mineria.mod.world.gen;

import com.mineria.mod.blocks.StrychnosPlantBlock;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.FeaturesInit;
import com.mineria.mod.init.StructuresInit;
import com.mineria.mod.world.biome.BiomeUtil.BiomeType;
import com.mineria.mod.world.feature.SpruceYewTree;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
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
import java.util.function.Supplier;

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
        generatePlantByType(event, BlocksInit.PLANTAIN.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 100));
        generatePlantByType(event, BlocksInit.MINT.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 60), Pair.of(BiomeType.FOREST, 30), Pair.of(BiomeType.JUNGLE, 10));
        generatePlantByType(event, BlocksInit.THYME.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 20), Pair.of(BiomeType.SAVANNA, 70), Pair.of(BiomeType.MOUNTAINS, 10));
        generatePlantByType(event, BlocksInit.NETTLE.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.FOREST, 25), Pair.of(BiomeType.JUNGLE, 70));
        generatePlantByType(event, BlocksInit.PULMONARY.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 20), Pair.of(BiomeType.FOREST, 80));
        generatePlantByType(event, BlocksInit.RHUBARB.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.PLAINS, 40), Pair.of(BiomeType.PLAINS, 30), Pair.of(BiomeType.BAMBOO_FOREST, 10));
        generatePlantByType(event, BlocksInit.SENNA.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.SAVANNA, 35), Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.WOODED_BADLANDS, 10));
        generateBushByType(event, BlocksInit.SENNA_BUSH.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.SAVANNA, 35), Pair.of(BiomeType.PLAINS, 5), Pair.of(BiomeType.WOODED_BADLANDS, 10));
        generateBushByType(event, BlocksInit.BLACK_ELDERBERRY_BUSH.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.FOREST, 70), Pair.of(BiomeType.PLAINS, 30));
        generatePlantByType(event, BlocksInit.ELDERBERRY_BUSH.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.FOREST, 80), Pair.of(BiomeType.PLAINS, 20));
        generatePlantByType(event, BlocksInit.BELLADONNA.getDefaultState(), PlantRarity.UNCOMMON, Pair.of(BiomeType.DARK_FOREST, 10), Pair.of(BiomeType.FOREST, 60), Pair.of(BiomeType.PLAINS, 30));
        generatePlantByType(event, BlocksInit.MANDRAKE.getDefaultState(), PlantRarity.RARE, Pair.of(BiomeType.DARK_FOREST, 70), Pair.of(BiomeType.FOREST, 20), Pair.of(BiomeType.PLAINS, 10));
        generateVineByType(event, BlocksInit.STRYCHNOS_TOXIFERA.getDefaultState(), PlantRarity.VERY_RARE, Pair.of(BiomeType.MOUNTAINS, 100));
        generateVineByType(event, BlocksInit.STRYCHNOS_NUX_VOMICA.getDefaultState(), PlantRarity.RARE, Pair.of(BiomeType.MOUNTAINS, 100));
        generateBushByType(event, BlocksInit.LYCIUM_BARBARUM.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 60));
        generateDoublePlantByType(event, BlocksInit.SAUSSUREA_COSTUS.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 10));
        generateVineByType(event, BlocksInit.SCHISANDRA_CHINENSIS.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 80));
        generatePlantByType(event, BlocksInit.PULSATILLA_CHINENSIS.getDefaultState(), PlantRarity.COMMON, Pair.of(BiomeType.BAMBOO_FOREST, 30));
    }

    @SafeVarargs
    private static void generatePlantByBiomes(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<RegistryKey<Biome>, Integer>... pairs)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(pairs).forEach(cat -> {
            if(cat.getLeft().getLocation().equals(event.getName()))
            {
                settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(
                        new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer()).tries(64).build())
                        .withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(Math.abs(cat.getRight() - 99) * getCountFromRarity(rarity)).range(16).count(2));
            }
        });
    }

    @SafeVarargs
    private static void generatePlantByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings =  event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(Objects.requireNonNull(event.getName())))
            {
                settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(
                        new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer()).tries(64).build())
                        .withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .chance(Math.abs(type.getRight() - 99) * getCountFromRarity(rarity)).range(16).count(2));
            }
        });
    }

    @SafeVarargs
    private static void generateBushByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {

    }

    @SafeVarargs
    private static void generateVineByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(Objects.requireNonNull(event.getName())))
            {
                settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeaturesInit.MOD_VINES
                        .withConfiguration(new BlockStateFeatureConfig(state.with(StrychnosPlantBlock.AGE, 2)))
                        .square().count(50)
                );
            }
        });
    }

    @SafeVarargs
    private static void generateDoublePlantByType(BiomeLoadingEvent event, BlockState state, PlantRarity rarity, Pair<BiomeType, Integer>... typeChance)
    {
        BiomeGenerationSettingsBuilder settings =  event.getGeneration();

        Arrays.stream(typeChance).forEach(type -> {
            if(type.getLeft().isBiomeValid(Objects.requireNonNull(event.getName())))
            {
                settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(
                        new BlockClusterFeatureConfig.Builder(
                                new SimpleBlockStateProvider(state), new DoublePlantBlockPlacer()).tries(64).preventProjection().build())
                        .withPlacement(Features.Placements.VEGETATION_PLACEMENT)
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
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

    private static void generateOres(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.Category.NETHER))
        {
            generateOre(settings, OreFeatureConfig.FillerBlockType.NETHERRACK, BlocksInit.GOLDEN_SILVERFISH_NETHERRACK.getDefaultState(), 5, 32, 128, 16);
        }
        else if(!event.getCategory().equals(Biome.Category.THEEND))
        {
            if(event.getCategory().equals(Biome.Category.BEACH) || event.getCategory().equals(Biome.Category.DESERT))
                generateOre(settings, new BlockMatchRuleTest(Blocks.SAND), BlocksInit.MINERAL_SAND.getDefaultState(), 8, 40, 70, 12);

            generateOre(settings, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksInit.COPPER_ORE.getDefaultState(), 7, 40, 80, 20);
            generateOre(settings, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksInit.LEAD_ORE.getDefaultState(), 9, 0, 64, 12);
            generateOre(settings, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksInit.SILVER_ORE.getDefaultState(), 9, 0, 32, 4);
            generateOre(settings, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksInit.TITANE_ORE.getDefaultState(), 6, 0, 13, 1);
            generateOre(settings, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlocksInit.LONSDALEITE_ORE.getDefaultState(), 3, 0, 10, 1);
        }
    }

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillerType, BlockState state, int veinSize, int minHeight, int maxHeight, int amount)
    {
        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(fillerType, state, veinSize))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                .square().count(amount));
    }

    private static void generateYewTree(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.Category.TAIGA))
            settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.TREE.withConfiguration(SpruceYewTree.SPRUCE_YEW_TREE).chance(20));
    }

    private static void generateStructures(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(BiomeType.FOREST.isBiomeValid(event.getName()))
            settings.withStructure(StructuresInit.WIZARD_LABORATORY.withConfiguration(NoFeatureConfig.INSTANCE));
    }
}
