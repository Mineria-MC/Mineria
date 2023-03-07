package io.github.mineria_mc.mineria.common.init.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;

public class MineriaStructures {
    private static final MineriaBootstrapEntries<Structure, Entry> STRUCTURES = new MineriaBootstrapEntries<>(Registries.STRUCTURE);

    public static final ResourceKey<Structure> WIZARD_TOWER = registerSingleJigsaw("wizard_tower", MineriaBiomes.Tags.HAS_WIZARD_TOWER, GenerationStep.Decoration.SURFACE_STRUCTURES, true, 0, MineriaStructurePools.WIZARD_TOWER);
    public static final ResourceKey<Structure> WIZARD_LABORATORY = registerSingleJigsaw("wizard_laboratory", MineriaBiomes.Tags.HAS_WIZARD_LABORATORY, GenerationStep.Decoration.SURFACE_STRUCTURES, false, -8, MineriaStructurePools.WIZARD_LABORATORY);
    public static final ResourceKey<Structure> PAGODA = registerSingleJigsaw("pagoda", MineriaBiomes.Tags.HAS_PAGODA, GenerationStep.Decoration.SURFACE_STRUCTURES, true, 0, MineriaStructurePools.PAGODA);
    public static final ResourceKey<Structure> RITUAL_STRUCTURE = registerSingleJigsaw("ritual_structure", MineriaBiomes.Tags.HAS_RITUAL_STRUCTURE, GenerationStep.Decoration.SURFACE_STRUCTURES, true, 0, MineriaStructurePools.RITUAL_STRUCTURE);

    private static ResourceKey<Structure> registerSingleJigsaw(String name, TagKey<Biome> validBiomes, GenerationStep.Decoration step, boolean beardify, int offsetHeight, ResourceKey<StructureTemplatePool> pool) {
        return STRUCTURES.register(name, (biomes, pools) -> new JigsawStructure(
                new Structure.StructureSettings(biomes.getOrThrow(validBiomes), Map.of(), step, beardify ? TerrainAdjustment.BEARD_BOX : TerrainAdjustment.NONE),
                pools.getOrThrow(pool),
                1,
                ConstantHeight.of(VerticalAnchor.absolute(offsetHeight)),
                false,
                Heightmap.Types.WORLD_SURFACE_WG
        ));
    }

    public static void bootstrap(BootstapContext<Structure> ctx) {
        STRUCTURES.registerAll(ctx);
    }

    @FunctionalInterface
    private interface Entry extends MineriaBootstrapEntries.Entry<Structure> {
        @Override
        default Structure create(MineriaBootstrapContext<Structure> ctx) {
            return create(ctx.lookup(Registries.BIOME), ctx.lookup(Registries.TEMPLATE_POOL));
        }

        Structure create(HolderGetter<Biome> biomes, HolderGetter<StructureTemplatePool> pools);
    }

    public static class Tags {
        public static final TagKey<Structure> ON_RITUAL_STRUCTURE_MAPS = STRUCTURES.createTag("on_ritual_structure_maps");
    }
}
