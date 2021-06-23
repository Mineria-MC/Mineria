package com.mineria.mod.util;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.stream.Stream;

public class DeferredRegisterUtil
{
    public static <E extends IForgeRegistryEntry<E>> Stream<E> filterEntriesFromRegister(DeferredRegister<E> deferred, Class<? extends E> filter)
    {
        return deferred.getEntries().stream().map(RegistryObject::get).filter(filter::isInstance);
    }
}
