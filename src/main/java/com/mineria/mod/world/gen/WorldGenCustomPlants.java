package com.mineria.mod.world.gen;

import com.mineria.mod.blocks.MineriaBlockPlant;
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
    private final WorldGenerator plantain;

    private final WorldGenerator mint_plains;
    private final WorldGenerator mint_forest;
    private final WorldGenerator mint_jungle;

    private final WorldGenerator thyme_plains;
    private final WorldGenerator thyme_savanna;
    private final WorldGenerator thyme_mountain;

    private final WorldGenerator nettle_plains;
    private final WorldGenerator nettle_forest;
    private final WorldGenerator nettle_jungle;

    private final WorldGenerator pulmonary_plains;
    private final WorldGenerator pulmonary_forest;

    public WorldGenCustomPlants()
    {
        plantain = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.PLANTAIN);

        mint_plains = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.MINT);
        mint_forest = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.MINT);
        mint_jungle = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.MINT);

        thyme_plains = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.THYME);
        thyme_savanna = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.THYME);
        thyme_mountain = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.THYME);

        nettle_plains = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.NETTLE);
        nettle_forest = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.NETTLE);
        nettle_jungle = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.NETTLE);

        pulmonary_plains = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.PULMONARY);
        pulmonary_forest = new PlantWorldGenerator((MineriaBlockPlant)BlocksInit.PULMONARY);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if(world.provider.getDimension() == 0)
        {
            runGenerator(plantain, world, random, chunkX, chunkZ, 100, Biomes.PLAINS);

            runGenerator(mint_plains, world, random, chunkX, chunkZ, 60, Biomes.PLAINS);
            runGenerator(mint_forest, world, random, chunkX, chunkZ, 30, Biomes.FOREST);
            runGenerator(mint_jungle, world, random, chunkX, chunkZ, 10, Biomes.JUNGLE);

            runGenerator(thyme_plains, world, random, chunkX, chunkZ, 20, Biomes.PLAINS);
            runGenerator(thyme_savanna, world, random, chunkX, chunkZ, 70, Biomes.SAVANNA);
            runGenerator(thyme_mountain, world, random, chunkX, chunkZ, 10, Biomes.EXTREME_HILLS);

            runGenerator(nettle_plains, world, random, chunkX, chunkZ, 5, Biomes.PLAINS);
            runGenerator(nettle_forest, world, random, chunkX, chunkZ, 25, Biomes.FOREST);
            runGenerator(nettle_jungle, world, random, chunkX, chunkZ, 70, Biomes.JUNGLE);

            runGenerator(pulmonary_plains, world, random, chunkX, chunkZ, 80, Biomes.FOREST);
            runGenerator(pulmonary_forest, world, random, chunkX, chunkZ, 20, Biomes.PLAINS);
        }
    }

    private static void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, int chance, Biome biome)
    {
        BlockPos chunkPos = new BlockPos(chunkX * 16, 0, chunkZ * 16);

        if(world.getBiome(chunkPos) == biome)
        {
            if(rand.nextInt(200) <= chance)
            {
                int x = rand.nextInt(16) + 4;
                int z = rand.nextInt(16) + 4;
                int y = world.getHeight(chunkPos.add(x, 0, z)).getY() + 32;

                if (y > 0)
                {
                    int randY = rand.nextInt(y);
                    BlockPos genPos = chunkPos.add(x, randY, z);
                    MineriaBlockPlant plant = ((PlantWorldGenerator)gen).getPlant();

                    if (plant.getDefaultState().getMaterial() != Material.AIR)
                    {
                        ((PlantWorldGenerator)gen).setGeneratedPlant(plant);
                        gen.generate(world, rand, genPos);
                    }
                }
            }
        }
    }
}
