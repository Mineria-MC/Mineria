package com.mineria.mod.common.capabilities.provider;

import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IIngestedFood;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IngestedFoodProvider implements ICapabilitySerializable<CompoundTag>
{
    private final IIngestedFood cap;

    public IngestedFoodProvider(IIngestedFood cap)
    {
        this.cap = cap;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return /*cap == CapabilityRegistry.INGESTED_FOOD_CAP ? LazyOptional.of(() -> (T) this.cap) :*/ LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        cap.deserializeNBT(nbt);
    }
}
