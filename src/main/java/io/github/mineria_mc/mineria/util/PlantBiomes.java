package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.common.init.datagen.MineriaBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

import static io.github.mineria_mc.mineria.common.init.MineriaBlocks.*;

/**
 * Client-only utility class for the biomes associated to a plant in the apothecarium.
 */
public class PlantBiomes {
    private static final Map<ResourceLocation, ResourceKey<Biome>[]> PLANT_BIOMES_MAP = new HashMap<>();

    @SafeVarargs
    private static void add(RegistryObject<Block> plant, ResourceKey<Biome>... biomes) {
        PLANT_BIOMES_MAP.put(plant.getId(), biomes);
    }

    @SuppressWarnings("unchecked")
    public static ResourceKey<Biome>[] biomesFor(Block plant) {
        return PLANT_BIOMES_MAP.getOrDefault(ForgeRegistries.BLOCKS.getKey(plant), new ResourceKey[0]);
    }

    static {
        add(PLANTAIN, Biomes.PLAINS);
        add(MINT, Biomes.PLAINS, Biomes.FOREST, Biomes.JUNGLE);
        add(THYME, Biomes.PLAINS, Biomes.SAVANNA, Biomes.WINDSWEPT_HILLS);
        add(NETTLE, Biomes.PLAINS, Biomes.FOREST, Biomes.JUNGLE);
        add(PULMONARY, Biomes.PLAINS, Biomes.FOREST);
        add(RHUBARB, Biomes.PLAINS, Biomes.FOREST, Biomes.BAMBOO_JUNGLE, MineriaBiomes.EASTERN_PLAINS);
        add(SENNA, Biomes.SAVANNA, Biomes.PLAINS, Biomes.WOODED_BADLANDS);
        add(SENNA_BUSH, Biomes.SAVANNA, Biomes.PLAINS, Biomes.WOODED_BADLANDS);
        add(BLACK_ELDERBERRY_BUSH, Biomes.FOREST, Biomes.PLAINS);
        add(ELDERBERRY_BUSH, Biomes.FOREST, Biomes.PLAINS);
        add(BELLADONNA, Biomes.DARK_FOREST, Biomes.FOREST, Biomes.PLAINS);
        add(MANDRAKE, Biomes.DARK_FOREST, Biomes.FOREST, Biomes.PLAINS);
        add(STRYCHNOS_TOXIFERA, Biomes.JUNGLE);
        add(STRYCHNOS_NUX_VOMICA, Biomes.JUNGLE);
        add(LYCIUM_CHINENSE, Biomes.BAMBOO_JUNGLE, MineriaBiomes.EASTERN_PLAINS);
        add(SAUSSUREA_COSTUS, Biomes.BAMBOO_JUNGLE, MineriaBiomes.EASTERN_PLAINS);
        add(SCHISANDRA_CHINENSIS, Biomes.BAMBOO_JUNGLE, MineriaBiomes.EASTERN_PLAINS);
        add(PULSATILLA_CHINENSIS, Biomes.BAMBOO_JUNGLE, MineriaBiomes.EASTERN_PLAINS);
        add(SPRUCE_YEW_SAPLING, Biomes.TAIGA);
    }
}
