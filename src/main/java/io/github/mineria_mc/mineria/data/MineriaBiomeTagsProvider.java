package io.github.mineria_mc.mineria.data;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

import static io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes.EASTERN_PLAINS;
import static io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes.Tags.*;
import static net.minecraft.tags.BiomeTags.*;
import static net.minecraftforge.common.Tags.Biomes.*;

public class MineriaBiomeTagsProvider extends BiomeTagsProvider {
    public MineriaBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, Mineria.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        bind(EASTERN_PLAINS, IS_OVERWORLD, IS_PLAINS,
                HAS_MINESHAFT, HAS_RUINED_PORTAL_STANDARD, HAS_STRONGHOLD);

        tag(PLANTS_PLAINS).add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS);
        tag(PLANTS_FOREST).add(Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.FLOWER_FOREST);
        tag(PLANTS_JUNGLE).add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE);
        tag(PLANTS_SAVANNA).add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        tag(PLANTS_HILL).add(Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
        tag(PLANTS_BAMBOO_JUNGLE).add(Biomes.BAMBOO_JUNGLE);
        tag(PLANTS_WOODED_BADLANDS).add(Biomes.WOODED_BADLANDS);
        tag(PLANTS_DARK_FOREST).add(Biomes.DARK_FOREST);

        tag(HAS_PAGODA).add(EASTERN_PLAINS);
        tag(HAS_RITUAL_STRUCTURE).add(Biomes.DARK_FOREST);
        tag(HAS_WIZARD_LABORATORY).add(Biomes.FOREST, Biomes.BIRCH_FOREST, Biomes.FLOWER_FOREST, Biomes.WINDSWEPT_FOREST);
        tag(HAS_WIZARD_TOWER).addTags(IS_PLAINS, IS_DESERT, IS_SAVANNA, IS_FOREST, IS_SWAMP, IS_BEACH, IS_SNOWY, IS_BADLANDS, IS_JUNGLE, IS_TAIGA, BiomeTags.IS_MOUNTAIN, IS_SLOPE, IS_PEAK);
    }

    @SafeVarargs
    private void bind(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
        for (TagKey<Biome> tag : tags) {
            tag(tag).add(biome);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "Mineria Biome Tags";
    }
}
