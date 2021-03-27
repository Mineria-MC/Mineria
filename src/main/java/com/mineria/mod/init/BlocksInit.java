package com.mineria.mod.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.barrel.WaterBarrelBlock;
import com.mineria.mod.blocks.infuser.InfuserBlock;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorBlock;
import com.mineria.mod.blocks.xp_block.XpBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BlocksInit
{
	//Deferred Registries (Blocks and Block Items)
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
	public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);

	//Ores
	public static final Block LEAD_ORE = register("lead_ore", new MineriaOre(2, 4.0F, 5.0F, SoundType.STONE));
	public static final Block TITANE_ORE = register("titane_ore", new MineriaOre(3, 6F, 10F, SoundType.STONE));
	public static final Block COPPER_ORE = register("copper_ore", new MineriaOre(1, 3F, 5F, SoundType.STONE));
	public static final Block LONSDALEITE_ORE = register("lonsdaleite_ore", new MineriaOre(3, 6F, 10F, SoundType.STONE));
	public static final Block NETHER_GOLD_ORE = register("nether_gold_ore", new MineriaOre(2, 1.75F, 1F, SoundType.STONE));
	public static final Block SILVER_ORE = register("silver_ore", new MineriaOre(2, 3F, 5F, SoundType.STONE));
	
	//OreBlocks
	public static final Block LEAD_BLOCK = register("lead_block", new MineriaBlock(Material.IRON, 6.5F, 20F, SoundType.METAL, 2));
	public static final Block TITANE_BLOCK = register("titane_block", new MineriaBlock(Material.IRON, 10F, 15F, SoundType.METAL, 3));
	public static final Block COPPER_BLOCK = register("copper_block", new MineriaBlock(Material.IRON, 5F, 10F, SoundType.METAL, 1));
	public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new MineriaBlock(Material.IRON, 10F, 17.5F, SoundType.METAL, 3));
	public static final Block SILVER_BLOCK = register("silver_block", new MineriaBlock(Material.IRON, 5F, 10F, SoundType.METAL, 2));
	public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new MineriaBlock(Material.IRON, 2.5F, 2F, SoundType.METAL, 3));
	
	//Machines
	public static final Block XP_BLOCK = register("xp_block", new XpBlock());
	public static final Block TITANE_EXTRACTOR = register("titane_extractor", new TitaneExtractorBlock());
	//public static final Block EXTRACTOR = register("extractor", new ExtractorBlock(1, Material.IRON, 1F, 1F, SoundType.METAL));
	public static final Block INFUSER = register("infuser", new InfuserBlock());

	//Plants
	public static final Block PLANTAIN = register("plantain", new PlantBlock(MaterialColor.GRASS));
	public static final Block MINT = register("mint", new PlantBlock(MaterialColor.GRASS));
	public static final Block THYME = register("thyme", new PlantBlock(MaterialColor.GRASS));
	public static final Block NETTLE = register("nettle", new PlantBlock(MaterialColor.GRASS));
	public static final Block PULMONARY = register("pulmonary", new PlantBlock(MaterialColor.GRASS));

	//Other
	public static final Block BLUE_GLOWSTONE = register("blue_glowstone", new Block(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).setLightLevel(value -> 15)));
	public static final Block MINERAL_SAND = register("mineral_sand", new FallingBlock(AbstractBlock.Properties.create(Material.SAND).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(2)));
	public static final Block LEAD_SPIKE = register("lead_spike", new SpikeBlock(2.5F, 2.0F, 2.0F));
	public static final Block COMPRESSED_LEAD_SPIKE = register("compressed_lead_spike", new SpikeBlock(7.0F, 25.0F, 4.0F));
	public static final Block GOLDEN_SILVERFISH_NETHERRACK = register("golden_silverfish_netherrack", new GoldenSilverfishBlock(Blocks.NETHERRACK));
	public static final Block XP_WALL = register("xp_wall", new MineriaBlock(Material.IRON, 2.5F, 5F, SoundType.METAL, 1));
	public static final Block WATER_BARREL = register("water_barrel", new WaterBarrelBlock(8), block -> new WaterBarrelBlock.WaterBarrelBlockItem((WaterBarrelBlock)block, new Item.Properties().maxStackSize(1)));
	public static final Block INFINITE_WATER_BARREL = register("infinite_water_barrel", new WaterBarrelBlock(-1), block -> new WaterBarrelBlock.WaterBarrelBlockItem((WaterBarrelBlock)block, new Item.Properties().maxStackSize(1).rarity(Rarity.EPIC)));

	private static Block register(String name, Block instance)
	{
		return register(name, instance, block -> new BlockItem(block, new Item.Properties().group(Mineria.MINERIA_GROUP)));
	}

	private static Block register(String name, Block instance, Function<Block, BlockItem> blockItem)
	{
		BLOCKS.register(name, () -> instance);
		BLOCK_ITEMS.register(name, () -> blockItem.apply(instance));
		return instance;
	}

	public static BlockItem getItemFromBlock(Block block)
	{
		return (BlockItem) BLOCK_ITEMS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(item -> Objects.equals(item.getRegistryName(), block.getRegistryName()))
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Could not find block item with registry name '" + block.getRegistryName() + "'."));
	}
}
