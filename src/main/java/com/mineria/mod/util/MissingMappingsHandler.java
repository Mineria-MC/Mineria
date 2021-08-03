package com.mineria.mod.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappingsHandler
{
    private static final Table<IForgeRegistry<?>, ResourceLocation, ResourceLocation> OLD_TO_NEW_MAPPINGS = Util.make(HashBasedTable.create(), table -> {
        table.put(ForgeRegistries.ITEMS, modLoc("mineria_xp_orb"), modLoc("xp_orb"));
        table.put(ForgeRegistries.BLOCKS, modLoc("elderberry"), new ResourceLocation("air"));
        table.put(ForgeRegistries.BLOCKS, modLoc("pulsatilla_vulgaris"), modLoc("pulsatilla_chinensis"));
        table.put(ForgeRegistries.ITEMS, modLoc("pulsatilla_vulgaris"), modLoc("pulsatilla_chinensis"));
        table.put(ForgeRegistries.ITEMS, modLoc("orange_blossom"), modLoc("orange-blossom"));
        table.put(ForgeRegistries.ITEMS, modLoc("distilled_orange-blossom"), modLoc("distilled_orange-blossom_water"));
    });

    @SubscribeEvent
    public static void fixMissingItemMappings(RegistryEvent.MissingMappings<Item> event)
    {
        if(event.getAllMappings() != null)
        {
            List<RegistryEvent.MissingMappings.Mapping<Item>> MOD_MAPPINGS = getModMappings(event);

            if(!MOD_MAPPINGS.isEmpty())
            {
                Mineria.LOGGER.info("Fixing missing Item mappings from modid mineria...");
                OLD_TO_NEW_MAPPINGS.row(ForgeRegistries.ITEMS).forEach((oldName, newName) -> MOD_MAPPINGS.forEach(mapping -> {
                    if(mapping.key.equals(oldName))
                    {
                        Item newItem = ForgeRegistries.ITEMS.getValue(newName);
                        if(newItem == null)
                            Mineria.LOGGER.error("Failed to fix " + mapping.key + ", could not find item with key " + newName + " ! Please report this error to the mod author.");
                        else
                            mapping.remap(newItem);
                    }
                }));
            }
        }
    }

    @SubscribeEvent
    public static void fixMissingBlockMappings(RegistryEvent.MissingMappings<Block> event)
    {
        if(event.getAllMappings() != null)
        {
            List<RegistryEvent.MissingMappings.Mapping<Block>> MOD_MAPPINGS = getModMappings(event);

            if(!MOD_MAPPINGS.isEmpty())
            {
                Mineria.LOGGER.info("Fixing missing Block mappings from modid mineria...");
                OLD_TO_NEW_MAPPINGS.row(ForgeRegistries.BLOCKS).forEach((oldName, newName) -> MOD_MAPPINGS.forEach(mapping -> {
                    if(mapping.key.equals(oldName))
                    {
                        Block newBlock = ForgeRegistries.BLOCKS.getValue(newName);
                        if(newBlock == null)
                            Mineria.LOGGER.error("Failed to fix " + mapping.key + ", could not find block with key " + newName + " ! Please report this error to the mod author.");
                        else
                            mapping.remap(newBlock);
                    }
                }));
            }
        }
    }

    private static <T extends IForgeRegistryEntry<T>> List<RegistryEvent.MissingMappings.Mapping<T>> getModMappings(RegistryEvent.MissingMappings<T> event)
    {
        return event.getAllMappings().stream().filter(mapping -> mapping.key.getNamespace().equals("mineria")).collect(Collectors.toList());
    }

    private static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(References.MODID, name);
    }
}
