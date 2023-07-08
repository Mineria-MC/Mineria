package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.common.enchantments.ElementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

@Deprecated
public interface IElementCapability extends INBTSerializable<CompoundTag> {
    void applyElement(@Nonnull ElementType element);

    int applicationCount(ElementType element);

    long getTickCount(ElementType element);

    void tick();

    void removeElement(ElementType element);
}
