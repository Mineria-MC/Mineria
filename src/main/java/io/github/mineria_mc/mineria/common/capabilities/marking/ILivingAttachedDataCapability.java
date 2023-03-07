package io.github.mineria_mc.mineria.common.capabilities.marking;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

// TODO: Merge element capability and poison capability into a single capability (which needs an other name)
public interface ILivingAttachedDataCapability extends INBTSerializable<CompoundTag> {
    void tick();

    <T> void mark(AttachedDataType<T> type, T value);
    
    <T> boolean isMarked(AttachedDataType<T> type, T value);

    <T> int markCount(AttachedDataType<T> type, T value);
    
    <T> long ticksSinceLastMark(AttachedDataType<T> type, T value);

    <T> void unmark(AttachedDataType<T> type, T value);
}
