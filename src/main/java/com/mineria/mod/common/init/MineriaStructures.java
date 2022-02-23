package com.mineria.mod.common.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.world.feature.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MineriaStructures
{
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Mineria.MODID);

    // TODO
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> WIZARD_LABORATORY = STRUCTURES.register("wizard_laboratory", WizardLaboratoryStructure::new);
//    public static final StructurePieceType WLP = StructurePieceType.setPieceId(WizardLaboratoryPiece::new, "mineria:wizard_laboratory_main_piece");
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> WIZARD_TOWER = STRUCTURES.register("wizard_tower", WizardTowerStructure::new);
//    public static final StructurePieceType WTP = StructurePieceType.setPieceId(WizardTowerPiece::new, "mineria:wizard_tower_main_piece");
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> PAGODA = STRUCTURES.register("pagoda", PagodaStructure::new);
//    public static final StructurePieceType PMP = StructurePieceType.setPieceId(PagodaPiece::new, "mineria:pagoda_main_piece");
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> RITUAL_STRUCTURE = STRUCTURES.register("ritual_structure", RitualStructure::new);
//    public static final StructurePieceType RSP = StructurePieceType.setPieceId(RitualStructurePiece::new, "mineria:ritual_structure_piece");

    /*
    public static void registerStructures(RegistryEvent.Register<Structure<?>> event)
    {
        registerStructure(event, WIZARD_LABORATORY, WizardLaboratoryStructure.getPieces(), settings -> settings.stable(DimensionSettings.OVERWORLD), new StructureSeparationSettings(25, 8, 256));
    }

    private static void registerStructure(RegistryEvent.Register<Structure<?>> event, Structure<?> structure, Map<String, IStructurePieceType> pieces, Predicate<DimensionSettings> doesGenerate, StructureSeparationSettings separationSettings)
    {
        event.getRegistry().register(structure);
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        pieces.forEach((name, piece) -> Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(References.MODID, structure.getRegistryName().getPath().concat("_".concat(name))), piece));
        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.forEach(dim -> {
            //if(doesGenerate.test(dim))
                dim.structureSettings().structureConfig().put(structure, separationSettings);
        });
    }*/

    public static void setupStructures()
    {
        setupMapSpacingAndLand(WIZARD_LABORATORY.get(), new StructureFeatureConfiguration(25, 8, 3216549), false);
        setupMapSpacingAndLand(WIZARD_TOWER.get(), new StructureFeatureConfiguration(25, 8, 35495687), true);
        setupMapSpacingAndLand(PAGODA.get(), new StructureFeatureConfiguration(20, 10, 9843516), true);
        setupMapSpacingAndLand(RITUAL_STRUCTURE.get(), new StructureFeatureConfiguration(24, 10, 65498733), true);
    }

    public static <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure, StructureFeatureConfiguration separationSettings, boolean transformSurroundingLand)
    {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand)
        {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder().addAll(StructureFeature.NOISE_AFFECTING_FEATURES).add(structure).build();
        }

        StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).put(structure, separationSettings).build();

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap)
            {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, separationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else
                structureMap.put(structure, separationSettings);
        });
    }

    public static final class Configured
    {
        public static final ConfiguredStructureFeature<?, ?> CONFIGURED_WIZARD_LABORATORY = MineriaStructures.WIZARD_LABORATORY.get().configured(FeatureConfiguration.NONE);
        public static final ConfiguredStructureFeature<?, ?> CONFIGURED_WIZARD_TOWER = MineriaStructures.WIZARD_TOWER.get().configured(FeatureConfiguration.NONE);
        public static final ConfiguredStructureFeature<?, ?> CONFIGURED_PAGODA = MineriaStructures.PAGODA.get().configured(FeatureConfiguration.NONE);
        public static final ConfiguredStructureFeature<?, ?> CONFIGURED_RITUAL_STRUCTURE = MineriaStructures.RITUAL_STRUCTURE.get().configured(FeatureConfiguration.NONE);

        public static void registerConfiguredStructures()
        {
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_wizard_laboratory"), CONFIGURED_WIZARD_LABORATORY);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_wizard_tower"), CONFIGURED_WIZARD_TOWER);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_pagoda"), CONFIGURED_PAGODA);
            Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_ritual_structure"), CONFIGURED_RITUAL_STRUCTURE);

            FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(MineriaStructures.WIZARD_LABORATORY.get(), CONFIGURED_WIZARD_LABORATORY);
            FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(MineriaStructures.WIZARD_TOWER.get(), CONFIGURED_WIZARD_TOWER);
            FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(MineriaStructures.PAGODA.get(), CONFIGURED_PAGODA);
            FlatLevelGeneratorSettings.STRUCTURE_FEATURES.put(MineriaStructures.RITUAL_STRUCTURE.get(), CONFIGURED_RITUAL_STRUCTURE);
        }
    }
}
