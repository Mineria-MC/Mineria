package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

@Deprecated
public interface IPoisonCapability extends INBTSerializable<CompoundTag> {
    void applyPoison(PoisonSource source);

    int exposureCount(PoisonSource source);

    long getTickCount(PoisonSource source);

    void tick();

    void removePoison(PoisonSource source);
}
