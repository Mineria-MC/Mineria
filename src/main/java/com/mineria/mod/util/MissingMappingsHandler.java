package com.mineria.mod.util;

import com.google.common.collect.ImmutableMap;
import com.mineria.mod.Mineria;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Action;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Automatically remaps every old mapping from Mineria. 
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappingsHandler
{
    private static final Logger LOGGER = LogManager.getLogger("Mineria Missing Mappings");

    private static final ImmutableMap<IForgeRegistry<?>, Map<ResourceLocation, ResourceLocation>> MAPPINGS_MAP = ImmutableMap.<IForgeRegistry<?>, Map<ResourceLocation, ResourceLocation>>builder()
            .put(ForgeRegistries.ITEMS,
                    ImmutableMap.<ResourceLocation, ResourceLocation>builder()
                            .put(modLoc("mineria_xp_orb"), modLoc("xp_orb"))
                            .put(modLoc("mrlulu_sword"), new ResourceLocation("diamond_sword"))
                            .put(modLoc("mathys_craft_sword"), new ResourceLocation("diamond_sword"))
                            .put(modLoc("test_only"), new ResourceLocation("diamond"))
                            .build())
            .build();

    @SubscribeEvent
    public static void fixMissingMappings(MissingMappings<?> event)
    {
        // get all missing mappings from mineria
        List<? extends Mapping<?>> modMappings = event.getAllMappings().stream().filter(mapping -> mapping.key.getNamespace().equals("mineria")).toList();

        if(modMappings.isEmpty())
            return;

        // get the key map related to the current registry
        Map<ResourceLocation, ResourceLocation> keyMap = MAPPINGS_MAP.get(event.getRegistry());

        for(Mapping<?> mapping : modMappings)
        {
            if(keyMap.containsKey(mapping.key))
            {
                ResourceLocation newKey = keyMap.get(mapping.key);
                IForgeRegistryEntry<?> entry = event.getRegistry().getValue(newKey);

                if(entry == null)
                {
                    LOGGER.error("Failed to fix '{}', could not find entry with key '{}' in Forge Registry '{}'! Please report this error to the mod author.", mapping.key, newKey, mapping.registry.getRegistryName());
                    continue;
                }

                try
                {
                    remap(mapping, entry);
                } catch (NoSuchFieldException e)
                {
                    LOGGER.error("Failed to locate a field in RegistryEvent.MissingMapping.Mapping during reflection! This version of the mod may not be compatible with your Forge Version!", e);
                } catch (IllegalAccessException e)
                {
                    LOGGER.error("Failed to access a field in RegistryEvent.MissingMapping.Mapping during reflection!", e);
                }
            }
        }
    }

    private static Field TARGET;
    private static Field ACTION;

    private static void remap(Mapping<?> mapping, IForgeRegistryEntry<?> entry) throws NoSuchFieldException, IllegalAccessException
    {
        if(TARGET == null)
        {
            TARGET = Mapping.class.getDeclaredField("target");
            TARGET.setAccessible(true);
        }
        if(ACTION == null)
        {
            ACTION = Mapping.class.getDeclaredField("action");
            ACTION.setAccessible(true);
        }

        TARGET.set(mapping, entry); // We set the value using reflection otherwise we have issues with generic types
        ACTION.set(mapping, Action.REMAP);
    }

    private static ResourceLocation modLoc(String name)
    {
        return new ResourceLocation(Mineria.MODID, name);
    }
}
