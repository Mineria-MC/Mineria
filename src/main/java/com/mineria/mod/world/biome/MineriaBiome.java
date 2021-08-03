package com.mineria.mod.world.biome;

import com.mineria.mod.init.BiomesInit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;

import java.util.List;

public abstract class MineriaBiome
{
    protected static MobSpawnInfo.Builder spawnInfoBuilder = new MobSpawnInfo.Builder();
    protected static BiomeGenerationSettings.Builder settingsBuilder = new BiomeGenerationSettings.Builder().withSurfaceBuilder(ConfiguredSurfaceBuilders.GRASS);
    private final int weight;
    private final BiomeManager.BiomeType type;
    private final Biome biome;

    public MineriaBiome(String name, int weight, BiomeManager.BiomeType type, Biome biome)
    {
        this.weight = weight;
        this.type = type;
        this.biome = biome.setRegistryName(new ResourceLocation("mineria", name));
        BiomesInit.BIOMES.add(this);
    }

    public int getWeight()
    {
        return weight;
    }

    public BiomeManager.BiomeType getType()
    {
        return type;
    }

    public Biome getBiome()
    {
        return biome;
    }

    public abstract void addFeatures(BiomeGenerationSettingsBuilder builder);

    public abstract void addSpawns(MobSpawnInfoBuilder builder);

    public abstract void addTypes(List<BiomeDictionary.Type> types);

    protected static int getSkyColorWithTemperatureModifier(float temperature)
    {
        float lvt_1_1_ = temperature / 3.0F;
        lvt_1_1_ = MathHelper.clamp(lvt_1_1_, -1.0F, 1.0F);
        return MathHelper.hsvToRGB(0.62222224F - lvt_1_1_ * 0.05F, 0.5F + lvt_1_1_ * 0.1F, 1.0F);
    }
}