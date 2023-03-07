package io.github.mineria_mc.mineria.util;

import com.google.common.collect.ImmutableMap;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.MissingMappingsEvent.Mapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Automatically remaps every old mapping from Mineria.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissingMappingsHandler {
    private static final Logger LOGGER = LogManager.getLogger("Mineria Missing Mappings");

    private static final ImmutableMap<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, ResourceLocation>> MAPPINGS_MAP = ImmutableMap.<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, ResourceLocation>>builder()
            .put(Registries.ITEM,
                    ImmutableMap.<ResourceLocation, ResourceLocation>builder()
                            .put(modLoc("mineria_xp_orb"), modLoc("xp_orb"))
                            .put(modLoc("copper_ingot"), new ResourceLocation("copper_ingot"))
                            .put(modLoc("copper_ore"), new ResourceLocation("copper_ore"))
                            .put(modLoc("copper_block"), new ResourceLocation("copper_block"))
                            .build())
            .put(Registries.BLOCK,
                    ImmutableMap.<ResourceLocation, ResourceLocation>builder()
                            .put(modLoc("copper_ore"), new ResourceLocation("copper_ore"))
                            .put(modLoc("copper_block"), new ResourceLocation("copper_block"))
                            .build())
            .build();

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void fixMissingMappings(MissingMappingsEvent event) {
        // get all missing mappings from mineria
        List<? extends Mapping<?>> modMappings = event.getAllMappings((ResourceKey<? extends Registry<Object>>) event.getKey()).stream().filter(mapping -> mapping.getKey().getNamespace().equals(Mineria.MODID)).toList();

        if (modMappings.isEmpty()) {
            return;
        }

        // get the key map related to the current registry
        Map<ResourceLocation, ResourceLocation> keyMap = MAPPINGS_MAP.get(event.getKey());

        if (keyMap == null) {
            return;
        }

        for (Mapping<?> mapping : modMappings) {
            if (keyMap.containsKey(mapping.getKey())) {
                ResourceLocation newKey = keyMap.get(mapping.getKey());
                Object entry = event.getRegistry().getValue(newKey);

                if (entry == null) {
                    LOGGER.error("Failed to fix '{}', could not find entry with key '{}' in Forge Registry '{}'! Please report this error to the mod author.", mapping.getKey(), newKey, event.getKey().registry());
                    continue;
                }

                ((Mapping<Object>) mapping).remap(entry);
            }
        }
    }

    private static ResourceLocation modLoc(String name) {
        return new ResourceLocation(Mineria.MODID, name);
    }
}
