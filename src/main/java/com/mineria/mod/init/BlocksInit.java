package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.extractor.BlockExtractor;
import com.mineria.mod.blocks.extractor.TileEntityExtractor;
import com.mineria.mod.blocks.infuser.BlockInfuser;
import com.mineria.mod.blocks.infuser.TileEntityInfuser;
import com.mineria.mod.blocks.titane_extractor.BlockTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.TileEntityTitaneExtractor;
import com.mineria.mod.blocks.xp_block.BlockXp;
import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = References.MODID)
public class BlocksInit {
	
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

	//Other
	public static Block blue_glowstone;
	public static Block mineral_sand;
	public static Block lead_spike;
	public static Block compressed_lead_spike;
	public static Block infested_netherrack;
	public static Block xp_wall;
	
	public static void init()
	{
		//Ores
		lead_ore = new OreBase("lead_ore", 2, Material.ROCK, 4F, 5F, SoundType.STONE);
		titane_ore = new OreBase("titane_ore", 3, Material.ROCK, 6F, 10F, SoundType.STONE);
		copper_ore = new OreBase("copper_ore", 1, Material.ROCK, 3F, 5F, SoundType.STONE);
		lonsdaleite_ore = new OreBase("lonsdaleite_ore", 3, Material.ROCK, 6F, 10F, SoundType.STONE, 1, 2);
		nether_gold_ore = new OreBase("nether_gold_ore", 2, Material.ROCK, 1.75F, 1F, SoundType.STONE);
		silver_ore = new OreBase("silver_ore", 2, Material.ROCK, 3F, 5F, SoundType.STONE);
		
		//OreBlocks
		lead_block = new OreBlockBase("lead_block", 2, Material.IRON, 6.5F, 20F, SoundType.METAL);
		titane_block = new OreBlockBase("titane_block", 3, Material.IRON, 10F, 15F, SoundType.METAL);
		copper_block = new OreBlockBase("copper_block", 1, Material.IRON, 5F, 10F, SoundType.METAL);
		lonsdaleite_block = new LonsdaleiteBlock("lonsdaleite_block", 3, Material.IRON, 10F, 17.5F, SoundType.METAL);
		silver_block = new OreBlockBase("silver_block", 2, Material.IRON, 5F, 10F, SoundType.METAL);
		compressed_lead_block = new OreBlockBase("compressed_lead_block", 3, Material.IRON, 2.5F, 2F, SoundType.METAL);
		
		//Machines
		xp_block = new BlockXp("xp_block", 1, Material.IRON, 2.5F, 5F, SoundType.METAL);
		titane_extractor = new BlockTitaneExtractor("titane_extractor", false);
		lit_titane_extractor = new BlockTitaneExtractor("lit_titane_extractor", true).setCreativeTab(null).setLightLevel(0.5F);
		extractor = new BlockExtractor("extractor", 1, Material.IRON, 1F, 1F, SoundType.METAL);
		infuser = new BlockInfuser("infuser", Material.IRON, false);
		lit_infuser = new BlockInfuser("lit_infuser", Material.IRON, true).setCreativeTab(null).setLightLevel(0.5F);

		//Plants
		plantain = new PlantBase("plantain", Material.PLANTS, MapColor.GRASS);
		mint = new PlantBase("mint", Material.PLANTS, MapColor.GRASS);
		thyme = new PlantBase("thyme", Material.PLANTS, MapColor.GRASS);
		nettle = new PlantBase("nettle", Material.PLANTS, MapColor.GRASS);
		pulmonary = new PlantBase("pulmonary", Material.PLANTS, MapColor.GRASS);

		//Other
		blue_glowstone = new BlockBlueGlowstone("blue_glowstone", 1, Material.GLASS, 0.3F, 0.3F, SoundType.GLASS, 15F);
		mineral_sand = new BlockMineralSand("mineral_sand", 0, Material.SAND, 0.5F, 0.5F, SoundType.SAND);
		lead_spike = new BlockSpike("lead_spike", 2, Material.IRON, 7F, 25F, SoundType.METAL, 2F);
		compressed_lead_spike = new BlockSpike("compressed_lead_spike", 2, Material.IRON, 2.5F, 2F, SoundType.METAL, 4F);
		infested_netherrack = new BlockInfestedNetherrack("infested_netherrack", SoundType.STONE);
		xp_wall = new BlockBase("xp_wall", 1, Material.IRON, 2.5F, 5F, SoundType.METAL);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		//Ores
		event.getRegistry().register(lead_ore);
		event.getRegistry().register(titane_ore);
		event.getRegistry().register(copper_ore);
		event.getRegistry().register(lonsdaleite_ore);
		event.getRegistry().register(nether_gold_ore);
		event.getRegistry().register(silver_ore);
		
		//OreBlocks
		event.getRegistry().register(lead_block);
		event.getRegistry().register(titane_block);
		event.getRegistry().register(copper_block);
		event.getRegistry().register(lonsdaleite_block);
		event.getRegistry().register(silver_block);
		event.getRegistry().register(compressed_lead_block);
		
		//Machines
		event.getRegistry().register(xp_block);
		GameRegistry.registerTileEntity(TileEntityXpBlock.class, References.MODID + ":tile_xp_block");
		event.getRegistry().register(titane_extractor);
		event.getRegistry().register(lit_titane_extractor);
		GameRegistry.registerTileEntity(TileEntityTitaneExtractor.class, References.MODID + ":tile_titane_extractor");
		event.getRegistry().register(extractor);
		GameRegistry.registerTileEntity(TileEntityExtractor.class, References.MODID + ":tile_extractor");
		event.getRegistry().register(infuser);
		event.getRegistry().register(lit_infuser);
		GameRegistry.registerTileEntity(TileEntityInfuser.class, References.MODID + ":tile_infuser");

		//Plants
		event.getRegistry().register(plantain);
		event.getRegistry().register(mint);
		event.getRegistry().register(thyme);
		event.getRegistry().register(nettle);
		event.getRegistry().register(pulmonary);

		//Other
		event.getRegistry().register(blue_glowstone);
		event.getRegistry().register(mineral_sand);
		event.getRegistry().register(lead_spike);
		event.getRegistry().register(compressed_lead_spike);
		event.getRegistry().register(infested_netherrack);
		event.getRegistry().register(xp_wall);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				//Ores
				new ItemBlock(lead_ore).setRegistryName(lead_ore.getRegistryName()),
				new ItemBlock(titane_ore).setRegistryName(titane_ore.getRegistryName()),
				new ItemBlock(copper_ore).setRegistryName(copper_ore.getRegistryName()),
				new ItemBlock(lonsdaleite_ore).setRegistryName(lonsdaleite_ore.getRegistryName()),
				new ItemBlock(nether_gold_ore).setRegistryName(nether_gold_ore.getRegistryName()),
				new ItemBlock(silver_ore).setRegistryName(silver_ore.getRegistryName()),
				
				//OreBlocks
				new ItemBlock(lead_block).setRegistryName(lead_block.getRegistryName()),
				new ItemBlock(titane_block).setRegistryName(titane_block.getRegistryName()),
				new ItemBlock(copper_block).setRegistryName(copper_block.getRegistryName()),
				new ItemBlock(lonsdaleite_block).setRegistryName(lonsdaleite_block.getRegistryName()),
				new ItemBlock(silver_block).setRegistryName(silver_block.getRegistryName()),
				new ItemBlock(compressed_lead_block).setRegistryName(compressed_lead_block.getRegistryName()),
				
				//Machines
				new ItemBlock(xp_block).setRegistryName(xp_block.getRegistryName()),
				new ItemBlock(titane_extractor).setRegistryName(titane_extractor.getRegistryName()),
				new ItemBlock(lit_titane_extractor).setRegistryName(lit_titane_extractor.getRegistryName()),
				new ItemBlock(extractor).setRegistryName(extractor.getRegistryName()),
				new ItemBlock(infuser).setRegistryName(infuser.getRegistryName()),
				new ItemBlock(lit_infuser).setRegistryName(lit_infuser.getRegistryName()),

				//Plants
				new ItemBlock(plantain).setRegistryName(plantain.getRegistryName()),
				new ItemBlock(mint).setRegistryName(mint.getRegistryName()),
				new ItemBlock(thyme).setRegistryName(thyme.getRegistryName()),
				new ItemBlock(nettle).setRegistryName(nettle.getRegistryName()),
				new ItemBlock(pulmonary).setRegistryName(pulmonary.getRegistryName()),

				//Other
				new ItemBlock(blue_glowstone).setRegistryName(blue_glowstone.getRegistryName()),
				new ItemBlock(mineral_sand).setRegistryName(mineral_sand.getRegistryName()),
				new ItemBlock(lead_spike).setRegistryName(lead_spike.getRegistryName()),
				new ItemBlock(compressed_lead_spike).setRegistryName(compressed_lead_spike.getRegistryName()),
				new ItemBlock(infested_netherrack).setRegistryName(infested_netherrack.getRegistryName()),
				new ItemBlock(xp_wall).setRegistryName(xp_wall.getRegistryName())
						);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		//Ores
		registerRender(Item.getItemFromBlock(lead_ore));
		registerRender(Item.getItemFromBlock(titane_ore));
		registerRender(Item.getItemFromBlock(copper_ore));
		registerRender(Item.getItemFromBlock(lonsdaleite_ore));
		registerRender(Item.getItemFromBlock(nether_gold_ore));
		registerRender(Item.getItemFromBlock(silver_ore));
		
		//OreBlocks
		registerRender(Item.getItemFromBlock(lead_block));
		registerRender(Item.getItemFromBlock(titane_block));
		registerRender(Item.getItemFromBlock(copper_block));
		registerRender(Item.getItemFromBlock(lonsdaleite_block));
		registerRender(Item.getItemFromBlock(silver_block));
		registerRender(Item.getItemFromBlock(compressed_lead_block));
		
		//Machines
		registerRender(Item.getItemFromBlock(xp_block));
		registerRender(Item.getItemFromBlock(titane_extractor));
		registerRender(Item.getItemFromBlock(lit_titane_extractor));
		registerRender(Item.getItemFromBlock(extractor));
		registerRender(Item.getItemFromBlock(infuser));
		registerRender(Item.getItemFromBlock(lit_infuser));

		//Plants
		registerRender(Item.getItemFromBlock(plantain));
		registerRender(Item.getItemFromBlock(mint));
		registerRender(Item.getItemFromBlock(thyme));
		registerRender(Item.getItemFromBlock(nettle));
		registerRender(Item.getItemFromBlock(pulmonary));

		//Other
		registerRender(Item.getItemFromBlock(blue_glowstone));
		registerRender(Item.getItemFromBlock(mineral_sand));
		registerRender(Item.getItemFromBlock(lead_spike));
		registerRender(Item.getItemFromBlock(compressed_lead_spike));
		registerRender(Item.getItemFromBlock(infested_netherrack));
		registerRender(Item.getItemFromBlock(xp_wall));
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
