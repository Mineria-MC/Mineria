package com.mineria.mod.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Optional;
import java.util.stream.Stream;

public class DeferredRegisterUtil
{
    public static <E extends IForgeRegistryEntry<E>> Stream<E> filterEntriesFromRegister(DeferredRegister<E> deferred, Class<? extends E> filter)
    {
        return deferred.getEntries().stream().map(RegistryObject::get).filter(filter::isInstance);
    }

    public static <E extends IForgeRegistryEntry<E>> Optional<E> findEntryByName(DeferredRegister<E> register, ResourceLocation name)
    {
        return register.getEntries().stream().filter(obj -> name.equals(obj.getId())).map(RegistryObject::get).findFirst();
    }
}
