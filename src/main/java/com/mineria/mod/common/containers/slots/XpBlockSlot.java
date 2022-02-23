package com.mineria.mod.common.containers.slots;

import net.minecraft.world.item.ItemStack;
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
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return false;
    }

    @Override
    public int getMaxStackSize()
    {
        return 16;
    }
}
