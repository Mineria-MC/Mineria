package io.github.mineria_mc.mineria.util;

import net.minecraft.world.item.ItemStack;

public interface HopperHandler {
    boolean canInsertHopperItem(int index, ItemStack stack);

    boolean canExtractHopperItem(int index, ItemStack stack);
}
