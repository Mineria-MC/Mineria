package com.mineria.mod.common.containers.slots;

import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class InfuserOutputSlot extends SlotItemHandler
{
    public InfuserOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack)
    {
        return stack.sameItem(new ItemStack(MineriaItems.CUP));
    }
}
