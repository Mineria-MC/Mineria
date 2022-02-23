package com.mineria.mod.common.capabilities;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IngestedFoodCapability implements IIngestedFood
{
    private final Map<Item, Pair<Long, Integer>> ingestedFoodMap = new HashMap<>();

    @Override
    public void foodIngested(Item food)
    {
        if(containsFood(food))
        {
            this.ingestedFoodMap.computeIfPresent(food, (food2, pair) -> Pair.of(0L, pair.getSecond() + 1));
        }
        else
        {
            this.ingestedFoodMap.put(food, Pair.of(0L, 1));
        }
    }

    private boolean containsFood(Item food)
    {
        return this.ingestedFoodMap.containsKey(food) && this.ingestedFoodMap.get(food) != null;
    }

    @Override
    public long getTicksSinceFoodIngested(Item food)
    {
        return containsFood(food) ? this.ingestedFoodMap.get(food).getFirst() : 0;
    }

    @Override
    public int getFoodIngestedCount(Item food)
    {
        return containsFood(food) ? this.ingestedFoodMap.get(food).getSecond() : 0;
    }

    @Override
    public void increaseTicksSinceFoodIngested()
    {
        this.ingestedFoodMap.replaceAll((food, pair) -> Pair.of(pair.getFirst() + 1, pair.getSecond()));
        Set<Item> entriesToRemove = new HashSet<>();
        this.ingestedFoodMap.forEach((food, pair) -> {
            if(pair.getFirst() > 12000)
                entriesToRemove.add(food);
        });
        entriesToRemove.forEach(this::removeIngestedFood);
    }

    @Override
    public void removeIngestedFood(Item food)
    {
        this.ingestedFoodMap.remove(food);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        CompoundTag ingestedFoodMap = new CompoundTag();

        this.ingestedFoodMap.forEach((food, pair) -> {
            CompoundTag pairNbt = new CompoundTag();
            pairNbt.putLong("Ticks", pair.getFirst());
            pairNbt.putInt("Count", pair.getSecond());
            ingestedFoodMap.put(food.getRegistryName().toString(), pairNbt);
        });

        nbt.put("IngestedFoodMap", ingestedFoodMap);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        CompoundTag ingestedFoodMap = nbt.getCompound("IngestedFoodMap");

        ingestedFoodMap.getAllKeys().forEach(key -> {
            Item food = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            CompoundTag pair = ingestedFoodMap.getCompound(key);
            this.ingestedFoodMap.put(food, Pair.of(pair.getLong("Ticks"), pair.getInt("Count")));
        });
    }
}
