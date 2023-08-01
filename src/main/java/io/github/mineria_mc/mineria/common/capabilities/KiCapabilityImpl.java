package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.common.capabilities.ki.IKiCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

class KiCapabilityImpl implements IKiCapability {
    private int ki, max, level;

    @Override
    public int getKi() {
        return ki;
    }

    @Override
    public int getMaxKi() {
        return max;
    }

    @Override
    public int getKiLevel() {
        return level;
    }

    @Override
    public void setKi(int ki) {
        this.ki = Math.min(Math.abs(ki), max);
    }

    @Override
    public void setMaxKi(int max) {
    }

    @Override
    public void setKiLevel(int level) {
        this.level = Mth.clamp(level, 1, MAX_LEVEL);
        this.max = this.level * 5 + 5;
        this.ki = Math.min(this.ki, this.max);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Ki", ki);
        nbt.putInt("Max", max);
        nbt.putInt("Level", level);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.ki = nbt.getInt("Ki");
        this.max = nbt.getInt("Max");
        this.level = nbt.getInt("Level");
    }
}
