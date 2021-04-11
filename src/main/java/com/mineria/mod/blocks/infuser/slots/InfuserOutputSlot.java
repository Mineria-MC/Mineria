package com.mineria.mod.blocks.infuser.slots;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class InfuserOutputSlot extends SlotItemHandler
{
    public InfuserOutputSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.isItemEqual(new ItemStack(ItemsInit.CUP));
    }
}
