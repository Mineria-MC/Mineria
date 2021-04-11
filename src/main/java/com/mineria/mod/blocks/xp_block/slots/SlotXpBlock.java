package com.mineria.mod.blocks.xp_block.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotXpBlock extends SlotItemHandler
{
	public SlotXpBlock(IItemHandler inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return 16;
    }
}
