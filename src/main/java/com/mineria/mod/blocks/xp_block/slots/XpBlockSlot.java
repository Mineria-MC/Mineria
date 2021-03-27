package com.mineria.mod.blocks.xp_block.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class XpBlockSlot extends SlotItemHandler
{
    public XpBlockSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return false;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 16;
    }
}
