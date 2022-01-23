package com.mineria.mod.common.capabilities.provider;

import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IAttachedEntity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AttachedEntityProvider implements ICapabilitySerializable<ListNBT>
{
    private final IAttachedEntity cap;

    public AttachedEntityProvider(IAttachedEntity cap)
    {
        this.cap = cap;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return cap == CapabilityRegistry.ATTACHED_ENTITY ? LazyOptional.of(() -> (T) this.cap) : LazyOptional.empty();
    }

    @Override
    public ListNBT serializeNBT()
    {
        return cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(ListNBT nbt)
    {
        cap.deserializeNBT(nbt);
    }
}
