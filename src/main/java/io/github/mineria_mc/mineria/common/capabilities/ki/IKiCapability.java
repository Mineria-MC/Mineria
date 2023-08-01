package io.github.mineria_mc.mineria.common.capabilities.ki;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IKiCapability extends INBTSerializable<CompoundTag> {
    int MAX_LEVEL = 15;

    int getKi();

    int getMaxKi();

    int getKiLevel();

    void setKi(int ki);

    void setMaxKi(int max);

    void setKiLevel(int level);
}
