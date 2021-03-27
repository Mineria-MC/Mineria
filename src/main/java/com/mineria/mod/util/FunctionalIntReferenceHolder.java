package com.mineria.mod.util;

import net.minecraft.util.IntReferenceHolder;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * This class holds an {@link IntSupplier} and an {@link IntConsumer}.
 * Used for the Int Trackers in Containers
 */
public class FunctionalIntReferenceHolder extends IntReferenceHolder
{
    private final IntSupplier getter;
    private final IntConsumer setter;

    public FunctionalIntReferenceHolder(final IntSupplier getter, final IntConsumer setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public int get()
    {
        return this.getter.getAsInt();
    }

    @Override
    public void set(int value)
    {
        this.setter.accept(value);
    }
}
