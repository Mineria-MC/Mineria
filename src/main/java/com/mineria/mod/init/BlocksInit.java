package com.mineria.mod.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.blocks.*;
import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.blocks.barrel.WaterBarrelBlock;
import com.mineria.mod.blocks.barrel.copper.CopperWaterBarrelBlock;
import com.mineria.mod.blocks.barrel.golden.GoldenWaterBarrelBlock;
import com.mineria.mod.blocks.barrel.iron.IronFluidBarrelBlock;
import com.mineria.mod.blocks.extractor.ExtractorBlock;
import com.mineria.mod.blocks.infuser.InfuserBlock;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorBlock;
import com.mineria.mod.blocks.xp_block.XpBlock;
import com.mineria.mod.world.feature.SakuraTree;
import com.mineria.mod.world.feature.SpruceYewTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.TallBlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BlocksInit
{
	//Deferred Registries (Blocks and Block Items)
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
	public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);

	private static final Function<Block, BlockItem> DEV_ITEM_FUNC = block -> new BlockItem(block, new Item.Properties().group(Mineria.DEV_GROUP));

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
	public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new LonsdaleiteBlock());
	public static final Block SILVER_BLOCK = register("silver_block", new MineriaBlock(Material.IRON, 5F, 10F, SoundType.METAL, 2));
	public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new MineriaBlock(Material.IRON, 2.5F, 2F, SoundType.METAL, 3));
	
	//Machines
	public static final Block XP_BLOCK = register("xp_block", new XpBlock());
	public static final Block TITANE_EXTRACTOR = register("titane_extractor", new TitaneExtractorBlock());
	public static final Block EXTRACTOR = register("extractor", new ExtractorBlock());
	public static final Block INFUSER = register("infuser", new InfuserBlock());

	//Plants
	public static final Block PLANTAIN = register("plantain", new PlantBlock(MaterialColor.GRASS, false));
	public static final Block MINT = register("mint", new PlantBlock(MaterialColor.GRASS, false));
	public static final Block THYME = register("thyme", new PlantBlock(MaterialColor.GRASS, false));
	public static final Block NETTLE = register("nettle", new PlantBlock(MaterialColor.GRASS, false));
	public static final Block PULMONARY = register("pulmonary", new PlantBlock(MaterialColor.GRASS, false));
	public static final Block RHUBARB = register("rhubarb", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block SENNA = register("senna", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block SENNA_BUSH = register("senna_bush", new PlantBlock(MaterialColor.GRASS, true), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block ELDERBERRY_BUSH = register("elderberry_bush", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable, Growth System
	public static final Block BLACK_ELDERBERRY_BUSH = register("black_elderberry_bush", new FruitPlantBlock(() -> ItemsInit.BLACK_ELDERBERRY), DEV_ITEM_FUNC); // TODO LootTable, Growth System
	public static final Block STRYCHNOS_TOXIFERA = register("strychnos_toxifera", new StrychnosPlantBlock(), DEV_ITEM_FUNC); // TODO LootTable, Review Growth System
	public static final Block STRYCHNOS_NUX_VOMICA = register("strychnos_nux-vomica", new StrychnosPlantBlock(), DEV_ITEM_FUNC); // TODO LootTable, Review Growth System
	public static final Block BELLADONNA = register("belladonna", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block MANDRAKE = register("mandrake", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block LYCIUM_BARBARUM = register("lycium_barbarum", new FruitPlantBlock(() -> ItemsInit.GOJI), DEV_ITEM_FUNC); // TODO LootTable, Growth System
	public static final Block SAUSSUREA_COSTUS = register("saussurea_costus", new SaussureaCostusPlantBlock(), block -> new TallBlockItem(block, new Item.Properties().group(Mineria.DEV_GROUP))); // TODO LootTable
	public static final Block SCHISANDRA_CHINENSIS = register("schisandra_chinensis", new VineBlock(AbstractBlock.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.VINE)), DEV_ITEM_FUNC); // TODO LootTable, Review Growth System
	public static final Block PULSATILLA_CHINENSIS = register("pulsatilla_chinensis", new PlantBlock(MaterialColor.GRASS, false), DEV_ITEM_FUNC); // TODO LootTable

	//Mushrooms
	public static final Block GIROLLE = register("girolle", new PlantBlock(MaterialColor.YELLOW, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block HORN_OF_PLENTY = register("horn_of_plenty", new PlantBlock(MaterialColor.BROWN, false), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block PUFFBALL = register("puffball", new PlantBlock(MaterialColor.WOOL, false), DEV_ITEM_FUNC); // TODO LootTable, Special Use
	public static final Block FLY_AGARIC = Blocks.RED_MUSHROOM;

	// Trees, Leaves...
	public static final Block SPRUCE_YEW_LEAVES = register("spruce_yew_leaves", new LeavesBlock(AbstractBlock.Properties.from(Blocks.OAK_LEAVES)), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block SPRUCE_YEW_SAPLING = register("spruce_yew_sapling", new SaplingBlock(new SpruceYewTree(), AbstractBlock.Properties.from(Blocks.SPRUCE_SAPLING)), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block SAKURA_LEAVES = register("sakura_leaves", new LeavesBlock(AbstractBlock.Properties.from(Blocks.OAK_LEAVES)), DEV_ITEM_FUNC); // TODO LootTable
	public static final Block SAKURA_SAPLING = register("sakura_sapling", new SaplingBlock(new SakuraTree(), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)), DEV_ITEM_FUNC); // TODO LootTable

	//Other
	public static final Block BLUE_GLOWSTONE = register("blue_glowstone", new Block(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).setLightLevel(value -> 15)));
	public static final Block MINERAL_SAND = register("mineral_sand", new FallingBlock(AbstractBlock.Properties.create(Material.SAND).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(2)));
	public static final Block LEAD_SPIKE = register("lead_spike", new SpikeBlock(2.5F, 2.0F, 2.0F));
	public static final Block COMPRESSED_LEAD_SPIKE = register("compressed_lead_spike", new SpikeBlock(7.0F, 25.0F, 4.0F));
	public static final Block GOLDEN_SILVERFISH_NETHERRACK = register("golden_silverfish_netherrack", new GoldenSilverfishBlock(Blocks.NETHERRACK));
	public static final Block XP_WALL = register("xp_wall", new MineriaBlock(Material.IRON, 2.5F, 5F, SoundType.METAL, 1));
	public static final Block WATER_BARREL = register("water_barrel", new WaterBarrelBlock(8), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().maxStackSize(1)));
	public static final Block INFINITE_WATER_BARREL = register("infinite_water_barrel", new WaterBarrelBlock(-1), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().maxStackSize(1).rarity(Rarity.EPIC)));
	public static final Block COPPER_WATER_BARREL = register("copper_water_barrel", new CopperWaterBarrelBlock(), block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().maxStackSize(1)));
	public static final Block IRON_FLUID_BARREL = register("iron_fluid_barrel", new IronFluidBarrelBlock(), block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().maxStackSize(1)));
	public static final Block GOLDEN_WATER_BARREL = register("golden_water_barrel", new GoldenWaterBarrelBlock(), GoldenWaterBarrelBlock.BarrelBlockItem::new);
	public static final Block TNT_BARREL = register("tnt_barrel", new TNTBarrelBlock());
	public static final Block DEBUG_BLOCK = register("debug_block", new DebugBlock(), block -> new BlockItem(block, new Item.Properties()));

	private static Block register(String name, Block instance)
	{
		return register(name, instance, block -> new BlockItem(block, new Item.Properties().group(Mineria.MINERIA_GROUP)));
	}

	private static <T extends Block> T register(String name, T instance, @Nullable Function<T, BlockItem> blockItem)
	{
		BLOCKS.register(name, () -> instance);
		if(blockItem != null) BLOCK_ITEMS.register(name, () -> blockItem.apply(instance));
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
