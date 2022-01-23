package com.mineria.mod.common.capabilities;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IIngestedFood extends INBTSerializable<CompoundNBT>
{
    void foodIngested(Item food);

    long getTicksSinceFoodIngested(Item food);

    int getFoodIngestedCount(Item food);

    void increaseTicksSinceFoodIngested();

    void removeIngestedFood(Item food);
}
