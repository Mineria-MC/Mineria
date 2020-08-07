package com.mineria.mod.world.gen;

import com.mineria.mod.init.BlocksInit;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenCustomOres implements IWorldGenerator
{
	private WorldGenerator copper_ore;
	private WorldGenerator silver_ore;
	private WorldGenerator lead_ore;
	private WorldGenerator titane_ore;
	private WorldGenerator lonsdaleite_ore;
	private WorldGenerator nether_gold_ore;
	private WorldGenerator infested_netherrack;
	private WorldGenerator mineral_sand;
	
	public WorldGenCustomOres()
	{
		copper_ore = new WorldGenMinable(BlocksInit.copper_ore.getDefaultState(), 7, BlockMatcher.forBlock(Blocks.STONE));
		silver_ore = new WorldGenMinable(BlocksInit.silver_ore.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.STONE));
		lead_ore = new WorldGenMinable(BlocksInit.lead_ore.getDefaultState(), 9, BlockMatcher.forBlock(Blocks.STONE));
		titane_ore = new WorldGenMinable(BlocksInit.titane_ore.getDefaultState(), 6, BlockMatcher.forBlock(Blocks.STONE));
		lonsdaleite_ore = new WorldGenMinable(BlocksInit.lonsdaleite_ore.getDefaultState(), 3, BlockMatcher.forBlock(Blocks.STONE));
		nether_gold_ore = new WorldGenMinable(BlocksInit.nether_gold_ore.getDefaultState(), 12, BlockMatcher.forBlock(Blocks.NETHERRACK));
		infested_netherrack = new WorldGenMinable(BlocksInit.infested_netherrack.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.NETHERRACK));
		mineral_sand = new WorldGenMinable(BlocksInit.mineral_sand.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.SAND));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(world.provider.getDimension() == 0) runGenerator(copper_ore, world, random, chunkX, chunkZ, 20.0F, 40, 80);
		if(world.provider.getDimension() == 0) runGenerator(silver_ore, world, random, chunkX, chunkZ, 4.0F, 0, 32);
		if(world.provider.getDimension() == 0) runGenerator(lead_ore, world, random, chunkX, chunkZ, 12.0F, 0, 64);
		if(world.provider.getDimension() == 0) runGenerator(titane_ore, world, random, chunkX, chunkZ, 0.1F, 0, 13);
		if(world.provider.getDimension() == 0) runGenerator(lonsdaleite_ore, world, random, chunkX, chunkZ, 1.0F, 0, 10);
		if(world.provider.getDimension() == -1) runGenerator(nether_gold_ore, world, random, chunkX, chunkZ, 6F, 0, 128);
		if(world.provider.getDimension() == -1) runGenerator(infested_netherrack, world, random, chunkX, chunkZ, 16F, 32, 128);
		if(world.provider.getDimension() == 0) runGenerator(mineral_sand, world, random, chunkX, chunkZ, 12F, 40, 70);
	}
	
	private void runGenerator(WorldGenerator gen, World world, Random rand, int chunkX, int chunkZ, float chance, int minHeight, int maxHeight)
	{
		if(minHeight > maxHeight || minHeight < 0 || maxHeight > 256) throw new IllegalArgumentException("Ore generated out of bounds");
		
		int heightDiff = maxHeight - minHeight + 1;
		for(int i = 0; i < chance; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunkZ * 16 + rand.nextInt(16);
			
			gen.generate(world, rand, new BlockPos(x, y, z));
		}
	}
}
