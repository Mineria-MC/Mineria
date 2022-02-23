package com.mineria.mod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * An implementation to override vanilla objets.
 */
public interface VanillaOverride
{
    /**
     * Sets the registry name of the objet.
     * @param name the name of the existing minecraft object.
     */
    default void setVanillaName(String name)
    {
        if(this instanceof ForgeRegistryEntry)
        {
            ObfuscationReflectionHelper.setPrivateValue(ForgeRegistryEntry.class, (ForgeRegistryEntry<?>) this, new ResourceLocation("minecraft", name), "registryName");
            return;
        }
        throw new AssertionError("A vanilla override should be an instance of a ForgeRegistryEntry!");
    }
}
