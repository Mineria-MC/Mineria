package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.util.Constants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

@SuppressWarnings("unused")
public class MineriaBlockRegistry {

    private static final Map<String, Block> BLOCKS = new HashMap<String, Block>();
    private static final Map<String, Item> BLOCK_ITEMS = new HashMap<String, Item>();

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

    private static Properties properties(MapColor color, float hardness, float resistance, SoundType sound) {
        return Properties.of().mapColor(color).strength(hardness, resistance).sound(sound).requiresCorrectToolForDrops();
    }
}
