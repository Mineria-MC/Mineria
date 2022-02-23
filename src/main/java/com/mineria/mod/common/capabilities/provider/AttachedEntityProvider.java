package com.mineria.mod.common.capabilities.provider;

import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.capabilities.IAttachedEntity;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AttachedEntityProvider implements ICapabilitySerializable<ListTag>
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
    public ListTag serializeNBT()
    {
        return cap.serializeNBT();
    }

    @Override
    public void deserializeNBT(ListTag nbt)
    {
        cap.deserializeNBT(nbt);
    }
}
