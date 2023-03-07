package io.github.mineria_mc.mineria.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

/**
 * A class that handles an inventory for a TileEntity
 */
public class MineriaItemStackHandler extends ItemStackHandler implements HopperHandler {
    public MineriaItemStackHandler(int size, ItemStack... stacks) {
        super(size);

        for (int index = 0; index < stacks.length; index++) {
            this.stacks.set(index, stacks[index]);
        }
    }

    public void clear() {
        for (int index = 0; index < this.getSlots(); index++) {
            this.stacks.set(index, ItemStack.EMPTY);
            this.onContentsChanged(index);
        }
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public NonNullList<ItemStack> toNonNullList() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(this.stacks);
        return items;
    }

    public void setNonNullList(NonNullList<ItemStack> items) {
        if (items.size() == 0)
            return;
        if (items.size() != this.getSlots())
            throw new IndexOutOfBoundsException("NonNullList must be same size as ItemStackHandler!");
        for (int index = 0; index < items.size(); index++) {
            this.stacks.set(index, items.get(index));
        }
    }

    @Override
    public String toString() {
        return "MineriaItemStackHandler[" + stacks + ']';
    }

    @Override
    public boolean canInsertHopperItem(int index, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canExtractHopperItem(int index, ItemStack stack) {
        return false;
    }
}
