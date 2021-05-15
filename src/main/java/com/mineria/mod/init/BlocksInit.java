package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.blocks.barrel.BlockWaterBarrel;
import com.mineria.mod.blocks.barrel.copper.BlockCopperWaterBarrel;
import com.mineria.mod.blocks.barrel.diamond.BlockDiamondFluidBarrel;
import com.mineria.mod.blocks.barrel.golden.BlockGoldenWaterBarrel;
import com.mineria.mod.blocks.barrel.iron.BlockIronFluidBarrel;
import com.mineria.mod.blocks.extractor.BlockExtractor;
import com.mineria.mod.blocks.infuser.BlockInfuser;
import com.mineria.mod.blocks.titane_extractor.BlockTitaneExtractor;
import com.mineria.mod.blocks.xp_block.BlockXp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = References.MODID)
public final class BlocksInit
{
	private static final List<Block> BLOCKS = new ArrayList<>();
	protected static final List<ItemBlock> ITEMBLOCKS = new ArrayList<>();
	private static final Map<Block, ItemBlock> UNREGISTERED = new HashMap<>();

	//Ores
	public static final Block LEAD_ORE = register("lead_ore", new MineriaBlockOre(4, 5, 2));
	public static final Block TITANE_ORE = register("titane_ore", new MineriaBlockOre(6, 10, 3));
	public static final Block COPPER_ORE = register("copper_ore", new MineriaBlockOre(3, 5, 1));
	public static final Block LONSDALEITE_ORE = register("lonsdaleite_ore", new MineriaBlock.BlockProperties().hardnessAndResistance(6, 10).harvestLevel(3, "pickaxe").sounds(SoundType.STONE).multipleXp(4, 10).customDrop(ItemsInit.LONSDALEITE).build(Material.ROCK));
	public static final Block NETHER_GOLD_ORE = register("nether_gold_ore", new MineriaBlockOre(1.75F, 1, 2));
	public static final Block SILVER_ORE = register("silver_ore", new MineriaBlockOre(3, 5, 2));

	//OreBlocks
	public static final Block LEAD_BLOCK = register("lead_block", new MineriaBlockMetal(6.5F, 20, 2));
	public static final Block TITANE_BLOCK = register("titane_block", new MineriaBlockMetal(10, 15, 3));
	public static final Block COPPER_BLOCK = register("copper_block", new MineriaBlockMetal(5, 10, 1));
	public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new BlockLonsdaleite());
	public static final Block SILVER_BLOCK = register("silver_block", new MineriaBlockMetal(5, 10, 2));
	public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new MineriaBlockMetal(2.5F, 2, 3));

	//Machines
	public static final Block XP_BLOCK = register("xp_block", new BlockXp());
	public static final Block TITANE_EXTRACTOR = register("titane_extractor", new BlockTitaneExtractor());
	public static final Block EXTRACTOR = register("extractor", new BlockExtractor());
	public static final Block INFUSER = register("infuser", new BlockInfuser());

	//Plants
	public static final Block PLANTAIN = register("plantain", new MineriaBlockPlant(MapColor.GRASS));
	public static final Block MINT = register("mint", new MineriaBlockPlant(MapColor.GRASS));
	public static final Block THYME = register("thyme", new MineriaBlockPlant(MapColor.GRASS));
	public static final Block NETTLE = register("nettle", new MineriaBlockPlant(MapColor.GRASS));
	public static final Block PULMONARY = register("pulmonary", new MineriaBlockPlant(MapColor.GRASS));

	//Other
	public static final Block BLUE_GLOWSTONE = register("blue_glowstone", new BlockBlueGlowstone());
	public static final Block MINERAL_SAND = register("mineral_sand", new BlockMineralSand());
	public static final Block LEAD_SPIKE = register("lead_spike", new BlockSpike(2.5F, 2, 2));
	public static final Block COMPRESSED_LEAD_SPIKE = register("compressed_lead_spike", new BlockSpike(7, 25, 4));
	public static final Block INFESTED_NETHERRACK = register("infested_netherrack", new BlockInfestedNetherrack());
	public static final Block XP_WALL = register("xp_wall", new MineriaBlock(Material.IRON, 2.5F, 5, 1, SoundType.METAL));
	public static final Block WATER_BARREL = register("water_barrel", new BlockWaterBarrel(8), AbstractBlockWaterBarrel.ItemBlockBarrel::new);
	public static final Block INFINITE_WATER_BARREL = register("infinite_water_barrel", new BlockWaterBarrel(-1), AbstractBlockWaterBarrel.ItemBlockBarrel::new);
	public static final Block COPPER_WATER_BARREL = register("copper_water_barrel", new BlockCopperWaterBarrel(), AbstractBlockWaterBarrel.ItemBlockBarrel::new);
	public static final Block IRON_FLUID_BARREL = register("iron_fluid_barrel", new BlockIronFluidBarrel(), AbstractBlockWaterBarrel.ItemBlockBarrel::new);
	public static final Block GOLDEN_WATER_BARREL = register("golden_water_barrel", new BlockGoldenWaterBarrel(), BlockGoldenWaterBarrel.ItemBlockBarrel::new);
	//public static final Block DIAMOND_FLUID_BARREL = register("diamond_fluid_barrel", new BlockDiamondFluidBarrel(), AbstractBlockWaterBarrel.ItemBlockBarrel::new);
	public static final Block TNT_BARREL = register("tnt_barrel", new BlockTNTBarrel());

	private static Block register(String name, Block instance)
	{
		return register(name, instance, ItemBlock::new);
	}

	private static <T extends Block> T register(String name, T instance, Function<T, ItemBlock> itemBlockBuilder)
	{
		UNREGISTERED.put(instance.setRegistryName(name).setUnlocalizedName(name), (ItemBlock) itemBlockBuilder.apply(instance).setRegistryName(name).setUnlocalizedName(name));
		return instance;
	}

	public static void init()
	{
		BLOCKS.addAll(UNREGISTERED.keySet());
		ITEMBLOCKS.addAll(UNREGISTERED.values());
	}

	public static Block getBlockFromName(String name)
	{
		return UNREGISTERED.keySet().stream().filter(block -> block.getRegistryName() != null && block.getRegistryName().getResourcePath().equals(name))
				.findFirst().orElse(Blocks.AIR);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		BLOCKS.forEach(event.getRegistry()::register);
	}
}
