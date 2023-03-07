package io.github.mineria_mc.mineria.common.capabilities;

import net.minecraft.nbt.CompoundTag;

public class TickInfo {
    public static final TickInfo EMPTY = immutable(0L, 0);

    private long tickIndicator;
    private int count;

    public TickInfo(long tickIndicator) {
        this(tickIndicator, 1);
    }

    public TickInfo(long tickIndicator, int count) {
        this.tickIndicator = tickIndicator;
        this.count = count;
    }

    public TickInfo atTime(long tickIndicator) {
        this.tickIndicator = tickIndicator;
        return this;
    }

    public TickInfo incrementCount() {
        count++;
        return this;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("TickIndicator", tickIndicator);
        tag.putInt("Count", count);
        return tag;
    }

    public static TickInfo fromNbt(CompoundTag tag) {
        long tickCount = tag.getLong("TickIndicator");
        int count = tag.getInt("Count");
        return new TickInfo(tickCount, count);
    }

    public long getAppliedTick() {
        return tickIndicator;
    }

    public int getCount() {
        return count;
    }

    public static TickInfo immutable(long tickCount, int count) {
        return new TickInfo(tickCount, count) {
            @Override
            public TickInfo atTime(long tickIndicator) {
                return this;
            }

            @Override
            public TickInfo incrementCount() {
                return this;
            }
        };
    }
}
