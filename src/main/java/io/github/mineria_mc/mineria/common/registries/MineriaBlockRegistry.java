package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.block.xp_block.XpBlock;
import io.github.mineria_mc.mineria.util.Constants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

@SuppressWarnings("unused")
public class MineriaBlockRegistry {

    private static final Map<String, Block> BLOCKS = new HashMap<String, Block>();
    private static final Map<String, Item> BLOCK_ITEMS = new HashMap<String, Item>();

    // Stone Ores
    public static final Block LEAD_ORE = register("lead_ore", new DropExperienceBlock(stoneOre(4f, 5f)));
    public static final Block TITANE_ORE = register("titane_ore", new DropExperienceBlock(stoneOre(6f, 10f)));
    public static final Block LONSDALEITE_ORE = register("lonsdaleite_ore", new DropExperienceBlock(stoneOre(6f, 10f), UniformInt.of(4, 10)));
    public static final Block SILVER_ORE = register("silver_ore", new DropExperienceBlock(stoneOre(3f, 5f)));

    // Deepslate Ores
    public static final Block DEEPSLATE_LEAD_ORE = register("deepslate_lead_ore", new DropExperienceBlock(deepslateOre(5.5f, 5.0f)));
    public static final Block DEEPSLATE_TITANE_ORE = register("deepslate_titane_ore", new DropExperienceBlock(deepslateOre(7.5f, 10f)));
    public static final Block DEEPSLATE_LONSDALEITE_ORE = register("deepslate_lonsdaleite_ore", new DropExperienceBlock(deepslateOre(7.5f, 10f), UniformInt.of(4, 10)));
    public static final Block DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", new DropExperienceBlock(deepslateOre(4.5f, 5f)));

    // Ores Blocks
    public static final Block LEAD_BLOCK = register("lead_block", new Block(properties(MapColor.METAL, 6.5f, 20f, SoundType.METAL)));
    public static final Block TITANE_BLOCK = register("titane_block", new Block(properties(MapColor.METAL, 10f, 15f, SoundType.METAL)));
    public static final Block LONSDALEITE_BLOCK = register("lonsdaleite_block", new Block(properties(MapColor.METAL, 10f, 17.5f, SoundType.METAL)));
    public static final Block SILVER_BLOCK = register("silver_block", new Block(properties(MapColor.METAL, 5f, 10f, SoundType.METAL)));
    public static final Block COMPRESSED_LEAD_BLOCK = register("compressed_lead_block", new Block(properties(MapColor.METAL, 2.5f, 2f, SoundType.METAL)));

    // Machines
    public static final Block XP_BLOCK = register("xp_block", new XpBlock());

    public static void register() {
        // Register Blocks
        for(Map.Entry<String, Block> entry : BLOCKS.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }

        // Register BlockItems
        for(Map.Entry<String, Item> entry : BLOCK_ITEMS.entrySet()) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static Block register(String blockName, Block block) {
        BLOCKS.put(blockName, block);
        BLOCK_ITEMS.put(blockName, new BlockItem(block, new FabricItemSettings()));
        return block;
    }

    // Properties quick functions

    private static Properties stoneOre(float hardness, float resistance) {
        return Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(hardness, resistance).sound(SoundType.STONE).requiresCorrectToolForDrops();
    }

    private static Properties deepslateOre(float hardness, float resistance) {
        return Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(hardness, resistance).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();
    }

    private static Properties properties(MapColor color, float hardness, float resistance, SoundType sound) {
        return Properties.of().mapColor(color).strength(hardness, resistance).sound(sound).requiresCorrectToolForDrops();
    }
}
