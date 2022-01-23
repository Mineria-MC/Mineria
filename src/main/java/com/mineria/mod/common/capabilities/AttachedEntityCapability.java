package com.mineria.mod.common.capabilities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;

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
    public void updateAttachedEntities(World world)
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
    public ListNBT serializeNBT()
    {
        ListNBT list = new ListNBT();
        attachedEntities.forEach(entity -> list.add(IntNBT.valueOf(entity.getId())));
        return list;
    }

    @Override
    public void deserializeNBT(ListNBT nbt)
    {
        entityIds.addAll(nbt.stream().filter(IntNBT.class::isInstance).map(IntNBT.class::cast).map(IntNBT::getAsInt).collect(Collectors.toList()));
    }
}
