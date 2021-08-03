package com.mineria.mod.init;

import com.mineria.mod.world.biome.EasternPlainsBiome;
import com.mineria.mod.world.biome.MineriaBiome;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BiomesInit
{
    public static final List<MineriaBiome> BIOMES = new ArrayList<>();

    public static final MineriaBiome EASTERN_PLAINS = new EasternPlainsBiome();

    public static void loadModdedBiomes(BiomeLoadingEvent event)
    {
        BIOMES.forEach(biome -> {
            if(Objects.equals(event.getName(), biome.getBiome().getRegistryName()))
            {
                biome.addFeatures(event.getGeneration());
                biome.addSpawns(event.getSpawns());
            }
        });
    }

    public static void registerBiomes(RegistryEvent.Register<Biome> event)
    {
        BIOMES.forEach(biome -> {
            event.getRegistry().register(biome.getBiome());
            List<BiomeDictionary.Type> types = new ArrayList<>();
            biome.addTypes(types);
            BiomeDictionary.addTypes(getRegistryKey(biome.getBiome()), types.toArray(new BiomeDictionary.Type[0]));
            BiomeManager.addBiome(biome.getType(), new BiomeManager.BiomeEntry(getRegistryKey(biome.getBiome()), biome.getWeight()));
        });
    }

    private static RegistryKey<Biome> getRegistryKey(Biome biome)
    {
        ForgeRegistry<Biome> registry = (ForgeRegistry<Biome>) ForgeRegistries.BIOMES;
        int id = registry.getID(biome);
        return registry.getKey(id);
    }
}
