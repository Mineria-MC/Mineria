package com.mineria.mod.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class CapabilityRegistry
{
    @CapabilityInject(IIngestedFood.class)
    public static Capability<IIngestedFood> INGESTED_FOOD_CAP = null;

    public static void registerCapabilities()
    {
        CapabilityManager.INSTANCE.register(IIngestedFood.class, makeStorage(), IngestedFoodCapability::new);
    }

    private static <NBT extends INBT, T extends INBTSerializable<NBT>> Capability.IStorage<T> makeStorage()
    {
        return new Capability.IStorage<T>()
        {
            @Nullable
            @Override
            public INBT writeNBT(Capability<T> capability, T instance, Direction side)
            {
                return instance.serializeNBT();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt)
            {
                if(!(capability.getClass().isInstance(instance))) throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation.");
                instance.deserializeNBT((NBT) nbt);
            }
        };
    }
}
