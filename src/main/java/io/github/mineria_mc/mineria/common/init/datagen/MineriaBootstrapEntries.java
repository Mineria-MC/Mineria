package io.github.mineria_mc.mineria.common.init.datagen;

import com.google.common.collect.ImmutableMap;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MineriaBootstrapEntries<T, E extends MineriaBootstrapEntries.Entry<T>> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final Map<ResourceKey<T>, E> entryMap = new HashMap<>();
    private final List<BootstrapConsumer<T>> entries = new ArrayList<>();

    public MineriaBootstrapEntries(ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    public ResourceKey<T> register(String name, E entry) {
        return registerEntry(name, key -> entry);
    }

    public ResourceKey<T> registerEntry(String name, Function<ResourceKey<T>, E> entryBuilder) {
        ResourceKey<T> key = makeKey(name);
        E entry = entryBuilder.apply(key);
        this.entryMap.put(key, entry);
        this.entries.add(ctx -> ctx.register(key, entry.create(ctx)));
        return key;
    }

    public void registerAll(BootstapContext<T> ctx) {
        MineriaBootstrapContext<T> mineriaCtx = MineriaBootstrapContext.wrap(ctx);
        this.entries.forEach(entry -> entry.register(mineriaCtx));
    }

    public boolean contains(String name) {
        return contains(makeKey(name));
    }

    public boolean contains(ResourceKey<T> key) {
        return entryMap.containsKey(key);
    }

    public Map<ResourceKey<T>, E> getEntryMap() {
        return ImmutableMap.copyOf(entryMap);
    }

    public ResourceKey<T> makeKey(String name) {
        return ResourceKey.create(this.registryKey, new ResourceLocation(Mineria.MODID, name));
    }

    public TagKey<T> createTag(String name) {
        return TagKey.create(this.registryKey, new ResourceLocation(Mineria.MODID, name));
    }

    @FunctionalInterface
    public interface BootstrapConsumer<T> {
        Holder<T> register(MineriaBootstrapContext<T> ctx);
    }

    @FunctionalInterface
    public interface Entry<T> {
        T create(MineriaBootstrapContext<T> ctx);
    }

    public static class Simple<T> extends MineriaBootstrapEntries<T, Entry<T>> {
        public Simple(ResourceKey<? extends Registry<T>> registryKey) {
            super(registryKey);
        }
    }
}
