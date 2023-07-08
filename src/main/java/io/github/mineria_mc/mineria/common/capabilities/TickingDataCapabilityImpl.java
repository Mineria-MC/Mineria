package io.github.mineria_mc.mineria.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class TickingDataCapabilityImpl implements ITickingDataCapability {
    private final Map<DataType<?>, Map<? extends Candidate, TickInfo>> entries;
    private long tickCounter;

    TickingDataCapabilityImpl() {
        this.entries = new HashMap<>();
    }

    @Override
    public void tick() {
        for (Map.Entry<DataType<?>, Map<? extends Candidate, TickInfo>> dataMapEntry : entries.entrySet()) {
            Map<? extends Candidate, TickInfo> data = dataMapEntry.getValue();
            Set<Candidate> toRemove = new HashSet<>();

            data.forEach((element, info) -> {
                if(tickCounter - info.getAppliedTick() >= element.getTickLimit()) {
                    toRemove.add(element);
                }
            });

            toRemove.forEach(data::remove);
        }
        tickCounter++;
    }

    @Override
    public <T extends Candidate> void store(DataType<T> type, @Nonnull T value) {
        if(type.filter().test(value)) {
            get(type).compute(value, (v, existing) -> existing == null ? new TickInfo(tickCounter) : existing.atTime(tickCounter).incrementCount());
        }
    }

    @Override
    public <T extends Candidate> boolean contains(DataType<T> type, @Nonnull T value) {
        return get(type).containsKey(value);
    }

    @Override
    public <T extends Candidate> int occurrences(DataType<T> type, @Nonnull T value) {
        return tickInfo(type, value).getCount();
    }

    @Override
    public <T extends Candidate> long ticksSinceStore(DataType<T> type, @Nonnull T value) {
        return contains(type, value) ? tickCounter - tickInfo(type, value).getAppliedTick() : 0;
    }

    @Override
    public <T extends Candidate> void remove(DataType<T> type, @Nonnull T value) {
        get(type).remove(value);
    }

    @Override
    public <T extends Candidate> void updateLegacyCapability(DataType<T> type, CompoundTag exposureMap) {
        Map<T, TickInfo> data = new HashMap<>();
        for (String elementId : exposureMap.getAllKeys()) {
            T element = type.candidateById().apply(elementId);
            if(element == null) {
                continue;
            }

            CompoundTag tickInfoNbt = exposureMap.getCompound(elementId);
            TickInfo tickInfo = type == TickingDataTypes.POISON_EXPOSURE ? new TickInfo(tickInfoNbt.getLong("Ticks"), tickInfoNbt.getInt("Count")) : TickInfo.fromNbt(tickInfoNbt);
            data.put(element, tickInfo.atTime(-element.getTickLimit() + tickInfo.getAppliedTick()));
        }
        if(!data.isEmpty()) {
            get(type).putAll(data);
        }
    }

    private <T extends Candidate> TickInfo tickInfo(DataType<T> type, T value) {
        return get(type).getOrDefault(value, TickInfo.EMPTY);
    }

    @SuppressWarnings("unchecked")
    private <T extends Candidate> Map<T, TickInfo> get(DataType<T> type) {
        return (Map<T, TickInfo>) entries.computeIfAbsent(type, t -> new HashMap<>());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        entries.forEach((dataType, data) -> {
            CompoundTag dataNbt = new CompoundTag();
            data.forEach((element, tickInfo) -> dataNbt.put(element.getSerializationString(), tickInfo.atTime(tickInfo.getAppliedTick() - tickCounter).toNbt()));
            nbt.put(dataType.id().toString(), dataNbt);
        });
        // reset to zero after subtraction has been done above
        this.tickCounter = 0;
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.entries.clear();

        for (String dataType : nbt.getAllKeys()) {
            DataType<?> type = DataType.byId(ResourceLocation.tryParse(dataType));
            if(type == null) {
                continue;
            }

            CompoundTag dataNbt = nbt.getCompound(dataType);
            Map<Candidate, TickInfo> data = new HashMap<>();
            for (String elementId : dataNbt.getAllKeys()) {
                Candidate element = type.candidateById().apply(elementId);
                if(element == null) {
                    continue;
                }

                data.put(element, TickInfo.fromNbt(dataNbt.getCompound(elementId)));
            }

            this.entries.put(type, data);
        }
    }
}
