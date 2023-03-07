package io.github.mineria_mc.mineria.common.containers.slots;

import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FilterSlot extends SlotItemHandler {
    public FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.is(MineriaItems.FILTER.get());
    }
}
