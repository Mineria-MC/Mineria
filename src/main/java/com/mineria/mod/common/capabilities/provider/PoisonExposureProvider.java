package com.mineria.mod.common.capabilities.provider;

import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IPoisonExposure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PoisonExposureProvider implements ICapabilitySerializable<CompoundTag>
{
    private final IPoisonExposure cap;

    public PoisonExposureProvider(IPoisonExposure cap)
    {
        this.cap = cap;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return cap == CapabilityRegistry.POISON_EXPOSURE ? LazyOptional.of(() -> (T) this.cap) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return this.cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        this.cap.deserializeNBT(nbt);
    }
}