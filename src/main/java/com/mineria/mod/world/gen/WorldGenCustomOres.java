package com.mineria.mod.world.gen;

import com.google.common.collect.Sets;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class WorldGenCustomOres implements IWorldGenerator
{
	private final WorldGenerator copper_ore;
	private final WorldGenerator silver_ore;
	private final WorldGenerator lead_ore;
	private final WorldGenerator titane_ore;
	private final WorldGenerator lonsdaleite_ore;
	private final WorldGenerator nether_gold_ore;
	private final WorldGenerator infested_netherrack;
	private final WorldGenerator mineral_sand;
	
	public WorldGenCustomOres()
	{
		copper_ore = new WorldGenMinable(BlocksInit.COPPER_ORE.getDefaultState(), 7, BlockMatcher.forBlock(Blocks.STONE));
		silver_ore = new WorldGenMinable(BlocksInit.SILVER_ORE.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.STONE));
		lead_ore = new WorldGenMinable(BlocksInit.LEAD_ORE.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.STONE));
		titane_ore = new WorldGenMinable(BlocksInit.TITANE_ORE.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.STONE));
		lonsdaleite_ore = new WorldGenMinable(BlocksInit.LONSDALEITE_ORE.getDefaultState(), 3, BlockMatcher.forBlock(Blocks.STONE));
		nether_gold_ore = new WorldGenMinable(BlocksInit.NETHER_GOLD_ORE.getDefaultState(), 12, BlockMatcher.forBlock(Blocks.NETHERRACK));
		infested_netherrack = new WorldGenMinable(BlocksInit.INFESTED_NETHERRACK.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.NETHERRACK));
		mineral_sand = new WorldGenMinable(BlocksInit.MINERAL_SAND.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.SAND));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.getDimension() == 0)
		{
			runGenerator(copper_ore, world, random, chunkX, chunkZ, 20.0F, 40, 80);
			runGenerator(silver_ore, world, random, chunkX, chunkZ, 4.0F, 0, 32);
			runGenerator(lead_ore, world, random, chunkX, chunkZ, 12.0F, 0, 64);
			runGenerator(titane_ore, world, random, chunkX, chunkZ, 0.1F, 0, 13);
			runGenerator(lonsdaleite_ore, world, random, chunkX, chunkZ, 1.0F, 0, 10);
			runGenerator(mineral_sand, world, random, chunkX, chunkZ, 12F, 40, 70, Biomes.BEACH, Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.MUTATED_DESERT);
		}
		else if(world.provider.getDimension() == -1)
		{
			runGenerator(nether_gold_ore, world, random, chunkX, chunkZ, 6F, 0, 128);
			runGenerator(infested_netherrack, world, random, chunkX, chunkZ, 16F, 32, 128);
		}
	}
	
	private static void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, float chance, int minHeight, int maxHeight, Biome... biomes)
	{
		Set<Biome> biomeSet = Sets.newHashSet(biomes);

		if(minHeight > maxHeight || minHeight < 0 || maxHeight > 256) throw new IllegalArgumentException("Ore cannot generate below 0 or above 256.");
		
		int heightDiff = maxHeight - minHeight + 1;
		for(int i = 0; i < chance; i++)
		{
			BlockPos genPos = new BlockPos(chunkX * 16 + rand.nextInt(16), minHeight + rand.nextInt(heightDiff), chunkZ * 16 + rand.nextInt(16));

			if(biomeSet.isEmpty() || biomeSet.contains(world.getBiome(genPos)))
				gen.generate(world, rand, genPos);
		}
	}
}
