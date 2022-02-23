package com.mineria.mod.common.capabilities;

import com.mineria.mod.common.effects.PoisonSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPoisonExposure extends INBTSerializable<CompoundTag>
{
    void poison(PoisonSource source);

    int exposureCount(PoisonSource source);

    long getTicksSinceLastExposure(PoisonSource source);

    void increaseTicksSinceExposure();

    void removeFromMap(PoisonSource source);

    /*class Cause<T>
    {
        enum CauseType { DRINK, POTION, PROJECTILE }

        private final CauseType type;
        private final T obj;

        private Cause(CauseType type, T obj)
        {
            this.type = type;
            this.obj = obj;
        }

        private CompoundNBT serialize()
        {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("CauseType", type.name());
            return nbt;
        }

        public static Cause<Item> ofItem(Item drink)
        {
            return new Cause<>(CauseType.DRINK, drink);
        }

        public static Cause<MineriaPotionEntity> ofPotion(MineriaPotionEntity potion)
        {
            return new Cause<>(CauseType.POTION, potion);
        }

        public static Cause<BlowgunRefillEntity> ofProjectile(BlowgunRefillEntity projectile)
        {
            return new Cause<>(CauseType.PROJECTILE, projectile);
        }
    }*/
}
