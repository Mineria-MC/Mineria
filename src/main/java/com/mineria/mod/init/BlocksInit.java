package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.barrel.BlockBarrel;
import com.mineria.mod.blocks.barrel.TileEntityBarrel;
import com.mineria.mod.blocks.extractor.BlockExtractor;
import com.mineria.mod.blocks.extractor.TileEntityExtractor;
import com.mineria.mod.blocks.infuser.BlockInfuser;
import com.mineria.mod.blocks.infuser.TileEntityInfuser;
import com.mineria.mod.blocks.titane_extractor.BlockTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.TileEntityTitaneExtractor;
import com.mineria.mod.blocks.xp_block.BlockXp;
import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import com.mineria.mod.items.ItemBlockBarrel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = References.MODID)
public class BlocksInit {

	public static final List<Block> blockList = new ArrayList<>();

	//Ores
	public static Block lead_ore;
	public static Block titane_ore;
	public static Block copper_ore;
	public static Block lonsdaleite_ore;
	public static Block nether_gold_ore;
	public static Block silver_ore;
	
	//OreBlocks
	public static Block lead_block;
	public static Block titane_block;
	public static Block copper_block;
	public static Block lonsdaleite_block;
	public static Block silver_block;
	public static Block compressed_lead_block;
	
	//Machines
	public static Block xp_block;
	public static Block titane_extractor;
	public static Block lit_titane_extractor;
	public static Block extractor;
	public static Block infuser;
	public static Block lit_infuser;

	//Plants
	public static Block plantain;
	public static Block mint;
	public static Block thyme;
	public static Block nettle;
	public static Block pulmonary;

	//WoodCuts
	public static Block cat_woodcut;
	public static Block cow_woodcut;
	public static Block deer_woodcut;
	public static Block dog_woodcut;
	public static Block donkey_woodcut;
	public static Block goat_woodcut;
	public static Block horse_woodcut;
	public static Block rabbit_woodcut;
	public static Block wolf_woodcut;

	//Other
	public static Block blue_glowstone;
	public static Block mineral_sand;
	public static Block lead_spike;
	public static Block compressed_lead_spike;
	public static Block infested_netherrack;
	public static Block xp_wall;
	public static Block water_barrel;
	public static Block infinite_water_barrel;
	
	public static void init()
	{

		//Ores
		lead_ore = register(new OreBase("lead_ore", 2, Material.ROCK, 4F, 5F, SoundType.STONE));
		titane_ore = register(new OreBase("titane_ore", 3, Material.ROCK, 6F, 10F, SoundType.STONE));
		copper_ore = register(new OreBase("copper_ore", 1, Material.ROCK, 3F, 5F, SoundType.STONE));
		lonsdaleite_ore = register(new OreBase("lonsdaleite_ore", 3, Material.ROCK, 6F, 10F, SoundType.STONE, 1, 2));
		nether_gold_ore = register(new OreBase("nether_gold_ore", 2, Material.ROCK, 1.75F, 1F, SoundType.STONE));
		silver_ore = register(new OreBase("silver_ore", 2, Material.ROCK, 3F, 5F, SoundType.STONE));
		
		//OreBlocks
		lead_block = register(new OreBlockBase("lead_block", 2, Material.IRON, 6.5F, 20F, SoundType.METAL));
		titane_block = register(new OreBlockBase("titane_block", 3, Material.IRON, 10F, 15F, SoundType.METAL));
		copper_block = register(new OreBlockBase("copper_block", 1, Material.IRON, 5F, 10F, SoundType.METAL));
		lonsdaleite_block = register(new LonsdaleiteBlock("lonsdaleite_block", 3, Material.IRON, 10F, 17.5F, SoundType.METAL));
		silver_block = register(new OreBlockBase("silver_block", 2, Material.IRON, 5F, 10F, SoundType.METAL));
		compressed_lead_block = register(new OreBlockBase("compressed_lead_block", 3, Material.IRON, 2.5F, 2F, SoundType.METAL));
		
		//Machines
		xp_block = register(new BlockXp("xp_block", 1, Material.IRON, 2.5F, 5F, SoundType.METAL));
		titane_extractor = register(new BlockTitaneExtractor("titane_extractor", false));
		lit_titane_extractor = register(new BlockTitaneExtractor("lit_titane_extractor", true).setCreativeTab(null).setLightLevel(0.5F));
		extractor = register(new BlockExtractor("extractor", 1, Material.IRON, 1F, 1F, SoundType.METAL));
		infuser = register(new BlockInfuser("infuser", Material.IRON, false));
		lit_infuser = register(new BlockInfuser("lit_infuser", Material.IRON, true).setLightLevel(0.5F));

		//Plants
		plantain = register(new PlantBase("plantain", Material.PLANTS, MapColor.GRASS));
		mint = register(new PlantBase("mint", Material.PLANTS, MapColor.GRASS));
		thyme = register(new PlantBase("thyme", Material.PLANTS, MapColor.GRASS));
		nettle = register(new PlantBase("nettle", Material.PLANTS, MapColor.GRASS));
		pulmonary = register(new PlantBase("pulmonary", Material.PLANTS, MapColor.GRASS));

		//WoodCuts
		cat_woodcut = register(new BlockWoodCut("cat"));
		cow_woodcut = register(new BlockWoodCut("cow"));
		deer_woodcut = register(new BlockWoodCut("deer"));
		dog_woodcut = register(new BlockWoodCut("dog"));
		donkey_woodcut = register(new BlockWoodCut("donkey"));
		goat_woodcut = register(new BlockWoodCut("goat"));
		horse_woodcut = register(new BlockWoodCut("horse"));
		rabbit_woodcut = register(new BlockWoodCut("rabbit"));
		wolf_woodcut = register(new BlockWoodCut("wolf"));

		//Other
		blue_glowstone = register(new BlockBlueGlowstone("blue_glowstone", 1, Material.GLASS, 0.3F, 0.3F, SoundType.GLASS, 15F));
		mineral_sand = register(new BlockMineralSand("mineral_sand", 0, Material.SAND, 0.5F, 0.5F, SoundType.SAND));
		lead_spike = register(new BlockSpike("lead_spike", 2, Material.IRON, 7F, 25F, SoundType.METAL, 2F));
		compressed_lead_spike = register(new BlockSpike("compressed_lead_spike", 2, Material.IRON, 2.5F, 2F, SoundType.METAL, 4F));
		infested_netherrack = register(new BlockInfestedNetherrack("infested_netherrack", SoundType.STONE));
		xp_wall = register(new BlockBase("xp_wall", 1, Material.IRON, 2.5F, 5F, SoundType.METAL));
		water_barrel = register(new BlockBarrel("water_barrel", 8));
		infinite_water_barrel = register(new BlockBarrel("infinite_water_barrel", -1));
	}

	private static Block register(Block instance)
	{
		blockList.add(instance);
		return instance;
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		blockList.forEach(block -> event.getRegistry().register(block));
		GameRegistry.registerTileEntity(TileEntityXpBlock.class, new ResourceLocation(References.MODID, "tile_xp_block"));
		GameRegistry.registerTileEntity(TileEntityTitaneExtractor.class, new ResourceLocation(References.MODID, "tile_titane_extractor"));
		GameRegistry.registerTileEntity(TileEntityExtractor.class, new ResourceLocation(References.MODID, "tile_extractor"));
		GameRegistry.registerTileEntity(TileEntityInfuser.class, new ResourceLocation(References.MODID, "tile_infuser"));
		GameRegistry.registerTileEntity(TileEntityBarrel.class, new ResourceLocation(References.MODID, "tile_water_barrel"));
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		blockList.stream().filter(block -> !(block instanceof BlockBarrel)).forEach(block -> event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName())));
		event.getRegistry().register(new ItemBlockBarrel(water_barrel));
		event.getRegistry().register(new ItemBlockBarrel(infinite_water_barrel));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		blockList.forEach(block -> registerRender(Item.getItemFromBlock(block)));
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
