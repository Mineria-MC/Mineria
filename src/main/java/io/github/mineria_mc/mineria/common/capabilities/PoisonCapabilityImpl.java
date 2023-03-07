package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class PoisonCapabilityImpl implements IPoisonCapability {
    private final Map<PoisonSource, Pair<Long, Integer>> exposureMap = new HashMap<>();

    @Override
    public void applyPoison(PoisonSource source) {
        if (isPoisoned(source)) {
            this.exposureMap.computeIfPresent(source, (source1, pair) -> Pair.of(0L, pair.getSecond() + 1));
        } else {
            this.exposureMap.put(source, Pair.of(0L, 1));
        }
    }

    private boolean isPoisoned(PoisonSource source) {
        return this.exposureMap.containsKey(source) && this.exposureMap.get(source) != null;
    }

    @Override
    public int exposureCount(PoisonSource source) {
        return isPoisoned(source) ? this.exposureMap.get(source).getSecond() : 0;
    }

    @Override
    public long getTickCount(PoisonSource source) {
        return isPoisoned(source) ? this.exposureMap.get(source).getFirst() : 0;
    }

    @Override
    public void tick() {
        this.exposureMap.replaceAll((source, pair) -> Pair.of(pair.getFirst() + 1, pair.getSecond()));
        Set<PoisonSource> entriesToRemove = new HashSet<>();
        this.exposureMap.forEach((source, pair) -> {
            if (pair.getFirst() > source.getMaxExposureTime())
                entriesToRemove.add(source);
        });
        entriesToRemove.forEach(this::removePoison);
    }

    @Override
    public void removePoison(PoisonSource source) {
        this.exposureMap.remove(source);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        CompoundTag exposureMap = new CompoundTag();

        this.exposureMap.forEach((source, pair) -> {
            CompoundTag pairNbt = new CompoundTag();
            pairNbt.putLong("Ticks", pair.getFirst());
            pairNbt.putInt("Count", pair.getSecond());
            exposureMap.put(source.getId().toString(), pairNbt);
        });

        nbt.put("ExposureMap", exposureMap);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag exposureMap = nbt.getCompound("ExposureMap");

        exposureMap.getAllKeys().forEach(key -> {
            PoisonSource source = PoisonSource.byName(ResourceLocation.tryParse(key));
            CompoundTag pair = exposureMap.getCompound(key);
            this.exposureMap.put(source, Pair.of(pair.getLong("Ticks"), pair.getInt("Count")));
        });
    }
}
