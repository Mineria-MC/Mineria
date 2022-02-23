package com.mineria.mod.util;

import net.minecraft.world.inventory.DataSlot;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * This class holds an {@link IntSupplier} and an {@link IntConsumer}.
 * Used to track fields from Tile Entities in Containers
 *
 * TODOLTR Replace with data slots (This works but not very well)
 */
public class FunctionalIntReferenceHolder extends DataSlot
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
