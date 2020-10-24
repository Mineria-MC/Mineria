package com.mineria.mod.world.gen;

import com.mineria.mod.blocks.PlantBase;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.material.Material;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenCustomPlants implements IWorldGenerator
{
    private WorldGenerator plantain;

    private WorldGenerator mint_plains;
    private WorldGenerator mint_forest;
    private WorldGenerator mint_jungle;

    private WorldGenerator thyme_plains;
    private WorldGenerator thyme_savanna;
    private WorldGenerator thyme_mountain;

    private WorldGenerator nettle_plains;
    private WorldGenerator nettle_forest;
    private WorldGenerator nettle_jungle;

    private WorldGenerator pulmonary_plains;
    private WorldGenerator pulmonary_forest;

    public WorldGenCustomPlants()
    {
        plantain = new WorldGenCustomPlant((PlantBase)BlocksInit.plantain);

        mint_plains = new WorldGenCustomPlant((PlantBase)BlocksInit.mint);
        mint_forest = new WorldGenCustomPlant((PlantBase)BlocksInit.mint);
        mint_jungle = new WorldGenCustomPlant((PlantBase)BlocksInit.mint);

        thyme_plains = new WorldGenCustomPlant((PlantBase)BlocksInit.thyme);
        thyme_savanna = new WorldGenCustomPlant((PlantBase)BlocksInit.thyme);
        thyme_mountain = new WorldGenCustomPlant((PlantBase)BlocksInit.thyme);

        nettle_plains = new WorldGenCustomPlant((PlantBase)BlocksInit.nettle);
        nettle_forest = new WorldGenCustomPlant((PlantBase)BlocksInit.nettle);
        nettle_jungle = new WorldGenCustomPlant((PlantBase)BlocksInit.nettle);

        pulmonary_plains = new WorldGenCustomPlant((PlantBase)BlocksInit.pulmonary);
        pulmonary_forest = new WorldGenCustomPlant((PlantBase)BlocksInit.pulmonary);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if(world.provider.getDimension() == 0) runGenerator(plantain, world, random, chunkX, chunkZ, 100, Biomes.PLAINS);

        if(world.provider.getDimension() == 0) runGenerator(mint_plains, world, random, chunkX, chunkZ, 60, Biomes.PLAINS);
        if(world.provider.getDimension() == 0) runGenerator(mint_forest, world, random, chunkX, chunkZ, 30, Biomes.FOREST);
        if(world.provider.getDimension() == 0) runGenerator(mint_jungle, world, random, chunkX, chunkZ, 10, Biomes.JUNGLE);

        if(world.provider.getDimension() == 0) runGenerator(thyme_plains, world, random, chunkX, chunkZ, 20, Biomes.PLAINS);
        if(world.provider.getDimension() == 0) runGenerator(thyme_savanna, world, random, chunkX, chunkZ, 70, Biomes.SAVANNA);
        if(world.provider.getDimension() == 0) runGenerator(thyme_mountain, world, random, chunkX, chunkZ, 10, Biomes.EXTREME_HILLS);

        if(world.provider.getDimension() == 0) runGenerator(nettle_plains, world, random, chunkX, chunkZ, 5, Biomes.PLAINS);
        if(world.provider.getDimension() == 0) runGenerator(nettle_forest, world, random, chunkX, chunkZ, 25, Biomes.FOREST);
        if(world.provider.getDimension() == 0) runGenerator(nettle_jungle, world, random, chunkX, chunkZ, 70, Biomes.JUNGLE);

        if(world.provider.getDimension() == 0) runGenerator(pulmonary_plains, world, random, chunkX, chunkZ, 80, Biomes.FOREST);
        if(world.provider.getDimension() == 0) runGenerator(pulmonary_forest, world, random, chunkX, chunkZ, 20, Biomes.PLAINS);
    }

    public void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, Biome biome)
    {
        BlockPos chunkPos = new BlockPos(chunkX * 16, 0, chunkZ * 16);

        if(world.getBiome(chunkPos) == biome)
        {
            if(rand.nextInt(200) <= chance)
            {
                int i7 = rand.nextInt(16) + 4;
                int l10 = rand.nextInt(16) + 4;
                int j14 = world.getHeight(chunkPos.add(i7, 0, l10)).getY() + 32;

                if (j14 > 0)
                {
                    int k17 = rand.nextInt(j14);
                    BlockPos blockpos1 = chunkPos.add(i7, k17, l10);
                    PlantBase plant = ((WorldGenCustomPlant)gen).getPlant();

                    if (plant.getDefaultState().getMaterial() != Material.AIR)
                    {
                        ((WorldGenCustomPlant)gen).setGeneratedPlant(plant);
                        ((WorldGenCustomPlant)gen).generate(world, rand, blockpos1);
                    }
                }
            }
        }
    }
}
