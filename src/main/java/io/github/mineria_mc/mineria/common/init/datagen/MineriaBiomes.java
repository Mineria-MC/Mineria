package io.github.mineria_mc.mineria.common.init.datagen;

import io.github.mineria_mc.mineria.common.world.biome.EasternPlainsBiomeInfo;
import io.github.mineria_mc.mineria.common.world.biome.MineriaBiomeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Collection;
import java.util.Optional;

public class MineriaBiomes {
    private static final MineriaBootstrapEntries<Biome, MineriaBiomeInfo> BIOMES = new MineriaBootstrapEntries<>(Registries.BIOME);

    public static final ResourceKey<Biome> EASTERN_PLAINS = BIOMES.registerEntry("eastern_plains", EasternPlainsBiomeInfo::new);

    public static Optional<MineriaBiomeInfo> getInfo(ResourceKey<Biome> key) {
        return Optional.ofNullable(BIOMES.getEntryMap().get(key));
    }

    public static Collection<MineriaBiomeInfo> getBiomesInfo() {
        return BIOMES.getEntryMap().values();
    }

    public static void bootstrap(BootstapContext<Biome> ctx) {
        BIOMES.registerAll(ctx);
    }

    public static final class Tags {
        public static final TagKey<Biome> PLANTS_PLAINS = BIOMES.createTag("is_plants_plain");
        public static final TagKey<Biome> PLANTS_FOREST = BIOMES.createTag("is_plants_forest");
        public static final TagKey<Biome> PLANTS_JUNGLE = BIOMES.createTag("is_plants_jungle");
        public static final TagKey<Biome> PLANTS_SAVANNA = BIOMES.createTag("is_plants_savanna");
        public static final TagKey<Biome> PLANTS_HILL = BIOMES.createTag("is_plants_hill");
        public static final TagKey<Biome> PLANTS_BAMBOO_JUNGLE = BIOMES.createTag("is_plants_bamboo_jungle");
        public static final TagKey<Biome> PLANTS_WOODED_BADLANDS = BIOMES.createTag("is_plants_wooded_badlands");
        public static final TagKey<Biome> PLANTS_DARK_FOREST = BIOMES.createTag("is_plants_dark_forest");

        public static final TagKey<Biome> HAS_WIZARD_LABORATORY = BIOMES.createTag("has_structure/wizard_laboratory");
        public static final TagKey<Biome> HAS_WIZARD_TOWER = BIOMES.createTag("has_structure/wizard_tower");
        public static final TagKey<Biome> HAS_PAGODA = BIOMES.createTag("has_structure/pagoda");
        public static final TagKey<Biome> HAS_RITUAL_STRUCTURE = BIOMES.createTag("has_structure/ritual_structure");
    }
}
