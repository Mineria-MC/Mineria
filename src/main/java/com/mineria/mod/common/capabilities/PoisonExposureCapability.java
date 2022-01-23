package com.mineria.mod.common.capabilities;

import com.mineria.mod.common.effects.PoisonSource;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PoisonExposureCapability implements IPoisonExposure
{
    private final Map<PoisonSource, Pair<Long, Integer>> exposureMap = new HashMap<>();

    @Override
    public void poison(PoisonSource source)
    {
        if(isPoisoned(source))
        {
            this.exposureMap.computeIfPresent(source, (source1, pair) -> Pair.of(0L, pair.getSecond() + 1));
        }
        else
        {
            this.exposureMap.put(source, Pair.of(0L, 1));
        }
    }

    private boolean isPoisoned(PoisonSource source)
    {
        return this.exposureMap.containsKey(source) && this.exposureMap.get(source) != null;
    }

    @Override
    public int exposureCount(PoisonSource source)
    {
        return isPoisoned(source) ? this.exposureMap.get(source).getSecond() : 0;
    }

    @Override
    public long getTicksSinceLastExposure(PoisonSource source)
    {
        return isPoisoned(source) ? this.exposureMap.get(source).getFirst() : 0;
    }

    @Override
    public void increaseTicksSinceExposure()
    {
        this.exposureMap.replaceAll((source, pair) -> Pair.of(pair.getFirst() + 1, pair.getSecond()));
        Set<PoisonSource> entriesToRemove = new HashSet<>();
        this.exposureMap.forEach((source, pair) -> {
            if(pair.getFirst() > source.getMaxExposureTime())
                entriesToRemove.add(source);
        });
        entriesToRemove.forEach(this::removeFromMap);
    }

    @Override
    public void removeFromMap(PoisonSource source)
    {
        this.exposureMap.remove(source);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT exposureMap = new CompoundNBT();

        this.exposureMap.forEach((source, pair) -> {
            CompoundNBT pairNbt = new CompoundNBT();
            pairNbt.putLong("Ticks", pair.getFirst());
            pairNbt.putInt("Count", pair.getSecond());
            exposureMap.put(source.getId().toString(), pairNbt);
        });

        nbt.put("ExposureMap", exposureMap);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        CompoundNBT exposureMap = nbt.getCompound("ExposureMap");

        exposureMap.getAllKeys().forEach(key -> {
            PoisonSource source = PoisonSource.byName(ResourceLocation.tryParse(key));
            CompoundNBT pair = exposureMap.getCompound(key);
            this.exposureMap.put(source, Pair.of(pair.getLong("Ticks"), pair.getInt("Count")));
        });
    }
}
