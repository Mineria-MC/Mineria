package com.mineria.mod.blocks.infuser.slots;

import com.mineria.mod.blocks.infuser.TileEntityInfuser;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InfuserFuelSlot extends Slot
{
    public InfuserFuelSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
         return TileEntityInfuser.isItemFuel(stack);
    }
}
