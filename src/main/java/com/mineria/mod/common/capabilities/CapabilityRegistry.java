package com.mineria.mod.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

// TODO
public class CapabilityRegistry
{
//    public static Capability<IIngestedFood> INGESTED_FOOD_CAP = null;
    public static Capability<IAttachedEntity> ATTACHED_ENTITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<IPoisonExposure> POISON_EXPOSURE = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities()
    {
//        CapabilityManager.INSTANCE.register(IIngestedFood.class, makeStorage(), IngestedFoodCapability::new);
//        CapabilityManager.INSTANCE.register(IAttachedEntity.class, makeStorage(), AttachedEntityCapability::new);
//        CapabilityManager.INSTANCE.register(IPoisonExposure.class, makeStorage(), PoisonExposureCapability::new);
    }

    /*private static <NBT extends Tag, T extends INBTSerializable<NBT>> Capability.IStorage<T> makeStorage()
    {
        return new Capability.IStorage<T>()
        {
            @Nullable
            @Override
            public Tag writeNBT(Capability<T> capability, T instance, Direction side)
            {
                return instance.serializeNBT();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt)
            {
                if(!(capability.getClass().isInstance(instance))) throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation.");
                instance.deserializeNBT((NBT) nbt);
            }
        };
    }*/
}
