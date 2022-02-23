package com.mineria.mod.common.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Predicate;

public interface IAttachedEntity extends INBTSerializable<ListTag>
{
    void attachEntity(Entity entity);

    void updateAttachedEntities(Level world);

    List<Entity> removeAttachedEntities(Predicate<Entity> entity);

    void removeAttachedEntity(int index);

    List<Entity> getAttachedEntities();
}
