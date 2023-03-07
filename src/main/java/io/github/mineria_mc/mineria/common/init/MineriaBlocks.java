package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.*;
import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.WaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.copper.CopperWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.diamond.DiamondFluidBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.golden.GoldenWaterBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.barrel.iron.IronFluidBarrelBlock;
import io.github.mineria_mc.mineria.common.blocks.distiller.DistillerBlock;
import io.github.mineria_mc.mineria.common.blocks.extractor.ExtractorBlock;
import io.github.mineria_mc.mineria.common.blocks.infuser.InfuserBlock;
import io.github.mineria_mc.mineria.common.blocks.ritual_table.RitualTableBlock;
import io.github.mineria_mc.mineria.common.blocks.titane_extractor.TitaneExtractorBlock;
import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlock;
import io.github.mineria_mc.mineria.common.world.feature.SakuraTree;
import io.github.mineria_mc.mineria.common.world.feature.SpruceYewTree;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MineriaBlocks {
    // Deferred Registries (Blocks and Block Items)
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Mineria.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mineria.MODID);

    // Stone ores
    public static final RegistryObject<Block> LEAD_ORE = register("lead_ore", () -> new DropExperienceBlock(stoneOre(4F, 5F)));
    public static final RegistryObject<Block> TITANE_ORE = register("titane_ore", () -> new DropExperienceBlock(stoneOre(6F, 10F)));
    public static final RegistryObject<Block> LONSDALEITE_ORE = register("lonsdaleite_ore", () -> new DropExperienceBlock(stoneOre(6F, 10F), UniformInt.of(4, 10)));
    public static final RegistryObject<Block> NETHER_GOLD_ORE = register("nether_gold_ore", () -> new DropExperienceBlock(stoneOre(1.75F, 1F)));
    public static final RegistryObject<Block> SILVER_ORE = register("silver_ore", () -> new DropExperienceBlock(stoneOre(3F, 5F)));

    // Deepslate ores
    public static final RegistryObject<Block> DEEPSLATE_LEAD_ORE = register("deepslate_lead_ore", () -> new DropExperienceBlock(deepslate(5.5F, 5.0F)));
    public static final RegistryObject<Block> DEEPSLATE_TITANE_ORE = register("deepslate_titane_ore", () -> new DropExperienceBlock(deepslate(7.5F, 10F)));
    public static final RegistryObject<Block> DEEPSLATE_LONSDALEITE_ORE = register("deepslate_lonsdaleite_ore", () -> new DropExperienceBlock(deepslate(7.5F, 10F), UniformInt.of(4, 10)));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", () -> new DropExperienceBlock(deepslate(4.5F, 5F)));

    // Ore Blocks
    public static final RegistryObject<Block> LEAD_BLOCK = register("lead_block", () -> new Block(properties(Material.METAL, 6.5F, 20F, SoundType.METAL)));
    public static final RegistryObject<Block> TITANE_BLOCK = register("titane_block", () -> new Block(properties(Material.METAL, 10F, 15F, SoundType.METAL)));
    public static final RegistryObject<Block> LONSDALEITE_BLOCK = register("lonsdaleite_block", () -> new Block(Properties.of(Material.METAL).strength(10F, 17.5F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block", () -> new Block(properties(Material.METAL, 5F, 10F, SoundType.METAL)));
    public static final RegistryObject<Block> COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", () -> new Block(properties(Material.METAL, 2.5F, 2F, SoundType.METAL)));

    // Machines
    public static final RegistryObject<Block> XP_BLOCK = register("xp_block", XpBlock::new);
    public static final RegistryObject<Block> TITANE_EXTRACTOR = register("titane_extractor", TitaneExtractorBlock::new);
    public static final RegistryObject<Block> EXTRACTOR = register("extractor", ExtractorBlock::new);
    public static final RegistryObject<Block> INFUSER = register("infuser", InfuserBlock::new);
    public static final RegistryObject<Block> DISTILLER = register("distiller", DistillerBlock::new);
    public static final RegistryObject<Block> APOTHECARY_TABLE = register("apothecary_table", ApothecaryTableBlock::new);

    // Plants
    public static final RegistryObject<Block> PLANTAIN = registerCompostable("plantain", () -> new PlantBlock(MaterialColor.PLANT, false), 0.4F);
    public static final RegistryObject<Block> MINT = registerCompostable("mint", () -> new PlantBlock(MaterialColor.PLANT, false), 0.5F);
    public static final RegistryObject<Block> THYME = registerCompostable("thyme", () -> new PlantBlock(MaterialColor.PLANT, false), 0.65F);
    public static final RegistryObject<Block> NETTLE = registerCompostable("nettle", () -> new PlantBlock(MaterialColor.PLANT, false), 0.7F);
    public static final RegistryObject<Block> PULMONARY = registerCompostable("pulmonary", () -> new PlantBlock(MaterialColor.PLANT, false), 0.5F);
    public static final RegistryObject<Block> RHUBARB = registerCompostable("rhubarb", () -> new PlantBlock(MaterialColor.PLANT, false), 0.4F);
    public static final RegistryObject<Block> SENNA = registerCompostable("senna", () -> new PlantBlock(MaterialColor.PLANT, false), 0.4F);
    public static final RegistryObject<Block> SENNA_BUSH = registerCompostable("senna_bush", () -> new PlantBlock(MaterialColor.PLANT, true), 0.5F);
    public static final RegistryObject<Block> ELDERBERRY_BUSH = registerCompostable("elderberry_bush", () -> new FruitPlantBlock(MineriaItems.ELDERBERRY, 5, false), 0.5F);
    public static final RegistryObject<Block> BLACK_ELDERBERRY_BUSH = registerCompostable("black_elderberry_bush", () -> new FruitPlantBlock(MineriaItems.BLACK_ELDERBERRY, 5, true), 0.5F);
    public static final RegistryObject<Block> STRYCHNOS_TOXIFERA = registerCompostable("strychnos_toxifera", StrychnosPlantBlock::new, 0.85F);
    public static final RegistryObject<Block> STRYCHNOS_NUX_VOMICA = registerCompostable("strychnos_nux-vomica", StrychnosPlantBlock::new, 0.8F);
    public static final RegistryObject<Block> BELLADONNA = registerCompostable("belladonna", () -> new PlantBlock(MaterialColor.PLANT, false), 0.65F);
    public static final RegistryObject<Block> MANDRAKE = registerCompostable("mandrake", () -> new PlantBlock(MaterialColor.PLANT, false), 0.6F);
    public static final RegistryObject<Block> LYCIUM_CHINENSE = registerCompostable("lycium_chinense", LyciumChinenseBlock::new, 0.7F);
    public static final RegistryObject<Block> SAUSSUREA_COSTUS = registerCompostable("saussurea_costus", SaussureaCostusPlantBlock::new, block -> new DoubleHighBlockItem(block, new Item.Properties()), 0.6F);
    public static final RegistryObject<Block> SCHISANDRA_CHINENSIS = registerCompostable("schisandra_chinensis", () -> new VineBlock(Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE)), 0.7F);
    public static final RegistryObject<Block> PULSATILLA_CHINENSIS = registerCompostable("pulsatilla_chinensis", () -> new PlantBlock(MaterialColor.PLANT, false), 0.65F);

    // Mushrooms
    public static final RegistryObject<Block> GIROLLE = registerCompostable("girolle", () -> new MineriaMushroomBlock(MaterialColor.COLOR_YELLOW), 0.55F);
    public static final RegistryObject<Block> HORN_OF_PLENTY = registerCompostable("horn_of_plenty", () -> new MineriaMushroomBlock(MaterialColor.COLOR_BROWN), 0.55F);
    public static final RegistryObject<Block> PUFFBALL = registerCompostable("puffball", PuffballBlock::new, 0.55F);
    public static final Block FLY_AGARIC = Blocks.RED_MUSHROOM;

    // Trees, Leaves...
    public static final RegistryObject<Block> SPRUCE_YEW_LEAVES = registerCompostable("spruce_yew_leaves", () -> new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)), 0.4F);
    public static final RegistryObject<Block> SPRUCE_YEW_SAPLING = registerCompostable("spruce_yew_sapling", () -> new SaplingBlock(new SpruceYewTree(), Properties.copy(Blocks.SPRUCE_SAPLING)), 0.4F);
    public static final RegistryObject<Block> SAKURA_LEAVES = registerCompostable("sakura_leaves", () -> new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)), 0.4F);
    public static final RegistryObject<Block> SAKURA_SAPLING = registerCompostable("sakura_sapling", () -> new SaplingBlock(new SakuraTree(), Properties.copy(Blocks.OAK_SAPLING)), 0.4F);

    // Pots
    public static final RegistryObject<Block> POTTED_PLANTAIN = registerFlowerPot("potted_plantain", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.PLANTAIN, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_MINT = registerFlowerPot("potted_mint", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.MINT, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_THYME = registerFlowerPot("potted_thyme", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.THYME, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_NETTLE = registerFlowerPot("potted_nettle", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.NETTLE, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_PULMONARY = registerFlowerPot("potted_pulmonary", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.PULMONARY, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_RHUBARB = registerFlowerPot("potted_rhubarb", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.RHUBARB, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_SENNA = registerFlowerPot("potted_senna", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.SENNA, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_BELLADONNA = registerFlowerPot("potted_belladonna", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.BELLADONNA, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_MANDRAKE = registerFlowerPot("potted_mandrake", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.MANDRAKE, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_PULSATILLA_CHINENSIS = registerFlowerPot("potted_pulsatilla_chinensis", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.PULSATILLA_CHINENSIS, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_SPRUCE_YEW_SAPLING = registerFlowerPot("potted_spruce_yew_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.SPRUCE_YEW_SAPLING, Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_SAKURA_SAPLING = registerFlowerPot("potted_sakura_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MineriaBlocks.SAKURA_SAPLING, Properties.copy(Blocks.FLOWER_POT)));

    // Other
    public static final RegistryObject<Block> BLUE_GLOWSTONE = register("blue_glowstone", () -> new Block(Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).lightLevel(value -> 15)));
    public static final RegistryObject<Block> MINERAL_SAND = register("mineral_sand", () -> new FallingBlock(Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> LEAD_SPIKE = register("lead_spike", () -> new SpikeBlock(2.5F, 2.0F, 2.0F));
    public static final RegistryObject<Block> COMPRESSED_LEAD_SPIKE = register("compressed_lead_spike", () -> new SpikeBlock(7.0F, 25.0F, 4.0F));
    public static final RegistryObject<Block> GOLDEN_SILVERFISH_NETHERRACK = register("golden_silverfish_netherrack", () -> new GoldenSilverfishBlock(Blocks.NETHERRACK));
    public static final RegistryObject<Block> XP_WALL = register("xp_wall", () -> new Block(properties(Material.METAL, 2.5F, 5F, SoundType.METAL)));
    public static final RegistryObject<Block> WATER_BARREL = registerBlock("water_barrel", () -> new WaterBarrelBlock(8), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Block> INFINITE_WATER_BARREL = registerBlock("infinite_water_barrel", () -> new WaterBarrelBlock(-1), block -> new WaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Block> COPPER_WATER_BARREL = registerBlock("copper_water_barrel", CopperWaterBarrelBlock::new, block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Block> IRON_FLUID_BARREL = registerBlock("iron_fluid_barrel", IronFluidBarrelBlock::new, block -> new AbstractWaterBarrelBlock.WaterBarrelBlockItem<>(block, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Block> GOLDEN_WATER_BARREL = registerBlock("golden_water_barrel", GoldenWaterBarrelBlock::new, GoldenWaterBarrelBlock.BarrelBlockItem::new);
    public static final RegistryObject<Block> DIAMOND_FLUID_BARREL = registerBlock("diamond_fluid_barrel", DiamondFluidBarrelBlock::new, DiamondFluidBarrelBlock.BarrelBlockItem::new);
    public static final RegistryObject<Block> TNT_BARREL = register("tnt_barrel", TNTBarrelBlock::new);
    public static final RegistryObject<Block> DEBUG_BLOCK = registerBlock("debug_block", DebugBlock::new, block -> new BlockItem(block, new Item.Properties()));
    public static final RegistryObject<Block> RITUAL_TABLE = register("ritual_table", RitualTableBlock::new);
    public static final RegistryObject<Block> FIRE_ELEMENTARY_STONE = register("fire_elementary_stone", ElementaryStoneBlock::new);
    public static final RegistryObject<Block> WATER_ELEMENTARY_STONE = register("water_elementary_stone", ElementaryStoneBlock::new);
    public static final RegistryObject<Block> AIR_ELEMENTARY_STONE = register("air_elementary_stone", ElementaryStoneBlock::new);
    public static final RegistryObject<Block> GROUND_ELEMENTARY_STONE = register("ground_elementary_stone", ElementaryStoneBlock::new);
    public static final RegistryObject<Block> PUFFBALL_POWDER = registerBlock("puffball_powder", PuffballPowderBlock::new, null);

    private static RegistryObject<Block> register(String name, Supplier<Block> instance) {
        return registerBlock(name, instance, block -> new BlockItem(block, new Item.Properties()));
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> blockSup, @Nullable Function<T, ? extends Item> blockItem) {
        RegistryObject<Block> obj = BLOCKS.register(name, blockSup);
        if (blockItem != null) {
            BLOCK_ITEMS.register(name, () -> blockItem.apply((T) obj.get()));
        }
        return obj;
    }

    private static <T extends FlowerPotBlock> RegistryObject<Block> registerFlowerPot(String name, Supplier<T> instance) {
        RegistryObject<Block> obj = registerBlock(name, instance, null);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(new ResourceLocation(Mineria.MODID, name.substring(name.indexOf('_') + 1)), obj);
        return obj;
    }

    private static <T extends Block> RegistryObject<Block> registerCompostable(String name, Supplier<T> instance, float compostValue) {
        return registerCompostable(name, instance, block -> new BlockItem(block, new Item.Properties()), compostValue);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> RegistryObject<Block> registerCompostable(String name, Supplier<T> blockSup, @Nonnull Function<T, BlockItem> blockItemFactory, float compostValue) {
        RegistryObject<Block> obj = BLOCKS.register(name, blockSup);
        RegistryObject<Item> item = BLOCK_ITEMS.register(name, () -> blockItemFactory.apply((T) obj.get()));
        MineriaItems.POST_INIT_QUEUE.add(() -> ComposterBlock.COMPOSTABLES.put(item.get(), compostValue));
        return obj;
    }

    private static Properties stoneOre(float hardness, float resistance) {
        return Properties.of(Material.STONE).strength(hardness, resistance).sound(SoundType.STONE).requiresCorrectToolForDrops();
    }

    private static Properties deepslate(float hardness, float resistance) {
        return Properties.of(Material.STONE).color(MaterialColor.DEEPSLATE).strength(hardness, resistance).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();
    }

    private static Properties properties(Material material, float hardness, float resistance, SoundType sound) {
        return Properties.of(material).strength(hardness, resistance).sound(sound).requiresCorrectToolForDrops();
    }

    public static BlockItem getItemFromBlock(Block block) {
        return BLOCK_ITEMS.getEntries().stream()
                .filter(obj -> obj.getId().equals(ForgeRegistries.BLOCKS.getKey(block)))
                .map(RegistryObject::get)
                .findFirst()
                .map(BlockItem.class::cast)
                .orElseThrow(() -> new NullPointerException("Could not find block item with registry name '" + ForgeRegistries.BLOCKS.getKey(block) + "'."));
    }

    public static final class Tags {
        public static final TagKey<Block> LEAD_ORES = BLOCKS.createTagKey("lead_ores");
        public static final TagKey<Block> SILVER_ORES = BLOCKS.createTagKey("silver_ores");
        public static final TagKey<Block> TITANE_ORES = BLOCKS.createTagKey("titane_ores");
        public static final TagKey<Block> LONSDALEITE_ORES = BLOCKS.createTagKey("lonsdaleite_ores");

        public static final TagKey<Block> PLANTS = BLOCKS.createTagKey("plants");
        public static final TagKey<Block> ALLOWED_BLOCKS_RITUAL_TABLE = BLOCKS.createTagKey("allowed_blocks_ritual_table");
        public static final TagKey<Block> MINEABLE_WITH_BILLHOOK = BLOCKS.createTagKey("mineable/billhook");
    }
}
