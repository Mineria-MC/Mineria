package com.mineria.mod.blocks.extractor.slots;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotExtractorFilter extends SlotItemHandler
{
    public SlotExtractorFilter(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return stack.getItem().equals(ItemsInit.FILTER);
    }
}
