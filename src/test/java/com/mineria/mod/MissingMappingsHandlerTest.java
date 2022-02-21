package com.mineria.mod;

import com.google.common.collect.ImmutableSet;
import com.mineria.mod.util.MissingMappingsHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.registries.ForgeRegistries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissingMappingsHandlerTest extends McTest
{
    @Test
    void testMissingMappingHandler()
    {
        MissingMappings.Mapping<Item> mapping = new MissingMappings.Mapping<>(ForgeRegistries.ITEMS, ForgeRegistries.ITEMS, new ResourceLocation(Mineria.MODID, "test_only"), 0);
        MissingMappings<Item> event = new MissingMappings<>(new ResourceLocation("items"), ForgeRegistries.ITEMS, ImmutableSet.of(mapping));
        MissingMappingsHandler.fixMissingMappings(event);
        assertEquals(Items.DIAMOND, mapping.getTarget());
    }
}
