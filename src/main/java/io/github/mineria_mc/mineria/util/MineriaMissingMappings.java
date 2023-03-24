package io.github.mineria_mc.mineria.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.MissingMappingsEvent.Mapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Automatically remaps every old mapping from Mineria.
 */
public class MineriaMissingMappings {
    private static final Logger LOGGER = LogManager.getLogger("Mineria Missing Mappings");

    private static final Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, ResourceLocation>> MAPPINGS_MAP = Map.copyOf(readMappingFixesFile());

    public static void initialize() {
        if(!MAPPINGS_MAP.isEmpty()) {
            MinecraftForge.EVENT_BUS.addListener(MineriaMissingMappings::fixMissingMappings);
        }
    }

    @SuppressWarnings("unchecked")
    private static void fixMissingMappings(MissingMappingsEvent event) {
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

    private static Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, ResourceLocation>> readMappingFixesFile() {
        Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, ResourceLocation>> result = new HashMap<>();
        ModList modList = ModList.get();
        if(modList == null) {
            return result;
        }
        IModFileInfo fileInfo = modList.getModFileById(Mineria.MODID);
        if(fileInfo == null) {
            return result;
        }
        Path mappings = fileInfo.getFile().findResource("mineria_mapping_fixes.json");
        if(Files.notExists(mappings)) {
            return result;
        }
        JsonElement json;
        try {
            json = JsonParser.parseReader(Files.newBufferedReader(mappings));
        } catch (IOException e) {
            LOGGER.error("Caught an exception when trying to read mappings!", e);
            return result;
        }
        if(!json.isJsonObject()) {
            return result;
        }
        for (Map.Entry<String, JsonElement> registryEntry : json.getAsJsonObject().entrySet()) {
            JsonElement mappingsJson = registryEntry.getValue();
            if(!mappingsJson.isJsonObject()) {
                continue;
            }
            Map<ResourceLocation, ResourceLocation> mappingsMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> mappingEntry : mappingsJson.getAsJsonObject().entrySet()) {
                JsonElement mappingValue = mappingEntry.getValue();
                if(!mappingValue.isJsonPrimitive()) {
                    continue;
                }
                mappingsMap.put(new ResourceLocation(mappingEntry.getKey()), new ResourceLocation(mappingValue.getAsString()));
            }
            result.put(ResourceKey.createRegistryKey(new ResourceLocation(registryEntry.getKey())), mappingsMap);
        }

        return result;
    }
}
