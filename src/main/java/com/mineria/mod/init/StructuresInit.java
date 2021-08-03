package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.world.feature.structure.WizardLaboratoryStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;

import java.util.Map;
import java.util.function.Predicate;

public class StructuresInit
{
    // TODO Fix Structure generation, data markers
    public static final Structure<NoFeatureConfig> WIZARD_LABORATORY = new WizardLaboratoryStructure();

    public static void registerStructures(RegistryEvent.Register<Structure<?>> event)
    {
        registerStructure(event, WIZARD_LABORATORY, WizardLaboratoryStructure.getPieces(), settings -> settings.func_242744_a(DimensionSettings.OVERWORLD), new StructureSeparationSettings(25, 8, 256));
    }

    private static void registerStructure(RegistryEvent.Register<Structure<?>> event, Structure<?> structure, Map<String, IStructurePieceType> pieces, Predicate<DimensionSettings> doesGenerate, StructureSeparationSettings separationSettings)
    {
        event.getRegistry().register(structure);
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);
        pieces.forEach((name, piece) -> Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(References.MODID, structure.getRegistryName().getPath().concat("_".concat(name))), piece));
        WorldGenRegistries.NOISE_SETTINGS.forEach(dim -> {
            //if(doesGenerate.test(dim))
                dim.getStructures().func_236195_a_().put(structure, separationSettings);
        });
    }
}
