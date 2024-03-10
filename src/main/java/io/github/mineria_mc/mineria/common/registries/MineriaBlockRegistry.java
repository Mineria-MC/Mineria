package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.util.Constants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class MineriaBlockRegistry {

    private static final Map<String, Block> BLOCKS = new HashMap<String, Block>();
    private static final Map<String, Item> BLOCK_ITEMS = new HashMap<String, Item>();

    public static void register() {
        // Register Blocks
        for(Map.Entry<String, Block> entry : BLOCKS.entrySet()) {
            Registry.register(Registries.BLOCK, new Identifier(Constants.MODID, entry.getKey()), entry.getValue());
        }

        // Register BlockItems
        for(Map.Entry<String, Item> entry : BLOCK_ITEMS.entrySet()) {
            Registry.register(Registries.ITEM, new Identifier(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static Block register(String blockName, Block block) {
        BLOCKS.put(blockName, block);
        BLOCK_ITEMS.put(blockName, new BlockItem(block, new FabricItemSettings()));
        return block;
    }
}
