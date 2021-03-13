package com.mineria.mod.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.blocks.BlockBase;
import com.mineria.mod.blocks.OreBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlocksInit
{
	//Deferred Registries (Blocks and Block Items)
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
	public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);

	//Ores
	public static final Block LEAD_ORE = register("lead_ore", new OreBase(2, 4.0F, 5.0F, SoundType.STONE));
	public static final Block TITANE_ORE = register("titane_ore", new OreBase(3, 6F, 10F, SoundType.STONE));
	public static final Block COPPER_ORE = register("copper_ore", new OreBase(1, 3F, 5F, SoundType.STONE));
	public static final Block LONSDALEITE_ORE = register("lonsdaleite_ore", new OreBase(3, 6F, 10F, SoundType.STONE));
	public static final Block NETHER_GOLD_ORE = register("nether_gold_ore", new OreBase(2, 1.75F, 1F, SoundType.STONE));
	public static final Block SILVER_ORE = register("silver_ore", new OreBase(2, 3F, 5F, SoundType.STONE));
	
	//OreBlocks
	public static final Block LEAD_BLOCK = register("lead_block", new BlockBase(Material.IRON, 6.5F, 20F, SoundType.METAL, 2));
	public static final Block TITANE_BLOCK = register("titane_block", new BlockBase(Material.IRON, 10F, 15F, SoundType.METAL, 3));
	public static final Block COPPER_BLOCK = register("copper_block", new BlockBase(Material.IRON, 5F, 10F, SoundType.METAL, 1));
	public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new BlockBase(Material.IRON, 10F, 17.5F, SoundType.METAL, 3));
	public static final Block SILVER_BLOCK = register("silver_block", new BlockBase(Material.IRON, 5F, 10F, SoundType.METAL, 2));
	public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new BlockBase(Material.IRON, 2.5F, 2F, SoundType.METAL, 3));
	
	//Machines
	//public static final Block xp_block;
	//public static final Block titane_extractor;
	//public static final Block extractor;
	//public static final Block infuser;

	/*
	//Plants
	public static final Block plantain;
	public static final Block mint;
	public static final Block thyme;
	public static final Block nettle;
	public static final Block pulmonary;*/

	//Other
	public static final Block blue_glowstone = register("blue_glowstone", new Block(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).setLightLevel(value -> 15)));
	public static final Block mineral_sand = register("mineral_sand", new FallingBlock(AbstractBlock.Properties.create(Material.SAND).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(2)));
	//public static final Block lead_spike;
	//public static final Block compressed_lead_spike;
	//public static final Block infested_netherrack;
	public static final Block xp_wall = register("xp_wall", new BlockBase(Material.IRON, 2.5F, 5F, SoundType.METAL, 1));
	//public static final Block water_barrel;
	//public static final Block infinite_water_barrel;

		/*
		xp_block = register(new BlockXp("xp_block", 1, Material.IRON, 2.5F, 5F, SoundType.METAL));
		titane_extractor = register(new BlockTitaneExtractor("titane_extractor", false));
		extractor = register(new BlockExtractor("extractor", 1, Material.IRON, 1F, 1F, SoundType.METAL));
		infuser = register(new BlockInfuser("infuser", Material.IRON, false));

		plantain = register(new PlantBase("plantain", Material.PLANTS, MapColor.GRASS));
		mint = register(new PlantBase("mint", Material.PLANTS, MapColor.GRASS));
		thyme = register(new PlantBase("thyme", Material.PLANTS, MapColor.GRASS));
		nettle = register(new PlantBase("nettle", Material.PLANTS, MapColor.GRASS));
		pulmonary = register(new PlantBase("pulmonary", Material.PLANTS, MapColor.GRASS));

		lead_spike = register(new BlockSpike("lead_spike", 2, Material.IRON, 7F, 25F, SoundType.METAL, 2F));
		compressed_lead_spike = register(new BlockSpike("compressed_lead_spike", 2, Material.IRON, 2.5F, 2F, SoundType.METAL, 4F));
		infested_netherrack = register(new BlockInfestedNetherrack("infested_netherrack", SoundType.STONE));
		water_barrel = register(new BlockBarrel("water_barrel", 8));
		infinite_water_barrel = register(new BlockBarrel("infinite_water_barrel", -1));
		*/

	private static Block register(String name, Block instance)
	{
		return register(name, instance, true);
	}

	private static Block register(String name, Block instance, boolean blockItem)
	{
		BLOCKS.register(name, () -> instance);
		Item.Properties basicProperties = new Item.Properties().group(Mineria.MINERIA_GROUP);
		if(blockItem) registerBlockItem(name, new BlockItem(instance, basicProperties));
		return instance;
	}

	private static void registerBlockItem(String name, BlockItem item)
	{
		BLOCK_ITEMS.register(name, () -> item);
	}

	public static void registerBlocks()
	{
		//GameRegistry.registerTileEntity(TileEntityXpBlock.class, new ResourceLocation(References.MODID, "tile_xp_block"));
		//GameRegistry.registerTileEntity(TileEntityTitaneExtractor.class, new ResourceLocation(References.MODID, "tile_titane_extractor"));
		//GameRegistry.registerTileEntity(TileEntityExtractor.class, new ResourceLocation(References.MODID, "tile_extractor"));
		//GameRegistry.registerTileEntity(TileEntityInfuser.class, new ResourceLocation(References.MODID, "tile_infuser"));
		//GameRegistry.registerTileEntity(TileEntityBarrel.class, new ResourceLocation(References.MODID, "tile_water_barrel"));
	}

	public static void registerItemBlocks()
	{
		//event.getRegistry().register(new ItemBlockBarrel(water_barrel));
		//event.getRegistry().register(new ItemBlockBarrel(infinite_water_barrel));
	}
}
