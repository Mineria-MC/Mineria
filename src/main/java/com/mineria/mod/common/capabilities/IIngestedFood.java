package com.mineria.mod.common.capabilities;

import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IIngestedFood extends INBTSerializable<CompoundTag>
{
    void foodIngested(Item food);

    long getTicksSinceFoodIngested(Item food);

    int getFoodIngestedCount(Item food);

    void increaseTicksSinceFoodIngested();

    void removeIngestedFood(Item food);
}
