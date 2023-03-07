package io.github.mineria_mc.mineria.common.init.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.HashMap;
import java.util.Map;

public interface MineriaBootstrapContext<T> {
    BootstapContext<T> initialContext();

    Holder.Reference<T> register(ResourceKey<T> key, T value);

    <O> HolderGetter<O> lookup(ResourceKey<? extends Registry<O>> reg);

    <O> Holder<O> get(ResourceKey<? extends Registry<O>> reg, ResourceKey<O> key);

    <O> HolderSet<O> values(ResourceKey<? extends Registry<O>> reg, TagKey<O> key);

    static <T> MineriaBootstrapContext<T> wrap(BootstapContext<T> initialCtx) {
        return new MineriaBootstrapContext<>() {
            private final Map<ResourceKey<? extends Registry<?>>, HolderGetter<?>> gettersCache = new HashMap<>();

            @Override
            public BootstapContext<T> initialContext() {
                return initialCtx;
            }

            @Override
            public Holder.Reference<T> register(ResourceKey<T> key, T value) {
                return initialCtx.register(key, value);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <O> HolderGetter<O> lookup(ResourceKey<? extends Registry<O>> reg) {
                return (HolderGetter<O>) gettersCache.computeIfAbsent(reg, resourceKey -> initialCtx.lookup(reg));
            }

            @Override
            public <O> Holder<O> get(ResourceKey<? extends Registry<O>> reg, ResourceKey<O> key) {
                return lookup(reg).getOrThrow(key);
            }

            @Override
            public <O> HolderSet<O> values(ResourceKey<? extends Registry<O>> reg, TagKey<O> key) {
                return lookup(reg).getOrThrow(key);
            }
        };
    }
}
