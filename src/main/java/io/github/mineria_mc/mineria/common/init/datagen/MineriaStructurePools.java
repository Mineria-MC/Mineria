package io.github.mineria_mc.mineria.common.init.datagen;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaDataMarkerHandlers;
import io.github.mineria_mc.mineria.common.world.feature.structure.pool_elements.SingleDataPoolElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.function.Function;

public class MineriaStructurePools {
    private static final MineriaBootstrapEntries<StructureTemplatePool, Entry> TEMPLATE_POOLS = new MineriaBootstrapEntries<>(Registries.TEMPLATE_POOL);

    private static final ResourceKey<StructureProcessorList> EMPTY = ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("empty"));

    public static final ResourceKey<StructureTemplatePool> WIZARD_TOWER = register("wizard_tower", Pools.EMPTY, processors -> List.of(Pair.of(
            new SingleDataPoolElement(
                    Either.left(new ResourceLocation(Mineria.MODID, "wizard_tower")),
                    processors.getOrThrow(EMPTY),
                    StructureTemplatePool.Projection.RIGID,
                    MineriaDataMarkerHandlers.WIZARD_TOWER.get()
            ), 1
    )));
    public static final ResourceKey<StructureTemplatePool> WIZARD_LABORATORY = register("wizard_laboratory", Pools.EMPTY, processors -> List.of(Pair.of(
            new SingleDataPoolElement(
                    Either.left(new ResourceLocation(Mineria.MODID, "wizard_laboratory")),
                    processors.getOrThrow(EMPTY),
                    StructureTemplatePool.Projection.RIGID,
                    MineriaDataMarkerHandlers.WIZARD_LABORATORY.get()
            ), 1
    )));
    public static final ResourceKey<StructureTemplatePool> PAGODA = register("pagoda", Pools.EMPTY, processors -> List.of(Pair.of(
            new SingleDataPoolElement(
                    Either.left(new ResourceLocation(Mineria.MODID, "pagoda")),
                    processors.getOrThrow(EMPTY),
                    StructureTemplatePool.Projection.RIGID,
                    MineriaDataMarkerHandlers.PAGODA.get()
            ).setGroundLevelDelta(0), 1
    )));
    public static final ResourceKey<StructureTemplatePool> RITUAL_STRUCTURE = register("ritual_structure", Pools.EMPTY, processors -> List.of(Pair.of(
            new SingleDataPoolElement(
                    Either.left(new ResourceLocation(Mineria.MODID, "ritual_structure")),
                    processors.getOrThrow(EMPTY),
                    StructureTemplatePool.Projection.RIGID,
                    MineriaDataMarkerHandlers.RITUAL_STRUCTURE.get()
            ).setRotation(Rotation.NONE), 1
    )));

    private static ResourceKey<StructureTemplatePool> register(String name, ResourceKey<StructureTemplatePool> fallback, Function<HolderGetter<StructureProcessorList>, List<Pair<StructurePoolElement, Integer>>> elements) {
        return TEMPLATE_POOLS.register(name, (pools, processorLists) -> new StructureTemplatePool(pools.getOrThrow(fallback), elements.apply(processorLists)));
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> ctx) {
        TEMPLATE_POOLS.registerAll(ctx);
    }

    @FunctionalInterface
    private interface Entry extends MineriaBootstrapEntries.Entry<StructureTemplatePool> {
        @Override
        default StructureTemplatePool create(MineriaBootstrapContext<StructureTemplatePool> ctx) {
            return create(ctx.lookup(Registries.TEMPLATE_POOL), ctx.lookup(Registries.PROCESSOR_LIST));
        }

        StructureTemplatePool create(HolderGetter<StructureTemplatePool> pools, HolderGetter<StructureProcessorList> processorLists);
    }
}
