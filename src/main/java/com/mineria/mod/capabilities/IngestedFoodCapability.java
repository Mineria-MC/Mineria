package com.mineria.mod.capabilities;

import com.mineria.mod.Mineria;
import com.mineria.mod.items.DrinkItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
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
            if(pair.getFirst() > DrinkItem.getMaxDigestionTime().getOrDefault(food, Mineria.FOOD_DIGESTION_TIME))
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
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT ingestedFoodMap = new CompoundNBT();

        this.ingestedFoodMap.forEach((food, pair) -> {
            CompoundNBT pairNbt = new CompoundNBT();
            pairNbt.putLong("Ticks", pair.getFirst());
            pairNbt.putInt("Count", pair.getSecond());
            ingestedFoodMap.put(food.getRegistryName().toString(), pairNbt);
        });

        nbt.put("IngestedFoodMap", ingestedFoodMap);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        CompoundNBT ingestedFoodMap = nbt.getCompound("IngestedFoodMap");

        ingestedFoodMap.keySet().forEach(key -> {
            Item food = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            CompoundNBT pair = ingestedFoodMap.getCompound(key);
            this.ingestedFoodMap.put(food, Pair.of(pair.getLong("Ticks"), pair.getInt("Count")));
        });
    }
}
