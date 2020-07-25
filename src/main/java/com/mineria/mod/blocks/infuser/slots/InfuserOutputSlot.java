package com.mineria.mod.blocks.infuser.slots;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InfuserOutputSlot extends Slot
{
    public InfuserOutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.isItemEqual(new ItemStack(ItemsInit.cup));
    }
}
