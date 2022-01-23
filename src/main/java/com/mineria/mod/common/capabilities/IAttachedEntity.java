package com.mineria.mod.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Predicate;

public interface IAttachedEntity extends INBTSerializable<ListNBT>
{
    void attachEntity(Entity entity);

    void updateAttachedEntities(World world);

    List<Entity> removeAttachedEntities(Predicate<Entity> entity);

    void removeAttachedEntity(int index);

    List<Entity> getAttachedEntities();
}
