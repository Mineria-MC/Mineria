package io.github.mineria_mc.mineria.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;
import java.util.stream.Stream;

public class DeferredRegisterUtil {
    public static <E> Stream<E> presentEntries(DeferredRegister<E> register) {
        return register.getEntries().stream().filter(RegistryObject::isPresent).map(RegistryObject::get);
    }

    public static <E> Stream<E> filter(DeferredRegister<E> register, Class<? extends E> filter) {
        return presentEntries(register).filter(filter::isInstance);
    }

    public static <E> boolean contains(DeferredRegister<E> register, E entry) {
        return presentEntries(register).anyMatch(entry::equals);
    }

    public static <E> Optional<E> byName(DeferredRegister<E> register, ResourceLocation name) {
        return register.getEntries().stream().filter(obj -> name.equals(obj.getId())).map(RegistryObject::get).findFirst();
    }
}
