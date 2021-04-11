package com.mineria.mod.blocks.titane_extractor.slots;

import com.mineria.mod.init.ItemsInit;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFilter extends SlotItemHandler
{
	public SlotFilter(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return stack.isItemEqual(new ItemStack(ItemsInit.FILTER));
    }
}
