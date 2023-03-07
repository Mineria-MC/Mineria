package io.github.mineria_mc.mineria.common.init.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class MineriaStructureSets {
    private static final MineriaBootstrapEntries<StructureSet, Entry> STRUCTURE_SETS = new MineriaBootstrapEntries<>(Registries.STRUCTURE_SET);

    public static final ResourceKey<StructureSet> WIZARD_TOWER = register("wizard_tower", MineriaStructures.WIZARD_TOWER, new RandomSpreadStructurePlacement(25, 8, RandomSpreadType.LINEAR, 35495687));
    public static final ResourceKey<StructureSet> WIZARD_LABORATORY = register("wizard_laboratory", MineriaStructures.WIZARD_LABORATORY, new RandomSpreadStructurePlacement(25, 8, RandomSpreadType.LINEAR, 3216549));
    public static final ResourceKey<StructureSet> PAGODA = register("pagoda", MineriaStructures.PAGODA, new RandomSpreadStructurePlacement(20, 10, RandomSpreadType.LINEAR, 9843516));
    public static final ResourceKey<StructureSet> RITUAL_STRUCTURE = register("ritual_structure", MineriaStructures.RITUAL_STRUCTURE, new RandomSpreadStructurePlacement(24, 10, RandomSpreadType.LINEAR, 65498733));

    private static ResourceKey<StructureSet> register(String name, ResourceKey<Structure> structure, StructurePlacement placement) {
        return STRUCTURE_SETS.register(name, structures -> new StructureSet(structures.getOrThrow(structure), placement));
    }

    public static void bootstrap(BootstapContext<StructureSet> ctx) {
        STRUCTURE_SETS.registerAll(ctx);
    }

    @FunctionalInterface
    private interface Entry extends MineriaBootstrapEntries.Entry<StructureSet> {
        @Override
        default StructureSet create(MineriaBootstrapContext<StructureSet> ctx) {
            return create(ctx.lookup(Registries.STRUCTURE));
        }

        StructureSet create(HolderGetter<Structure> structures);
    }
}
