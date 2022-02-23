package com.mineria.mod.common.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AttachedEntityCapability implements IAttachedEntity
{
    private final List<Entity> attachedEntities = new ArrayList<>();
    private final List<Integer> entityIds = new ArrayList<>();

    @Override
    public void attachEntity(Entity entity)
    {
        if(!entityIds.isEmpty())
            updateAttachedEntities(entity.getCommandSenderWorld());

        if(entity.isAlive()) attachedEntities.add(entity);
    }

    @Override
    public void updateAttachedEntities(Level world)
    {
        if(!entityIds.isEmpty())
        {
            attachedEntities.addAll(entityIds.stream().map(world::getEntity).filter(Objects::nonNull).collect(Collectors.toList()));
            entityIds.clear();
        }
        removeAttachedEntities(entity -> !entity.isAlive());
    }

    @Override
    public List<Entity> removeAttachedEntities(Predicate<Entity> predicate)
    {
        List<Entity> result = new ArrayList<>();

        for (Entity entity : attachedEntities)
        {
            if(predicate.test(entity))
            {
                result.add(entity);
            }
        }

        attachedEntities.removeAll(result);

        return result;
    }

    @Override
    public void removeAttachedEntity(int index)
    {
        attachedEntities.remove(index);
    }

    @Override
    public List<Entity> getAttachedEntities()
    {
        return attachedEntities;
    }

    @Override
    public ListTag serializeNBT()
    {
        ListTag list = new ListTag();
        attachedEntities.forEach(entity -> list.add(IntTag.valueOf(entity.getId())));
        return list;
    }

    @Override
    public void deserializeNBT(ListTag nbt)
    {
        entityIds.addAll(nbt.stream().filter(IntTag.class::isInstance).map(IntTag.class::cast).map(IntTag::getAsInt).collect(Collectors.toList()));
    }
}
