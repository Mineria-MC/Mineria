package io.github.mineria_mc.mineria.common.registries;

import java.util.HashMap;
import java.util.Map;

import io.github.mineria_mc.mineria.common.item.MineriaItem;
import io.github.mineria_mc.mineria.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class MineriaItemRegistry {
    
    private static final Map<String, Item> ITEMS = new HashMap<String, Item>();

    // Lead
    public static final Item LEAD_INGOT = register("lead_ingot", new MineriaItem());
    public static final Item COMPRESSED_LEAD_INGOT = register("compressed_lead_ingot", new MineriaItem());
    
    // Lonsdaleite
    public static final Item LONSDALEITE = register("lonsdaleite", new MineriaItem());
    
    // Silver
    public static final Item SILVER_INGOT = register("silver_ingot", new MineriaItem());

    // Titane
    public static final Item TITANE_INGOT = register("titane_ingot", new MineriaItem());

    public static void register() {
        for(Map.Entry<String, Item> entry : ITEMS.entrySet()) {
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MODID, entry.getKey()), entry.getValue());
        }
    }

    private static Item register(String itemName, Item item) {
        ITEMS.put(itemName, item);
        return item;
    }
}
