package com.mineria.mod.world.biome;

import com.google.common.collect.Sets;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.Set;

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

        private final Set<RegistryKey<Biome>> validBiomes;

        @SafeVarargs
        BiomeType(RegistryKey<Biome>... biomes)
        {
            this.validBiomes = Sets.newHashSet(biomes);
        }

        public boolean isBiomeValid(ResourceLocation biomeName)
        {
            return this.validBiomes.stream().map(RegistryKey::getLocation).anyMatch(biomeName::equals);
        }
    }
}
