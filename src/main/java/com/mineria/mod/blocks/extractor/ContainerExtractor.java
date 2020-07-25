package com.mineria.mod.blocks.extractor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerExtractor extends Container
{
	private final TileEntityExtractor tileentity;
	private int extractTime, totalExtractTime;
	
	public ContainerExtractor(InventoryPlayer player, TileEntityExtractor tileentity) 
	{
		this.tileentity = tileentity;
		IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		this.addSlotToContainer(new SlotItemHandler(handler, 0, 3, 15));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 38, 15));
		this.addSlotToContainer(new SlotItemHandler(handler, 2, 20, 87));
		this.addSlotToContainer(new SlotItemHandler(handler, 3, 187, 87));
		this.addSlotToContainer(new SlotItemHandler(handler, 4, 187, 65));
		this.addSlotToContainer(new SlotItemHandler(handler, 5, 187, 43));
		this.addSlotToContainer(new SlotItemHandler(handler, 6, 187, 22));
		this.addSlotToContainer(new SlotItemHandler(handler, 7, 117, 3));
		this.addSlotToContainer(new SlotItemHandler(handler, 8, 65, 22));
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(player, x + y*9 + 9, 6 + x*18, 116 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(player, x, 6 + x * 18, 174));
		}
	}
	
	@Override
	public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.listeners.size(); ++i) 
		{
			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			
			if(this.extractTime != this.tileentity.getField(2)) listener.sendWindowProperty(this, 2, this.tileentity.getField(0));
			if(this.totalExtractTime != this.tileentity.getField(3)) listener.sendWindowProperty(this, 3, this.tileentity.getField(1));
		}
		
		this.extractTime = this.tileentity.getField(0);
		this.totalExtractTime = this.tileentity.getField(1);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileentity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!ExtractorRecipes.getInstance().getExtractingResult(itemstack1, itemstack1, itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
/*                else if (TileEntityTitaneExtractor.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }*/
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
