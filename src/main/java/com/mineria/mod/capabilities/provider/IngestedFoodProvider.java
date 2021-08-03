package com.mineria.mod.capabilities.provider;

import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.capabilities.IIngestedFood;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IngestedFoodProvider implements ICapabilitySerializable<CompoundNBT>
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
        return cap == CapabilityRegistry.INGESTED_FOOD_CAP ? LazyOptional.of(() -> (T) this.cap) : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        return cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        cap.deserializeNBT(nbt);
    }
}
