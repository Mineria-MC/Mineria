package com.mineria.mod.blocks.extractor;

import com.mineria.mod.blocks.extractor.slots.SlotExtractorFilter;
import com.mineria.mod.blocks.extractor.slots.SlotExtractorOutput;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.items.ItemBlockBarrel;
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
	
	public ContainerExtractor(InventoryPlayer playerInv, TileEntityExtractor tileentity)
	{
		this.tileentity = tileentity;
		IItemHandler handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		this.addSlotToContainer(new SlotItemHandler(handler, 0, 6, 18));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 41, 18));
		this.addSlotToContainer(new SlotExtractorFilter(handler, 2, 23, 90));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 3, 190, 90));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 4, 190, 68));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 5, 190, 46));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 6, 190, 25));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 7, 120, 6));
		this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 8, 68, 25));
        this.addSlotToContainer(new SlotExtractorOutput(playerInv.player, handler, 9, 68, 46));
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInv, x + y*9 + 9, 6 + x*18, 116 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInv, x, 6 + x * 18, 174));
		}
	}

	@Override
	public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.listeners.size(); ++i) 
		{
			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			
			if(this.extractTime != this.tileentity.getField(0)) listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
			if(this.totalExtractTime != this.tileentity.getField(1)) listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
		}
		
		this.extractTime = this.tileentity.getField(0);
		this.totalExtractTime = this.tileentity.getField(1);
	}

	@Override
	public void updateProgressBar(int id, int data)
	{
		this.tileentity.setField(id, data);
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

			if (index > 2 && index < 10)
			{
				if(!this.mergeItemStack(itemstack1, 10, 46, true)) return ItemStack.EMPTY;

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0 && index != 1 && index != 2)
			{
				if (!ExtractorRecipes.getInstance().getExtractingResult(itemstack1, itemstack1).isEmpty())
				{
					if (!this.mergeItemStack(itemstack1, 0, 2, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if(itemstack1.getItem().equals(new ItemStack(BlocksInit.mineral_sand).getItem()))
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if(itemstack1.getItem() instanceof ItemBlockBarrel)
				{
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (itemstack1.getItem().equals(ItemsInit.filter))
				{
					if (!this.mergeItemStack(itemstack1, 2, 3, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 10 && index < 37)
				{
					if (!this.mergeItemStack(itemstack1, 37, 46, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 37 && index < 46 && !this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 10, 46, false))
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
