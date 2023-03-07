package io.github.mineria_mc.mineria.common.capabilities.marking;

import io.github.mineria_mc.mineria.common.capabilities.TickInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class LivingAttachedDataCapabilityImpl implements ILivingAttachedDataCapability {
    private final AttachedDataEntries entries;
    private long tickCounter;

    private LivingAttachedDataCapabilityImpl() {
        this.entries = new AttachedDataEntries();
    }

    @Override
    public void tick() {


        tickCounter++;
    }

    @Override
    public <T> void mark(AttachedDataType<T> type, T value) {
        entries.get(type).compute(value, (v, existing) -> existing == null ? new TickInfo(tickCounter) : existing.atTime(tickCounter).incrementCount());
    }

    @Override
    public <T> boolean isMarked(AttachedDataType<T> type, T value) {
        return entries.tickInfo(type, value) != TickInfo.EMPTY;
    }

    @Override
    public <T> int markCount(AttachedDataType<T> type, T value) {
        return entries.tickInfo(type, value).getCount();
    }

    @Override
    public <T> long ticksSinceLastMark(AttachedDataType<T> type, T value) {
        return entries.tickInfo(type, value).getAppliedTick();
    }

    @Override
    public <T> void unmark(AttachedDataType<T> type, T value) {
        entries.get(type).remove(value);
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    private record MarkTypeEntry<T>(AttachedDataType<T> type, Set<MarkDataEntry<T>> entries) {
    }

    private record MarkDataEntry<T>(T value, TickInfo tickInfo) {
    }

    private static final class AttachedDataEntries implements INBTSerializable<CompoundTag> {
        private final Map<AttachedDataType<?>, Map<?, TickInfo>> delegate;

        public AttachedDataEntries() {
            this.delegate = new HashMap<>();
        }

        <T> TickInfo tickInfo(AttachedDataType<T> type, T value) {
            return get(type).getOrDefault(value, TickInfo.EMPTY);
        }

        @SuppressWarnings("unchecked")
        <T> Map<T, TickInfo> get(AttachedDataType<T> type) {
            return (Map<T, TickInfo>) delegate.get(type);
        }

        @Override
        public CompoundTag serializeNBT() {
            return null;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {

        }
    }
}
