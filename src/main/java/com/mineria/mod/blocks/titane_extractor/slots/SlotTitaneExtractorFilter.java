package com.mineria.mod.blocks.titane_extractor.slots;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTitaneExtractorFilter extends Slot
{
	public SlotTitaneExtractorFilter(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return stack.isItemEqual(new ItemStack(ItemsInit.filter));
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return super.getItemStackLimit(stack);
    }
}
