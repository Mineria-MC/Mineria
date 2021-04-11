package com.mineria.mod.blocks.titane_extractor.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutput extends SlotItemHandler
{
	public SlotOutput(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
