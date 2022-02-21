package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.*;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableBlock;
import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.WaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.copper.CopperWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.golden.GoldenWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.iron.IronFluidBarrelBlock;
import com.mineria.mod.common.blocks.distiller.DistillerBlock;
import com.mineria.mod.common.blocks.extractor.ExtractorBlock;
import com.mineria.mod.common.blocks.infuser.InfuserBlock;
import com.mineria.mod.common.blocks.ritual_table.RitualTableBlock;
import com.mineria.mod.common.blocks.titane_extractor.TitaneExtractorBlock;
import com.mineria.mod.common.blocks.xp_block.XpBlock;
import com.mineria.mod.common.world.feature.SakuraTree;
import com.mineria.mod.common.world.feature.SpruceYewTree;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
public class MineriaBlocks
{
	// Deferred Registries (Blocks and Block Items)
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Mineria.MODID);
	public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mineria.MODID);

	// Ores
	public static final Block LEAD_ORE = register("lead_ore", new MineriaOre(2, 4.0F, 5.0F, SoundType.STONE));
	public static final Block TITANE_ORE = register("titane_ore", new MineriaOre(3, 6F, 10F, SoundType.STONE));
	public static final Block COPPER_ORE = register("copper_ore", new MineriaOre(1, 3F, 5F, SoundType.STONE));
	public static final Block LONSDALEITE_ORE = register("lonsdaleite_ore", new MineriaOre(3, 6F, 10F, SoundType.STONE));
	public static final Block NETHER_GOLD_ORE = register("nether_gold_ore", new MineriaOre(2, 1.75F, 1F, SoundType.STONE));
	public static final Block SILVER_ORE = register("silver_ore", new MineriaOre(2, 3F, 5F, SoundType.STONE));
	
	// Ore Blocks
	public static final Block LEAD_BLOCK = register("lead_block", new MineriaBlock(Material.METAL, 6.5F, 20F, SoundType.METAL, 2));
	public static final Block TITANE_BLOCK = register("titane_block", new MineriaBlock(Material.METAL, 10F, 15F, SoundType.METAL, 3));
	public static final Block COPPER_BLOCK = register("copper_block", new MineriaBlock(Material.METAL, 5F, 10F, SoundType.METAL, 1));
	public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new Block(Properties.of(Material.METAL).strength(10F, 17.5F).sound(SoundType.METAL).noOcclusion().harvestTool(ToolType.PICKAXE).harvestLevel(3)));
	public static final Block SILVER_BLOCK = register("silver_block", new MineriaBlock(Material.METAL, 5F, 10F, SoundType.METAL, 2));
	public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new MineriaBlock(Material.METAL, 2.5F, 2F, SoundType.METAL, 3));
	
	// Machines
	public static final Block XP_BLOCK = register("xp_block", new XpBlock());
	public static final Block TITANE_EXTRACTOR = register("titane_extractor", new TitaneExtractorBlock());
	public static final Block EXTRACTOR = register("extractor", new ExtractorBlock());
	public static final Block INFUSER = register("infuser", new InfuserBlock(), Mineria.APOTHECARY_GROUP);
	public static final Block DISTILLER = register("distiller", new DistillerBlock(), Mineria.APOTHECARY_GROUP);
	public static final Block APOTHECARY_TABLE = register("apothecary_table", new ApothecaryTableBlock(), Mineria.APOTHECARY_GROUP);

	// Plants
	public static final Block PLANTAIN = registerCompostable("plantain", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.4F);
	public static final Block MINT = registerCompostable("mint", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.5F);
	public static final Block THYME = registerCompostable("thyme", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.65F);
	public static final Block NETTLE = registerCompostable("nettle", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.7F);
	public static final Block PULMONARY = registerCompostable("pulmonary", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.5F);
	public static final Block RHUBARB = registerCompostable("rhubarb", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.4F);
	public static final Block SENNA = registerCompostable("senna", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.4F);
	public static final Block SENNA_BUSH = registerCompostable("senna_bush", new PlantBlock(MaterialColor.PLANT, true), Mineria.APOTHECARY_GROUP, 0.5F);
	public static final Block ELDERBERRY_BUSH = registerCompostable("elderberry_bush", new FruitPlantBlock(() -> MineriaItems.ELDERBERRY, 5, false), Mineria.APOTHECARY_GROUP, 0.5F);
	public static final Block BLACK_ELDERBERRY_BUSH = registerCompostable("black_elderberry_bush", new FruitPlantBlock(() -> MineriaItems.BLACK_ELDERBERRY, 5, true), Mineria.APOTHECARY_GROUP, 0.5F);
	public static final Block STRYCHNOS_TOXIFERA = registerCompostable("strychnos_toxifera", new StrychnosPlantBlock(), Mineria.APOTHECARY_GROUP, 0.85F);
	public static final Block STRYCHNOS_NUX_VOMICA = registerCompostable("strychnos_nux-vomica", new StrychnosPlantBlock(), Mineria.APOTHECARY_GROUP, 0.8F);
	public static final Block BELLADONNA = registerCompostable("belladonna", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.65F);
	public static final Block MANDRAKE = registerCompostable("mandrake", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.6F);
	public static final Block LYCIUM_CHINENSE = registerCompostable("lycium_chinense", new LyciumChinenseBlock(), Mineria.APOTHECARY_GROUP, 0.7F);
	public static final Block SAUSSUREA_COSTUS = registerCompostable("saussurea_costus", new SaussureaCostusPlantBlock(), block -> new TallBlockItem(block, new Item.Properties().tab(Mineria.APOTHECARY_GROUP)), 0.6F);
	public static final Block SCHISANDRA_CHINENSIS = registerCompostable("schisandra_chinensis", new VineBlock(Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE)), Mineria.APOTHECARY_GROUP, 0.7F);
	public static final Block PULSATILLA_CHINENSIS = registerCompostable("pulsatilla_chinensis", new PlantBlock(MaterialColor.PLANT, false), Mineria.APOTHECARY_GROUP, 0.65F);

	// Mushrooms
	public static final Block GIROLLE = registerCompostable("girolle", new MineriaMushroomBlock(MaterialColor.COLOR_YELLOW), 0.55F);
	public static final Block HORN_OF_PLENTY = registerCompostable("horn_of_plenty", new MineriaMushroomBlock(MaterialColor.COLOR_BROWN), 0.55F);
	public static final Block PUFFBALL = registerCompostable("puffball", new PuffballBlock(), 0.55F);
	public static final Block FLY_AGARIC = Blocks.RED_MUSHROOM;

	// Trees, Leaves...
	public static final Block SPRUCE_YEW_LEAVES = registerCompostable("spruce_yew_leaves", new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)), 0.4F);
	public static final Block SPRUCE_YEW_SAPLING = registerCompostable("spruce_yew_sapling", new SaplingBlock(new SpruceYewTree(), Properties.copy(Blocks.SPRUCE_SAPLING)), 0.4F);
	public static final Block SAKURA_LEAVES = registerCompostable("sakura_leaves", new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)), 0.4F);
	public static final Block SAKURA_SAPLING = registerCompostable("sakura_sapling", new SaplingBlock(new SakuraTree(), Properties.copy(Blocks.OAK_SAPLING)), 0.4F);

	// Pots
	public static final Block POTTED_PLANTAIN = registerFlowerPot("potted_plantain", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.PLANTAIN, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_MINT = registerFlowerPot("potted_mint", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.MINT, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_THYME = registerFlowerPot("potted_thyme", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.THYME, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_NETTLE = registerFlowerPot("potted_nettle", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.NETTLE, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_PULMONARY = registerFlowerPot("potted_pulmonary", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.PULMONARY, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_RHUBARB = registerFlowerPot("potted_rhubarb", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.RHUBARB, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_SENNA = registerFlowerPot("potted_senna", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.SENNA, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_BELLADONNA = registerFlowerPot("potted_belladonna", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.BELLADONNA, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_MANDRAKE = registerFlowerPot("potted_mandrake", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.MANDRAKE, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_PULSATILLA_CHINENSIS = registerFlowerPot("potted_pulsatilla_chinensis", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.PULSATILLA_CHINENSIS, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_SPRUCE_YEW_SAPLING = registerFlowerPot("potted_spruce_yew_sapling", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.SPRUCE_YEW_SAPLING, Properties.copy(Blocks.FLOWER_POT)));
	public static final Block POTTED_SAKURA_SAPLING = registerFlowerPot("potted_sakura_sapling", new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> MineriaBlocks.SAKURA_SAPLING, Properties.copy(Blocks.FLOWER_POT)));

	// Other
	public static final Block BLUE_GLOWSTONE = register("blue_glowstone", new Block(Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).lightLevel(value -> 15)));
	public static final Block MINERAL_SAND = register("mineral_sand", new FallingBlock(Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL).harvestLevel(2)));
	public static final Block LEAD_SPIKE = register("lead_spike", new SpikeBlock(2.5F, 2.0F, 2.0F));
	public static final Block COMPRESSED_LEAD_SPIKE = register("compressed_lead_spike", new SpikeBlock(7.0F, 25.0F, 4.0F));
	public static final Block GOLDEN_SILVERFISH_NETHERRACK = register("golden_silverfish_netherrack", new GoldenSilverfishBlock(Blocks.NETHERRACK));
	public static final Block XP_WALL = register("xp_wall", new MineriaBlock(Material.METAL, 2.5F, 5F, SoundType.METAL, 1));
	public static final Block WATER_BARREL = registerBlock("water_barrel", new WaterBarrelBlock(8), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1)));
	public static final Block INFINITE_WATER_BARREL = registerBlock("infinite_water_barrel", new WaterBarrelBlock(-1), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().stacksTo(1).tab(Mineria.MINERIA_GROUP).rarity(Rarity.EPIC)));
	public static final Block COPPER_WATER_BARREL = registerBlock("copper_water_barrel", new CopperWaterBarrelBlock(), block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1)));
	public static final Block IRON_FLUID_BARREL = registerBlock("iron_fluid_barrel", new IronFluidBarrelBlock(), block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().tab(Mineria.MINERIA_GROUP).stacksTo(1)));
	public static final Block GOLDEN_WATER_BARREL = registerBlock("golden_water_barrel", new GoldenWaterBarrelBlock(), GoldenWaterBarrelBlock.BarrelBlockItem::new);
	public static final Block TNT_BARREL = register("tnt_barrel", new TNTBarrelBlock());
	public static final Block DEBUG_BLOCK = registerBlock("debug_block", new DebugBlock(), block -> new BlockItem(block, new Item.Properties()));
	public static final Block RITUAL_TABLE = register("ritual_table", new RitualTableBlock());
	public static final Block FIRE_ELEMENTARY_STONE = register("fire_elementary_stone", new ElementaryStoneBlock());
	public static final Block WATER_ELEMENTARY_STONE = register("water_elementary_stone", new ElementaryStoneBlock());
	public static final Block AIR_ELEMENTARY_STONE = register("air_elementary_stone", new ElementaryStoneBlock());
	public static final Block GROUND_ELEMENTARY_STONE = register("ground_elementary_stone", new ElementaryStoneBlock());
	public static final Block PUFFBALL_POWDER = registerBlock("puffball_powder", new PuffballPowderBlock(), null);

	private static Block register(String name, Block instance)
	{
		return registerBlock(name, instance, block -> new BlockItem(block, new Item.Properties().tab(Mineria.MINERIA_GROUP)));
	}

	private static Block register(String name, Block instance, ItemGroup group)
	{
		return registerBlock(name, instance, block -> new BlockItem(block, new Item.Properties().tab(group)));
	}

	private static <T extends Block> T registerBlock(String name, T instance, @Nullable Function<T, BlockItem> blockItem)
	{
		BLOCKS.register(name, () -> instance);
		if(blockItem != null) BLOCK_ITEMS.register(name, () -> blockItem.apply(instance));
		return instance;
	}

	private static <T extends FlowerPotBlock> T registerFlowerPot(String name, T instance)
	{
		((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(new ResourceLocation(Mineria.MODID, name.substring(name.indexOf('_') + 1)), () -> instance);
		return registerBlock(name, instance, null);
	}

	private static <T extends Block> T registerCompostable(String name, T instance, float compostValue)
	{
		return registerCompostable(name, instance, block -> new BlockItem(block, new Item.Properties().tab(Mineria.MINERIA_GROUP)), compostValue);
	}

	private static <T extends Block> T registerCompostable(String name, T instance, ItemGroup group, float compostValue)
	{
		return registerCompostable(name, instance, block -> new BlockItem(block, new Item.Properties().tab(group)), compostValue);
	}

	private static <T extends Block> T registerCompostable(String name, T instance, @Nonnull Function<T, BlockItem> blockItemFactory, float compostValue)
	{
		BLOCKS.register(name, () -> instance);
		Item blockItem = blockItemFactory.apply(instance);
		BLOCK_ITEMS.register(name, () -> blockItem);
		ComposterBlock.COMPOSTABLES.put(blockItem, compostValue);
		return instance;
	}

	public static BlockItem getItemFromBlock(Block block)
	{
		return BLOCK_ITEMS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(item -> Objects.equals(item.getRegistryName(), block.getRegistryName()))
				.findFirst()
				.map(BlockItem.class::cast)
				.orElseThrow(() -> new NullPointerException("Could not find block item with registry name '" + block.getRegistryName() + "'."));
	}

	public static final class Tags
	{
		public static final ITag.INamedTag<Block> PLANTS = BlockTags.bind("mineria:plants");
		public static final ITag.INamedTag<Block> ALLOWED_BLOCKS_RITUAL_TABLE = BlockTags.bind("mineria:allowed_blocks_ritual_table");
	}
}
