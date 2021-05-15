package com.mineria.mod.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = References.MODID)
public class MissingMappingHandler
{
    private static final Table<IForgeRegistry<?>, String, String> OLD_TO_NEW_MAPPINGS = MineriaUtils.make(HashBasedTable.create(), table -> {
        table.put(ForgeRegistries.ENTITIES, "golden_fish", "golden_silverfish");
        table.put(ForgeRegistries.BLOCKS, "lit_titane_extractor", "titane_extractor");
        table.put(ForgeRegistries.BLOCKS, "lit_infuser", "infuser");
        table.put(ForgeRegistries.ITEMS, "lit_titane_extractor", "titane_extractor");
        table.put(ForgeRegistries.ITEMS, "lit_infuser", "infuser");
        table.put(ForgeRegistries.ITEMS, "mineria_xp_orb", "xp_orb");
    });

    @SubscribeEvent
    public static void fixEntityMappings(RegistryEvent.MissingMappings<EntityEntry> event)
    {
        if(event.getAllMappings() != null)
        {
            final List<Mapping<EntityEntry>> MOD_MAPPINGS = getMappings(event);

            if(!MOD_MAPPINGS.isEmpty())
            {
                Mineria.LOGGER.info("Fixing missing entity mappings from modid mineria...");

                OLD_TO_NEW_MAPPINGS.row(ForgeRegistries.ENTITIES).forEach((oldName, newName) -> MOD_MAPPINGS.forEach(mapping -> {
                    fixMapping(ForgeRegistries.ENTITIES, mapping, oldName, newName);
                }));
            }
        }
    }

    @SubscribeEvent
    public static void fixBlockMappings(RegistryEvent.MissingMappings<Block> event)
    {
        if(event.getAllMappings() != null)
        {
            final List<Mapping<Block>> MOD_MAPPINGS = getMappings(event);

            if(!MOD_MAPPINGS.isEmpty())
            {
                Mineria.LOGGER.info("Fixing missing block mappings from modid mineria...");

                OLD_TO_NEW_MAPPINGS.row(ForgeRegistries.BLOCKS).forEach((oldName, newName) -> MOD_MAPPINGS.forEach(mapping -> {
                    fixMapping(ForgeRegistries.BLOCKS, mapping, oldName, newName);
                }));
            }
        }
    }

    @SubscribeEvent
    public static void fixItemMappings(RegistryEvent.MissingMappings<Item> event)
    {
        if(event.getAllMappings() != null)
        {
            final List<Mapping<Item>> MOD_MAPPINGS = getMappings(event);

            if(!MOD_MAPPINGS.isEmpty())
            {
                Mineria.LOGGER.info("Fixing missing block mappings from modid mineria...");

                OLD_TO_NEW_MAPPINGS.row(ForgeRegistries.ITEMS).forEach((oldName, newName) -> MOD_MAPPINGS.forEach(mapping -> {
                    fixMapping(ForgeRegistries.ITEMS, mapping, oldName, newName);
                }));
            }
        }
    }

    private static <V extends IForgeRegistryEntry<V>> void fixMapping(IForgeRegistry<V> registry, Mapping<V> mapping, String oldName, String newName)
    {
        if(mapping.key.getResourcePath().equals(oldName))
        {
            V newValue = registry.getValue(new ResourceLocation(References.MODID, newName));
            if(newValue == null)
                Mineria.LOGGER.fatal("Failed to fix " + mapping.key + ", could not find an instance of " + registry.getRegistrySuperType().getSimpleName() + " with key mineria:" + newName + " ! Please report this error to the mod author.");
            else
                mapping.remap(newValue);
        }
    }

    private static <T extends IForgeRegistryEntry<T>> List<Mapping<T>> getMappings(RegistryEvent.MissingMappings<T> event)
    {
        return event.getAllMappings().stream().filter(mapping -> mapping.key.getResourceDomain().equals(References.MODID)).collect(Collectors.toList());
    }
}
