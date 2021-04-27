package com.mineria.mod.util;

import com.mineria.mod.References;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldGenerationHandler
{
    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event)
    {
        generateOres(event);
        generatePlants(event);
    }

    private static void generatePlants(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder settings = event.getGeneration();

        if(event.getCategory().equals(Biome.Category.PLAINS))
        {
            generatePlant(settings, BlocksInit.PLANTAIN.getDefaultState(), 100);
            generatePlant(settings, BlocksInit.MINT.getDefaultState(), 60);
            generatePlant(settings, BlocksInit.THYME.getDefaultState(), 20);
            generatePlant(settings, BlocksInit.NETTLE.getDefaultState(), 5);
            generatePlant(settings, BlocksInit.PULMONARY.getDefaultState(), 80);
        }
        else if(event.getCategory().equals(Biome.Category.FOREST))
        {
            generatePlant(settings, BlocksInit.MINT.getDefaultState(), 30);
            generatePlant(settings, BlocksInit.NETTLE.getDefaultState(), 25);
            generatePlant(settings, BlocksInit.PULMONARY.getDefaultState(), 80);
        }
        else if(event.getCategory().equals(Biome.Category.JUNGLE))
        {
            generatePlant(settings, BlocksInit.MINT.getDefaultState(), 10);
            generatePlant(settings, BlocksInit.NETTLE.getDefaultState(), 70);
        }
        else if(event.getCategory().equals(Biome.Category.SAVANNA))
            generatePlant(settings, BlocksInit.THYME.getDefaultState(), 70);

        else if(event.getCategory().equals(Biome.Category.EXTREME_HILLS))
            generatePlant(settings, BlocksInit.THYME.getDefaultState(), 10);
    }

    private static void generatePlant(BiomeGenerationSettingsBuilder settings, BlockState state, int chance)
    {
        settings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(
                new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(state), new SimpleBlockPlacer()).tries(64).build())
        .withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                .chance(Math.abs(chance - 99)).range(16).count(2));
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
}
