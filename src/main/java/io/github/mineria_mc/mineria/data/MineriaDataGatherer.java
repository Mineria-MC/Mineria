package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// TODO
public final class MineriaDataGatherer implements Consumer<GatherDataEvent> {
    @Override
    public void accept(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        if (event.includeServer()) {
            MineriaBiomeModifications.addAll();
            RegistrySetBuilder regSetBuilder = new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, MineriaConfiguredFeatures::bootstrap)
                    .add(Registries.PLACED_FEATURE, MineriaPlacements::bootstrap)
                    .add(Registries.TEMPLATE_POOL, MineriaStructurePools::bootstrap)
                    .add(Registries.STRUCTURE, MineriaStructures::bootstrap)
                    .add(Registries.STRUCTURE_SET, MineriaStructureSets::bootstrap)
                    .add(Registries.BIOME, MineriaBiomes::bootstrap)
                    .add(ForgeRegistries.Keys.BIOME_MODIFIERS, MineriaBiomeModifications::makeBiomeModifiers);
            provider = provider.thenApply(p -> regSetBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), p));

            generator.addProvider(true, new DatapackBuiltinEntriesProvider(output, provider, Set.of(Mineria.MODID)));
            MineriaBlockTagsProvider blockTagsProvider = new MineriaBlockTagsProvider(output, provider, helper);
            generator.addProvider(true, blockTagsProvider);
            generator.addProvider(true, new MineriaItemTagsProvider(output, provider, blockTagsProvider, helper));
            generator.addProvider(true, new MineriaEntityTypeTagsProvider(output, provider, helper));
            generator.addProvider(true, new MineriaBiomeTagsProvider(output, provider, helper));
            generator.addProvider(true, new MineriaStructureTagsProvider(output, provider, helper));
            generator.addProvider(true, new MineriaLootTableProvider(output));
            generator.addProvider(true, new MineriaGLMProvider(output));
        }
    }
}
