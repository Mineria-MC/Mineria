package com.mineria.mod.common.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.world.feature.structure.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MineriaStructures
{
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Mineria.MODID);

    public static final RegistryObject<Structure<NoFeatureConfig>> WIZARD_LABORATORY = STRUCTURES.register("wizard_laboratory", WizardLaboratoryStructure::new);
    public static final IStructurePieceType WLP = IStructurePieceType.setPieceId(WizardLaboratoryPiece::new, "mineria:wizard_laboratory_main_piece");
    public static final RegistryObject<Structure<NoFeatureConfig>> WIZARD_TOWER = STRUCTURES.register("wizard_tower", WizardTowerStructure::new);
    public static final IStructurePieceType WTP = IStructurePieceType.setPieceId(WizardTowerPiece::new, "mineria:wizard_tower_main_piece");
    public static final RegistryObject<Structure<NoFeatureConfig>> PAGODA = STRUCTURES.register("pagoda", PagodaStructure::new);
    public static final IStructurePieceType PMP = IStructurePieceType.setPieceId(PagodaPiece::new, "mineria:pagoda_main_piece");
    public static final RegistryObject<Structure<NoFeatureConfig>> RITUAL_STRUCTURE = STRUCTURES.register("ritual_structure", RitualStructure::new);
    public static final IStructurePieceType RSP = IStructurePieceType.setPieceId(RitualStructurePiece::new, "mineria:ritual_structure_piece");

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
        setupMapSpacingAndLand(WIZARD_LABORATORY.get(), new StructureSeparationSettings(25, 8, 3216549), false);
        setupMapSpacingAndLand(WIZARD_TOWER.get(), new StructureSeparationSettings(25, 8, 35495687), true);
        setupMapSpacingAndLand(PAGODA.get(), new StructureSeparationSettings(20, 10, 9843516), true);
        setupMapSpacingAndLand(RITUAL_STRUCTURE.get(), new StructureSeparationSettings(24, 10, 65498733), true);
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings separationSettings, boolean transformSurroundingLand)
    {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand)
        {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
        }

        DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, separationSettings).build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap)
            {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, separationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else
                structureMap.put(structure, separationSettings);
        });
    }

    public static final class Configured
    {
        public static final StructureFeature<?, ?> CONFIGURED_WIZARD_LABORATORY = MineriaStructures.WIZARD_LABORATORY.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> CONFIGURED_WIZARD_TOWER = MineriaStructures.WIZARD_TOWER.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> CONFIGURED_PAGODA = MineriaStructures.PAGODA.get().configured(IFeatureConfig.NONE);
        public static final StructureFeature<?, ?> CONFIGURED_RITUAL_STRUCTURE = MineriaStructures.RITUAL_STRUCTURE.get().configured(IFeatureConfig.NONE);

        public static void registerConfiguredStructures()
        {
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_wizard_laboratory"), CONFIGURED_WIZARD_LABORATORY);
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_wizard_tower"), CONFIGURED_WIZARD_TOWER);
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_pagoda"), CONFIGURED_PAGODA);
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(Mineria.MODID, "configured_ritual_structure"), CONFIGURED_RITUAL_STRUCTURE);

            FlatGenerationSettings.STRUCTURE_FEATURES.put(MineriaStructures.WIZARD_LABORATORY.get(), CONFIGURED_WIZARD_LABORATORY);
            FlatGenerationSettings.STRUCTURE_FEATURES.put(MineriaStructures.WIZARD_TOWER.get(), CONFIGURED_WIZARD_TOWER);
            FlatGenerationSettings.STRUCTURE_FEATURES.put(MineriaStructures.PAGODA.get(), CONFIGURED_PAGODA);
            FlatGenerationSettings.STRUCTURE_FEATURES.put(MineriaStructures.RITUAL_STRUCTURE.get(), CONFIGURED_RITUAL_STRUCTURE);
        }
    }
}
