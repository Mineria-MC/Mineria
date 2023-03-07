package io.github.mineria_mc.mineria.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleCapabilityProvider<C> implements ICapabilityProvider {
    private final LazyOptional<C> capCache;
    private final NonNullSupplier<Capability<C>> capGetter;

    private SimpleCapabilityProvider(@Nonnull C cap, NonNullSupplier<Capability<C>> capGetter) {
        this.capCache = LazyOptional.of(() -> cap);
        this.capGetter = capGetter;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == capGetter.get() ? capCache.cast() : LazyOptional.empty();
    }

    public static <C> ICapabilityProvider simple(C cap, NonNullSupplier<Capability<C>> capGetter) {
        return new SimpleCapabilityProvider<>(cap, capGetter);
    }

    public static <NBT extends Tag, C extends INBTSerializable<NBT>> ICapabilitySerializable<NBT> serializable(C cap, NonNullSupplier<Capability<C>> capGetter) {
        return new Serializable<>(cap, capGetter);
    }

    private static class Serializable<NBT extends Tag, C extends INBTSerializable<NBT>> extends SimpleCapabilityProvider<C> implements ICapabilitySerializable<NBT> {
        private final C serializableCap;

        private Serializable(@Nonnull C cap, NonNullSupplier<Capability<C>> capGetter) {
            super(cap, capGetter);
            this.serializableCap = cap;
        }

        @Override
        public NBT serializeNBT() {
            return this.serializableCap.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBT nbt) {
            this.serializableCap.deserializeNBT(nbt);
        }
    }
}
