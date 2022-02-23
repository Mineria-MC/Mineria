package com.mineria.mod.common.world.biome;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import javax.annotation.Nullable;

public class BiomeUtil
{
    public enum BiomeType
    {
        PLAINS(Biomes.PLAINS),
        FOREST(Biomes.FOREST),
        BIRCH_FOREST(Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS),
        DARK_FOREST(Biomes.DARK_FOREST, Biomes.DARK_FOREST_HILLS),
        BAMBOO_FOREST(Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE_HILLS),
        SAVANNA(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU),
        WOODED_BADLANDS(Biomes.WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU),
        JUNGLE(Biomes.JUNGLE, Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE),
        MOUNTAINS(Biomes.MOUNTAINS, Biomes.MOUNTAIN_EDGE, Biomes.GRAVELLY_MOUNTAINS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.TAIGA_MOUNTAINS, Biomes.WOODED_MOUNTAINS);

        private final ImmutableSet<ResourceKey<Biome>> validBiomes;

        @SafeVarargs
        BiomeType(ResourceKey<Biome>... biomes)
        {
            this.validBiomes = ImmutableSet.copyOf(biomes);
        }

        public boolean isBiomeValid(@Nullable ResourceLocation biomeName)
        {
            return biomeName != null && this.validBiomes.stream().map(ResourceKey::location).anyMatch(biomeName::equals);
        }
    }
}
