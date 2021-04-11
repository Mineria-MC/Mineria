package com.mineria.mod.blocks.extractor;

import com.mineria.mod.blocks.barrel.BlockBarrel;
import com.mineria.mod.blocks.titane_extractor.slots.SlotFilter;
import com.mineria.mod.blocks.titane_extractor.slots.SlotOutput;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
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
	private final TileEntityExtractor tile;
	private int extractTime, totalExtractTime;
	
	public ContainerExtractor(InventoryPlayer playerInv, TileEntityExtractor tile)
	{
		this.tile = tile;
		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		this.addSlotToContainer(new SlotItemHandler(handler, 0, 6, 18));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 41, 18));
		this.addSlotToContainer(new SlotFilter(handler, 2, 23, 90));
		this.addSlotToContainer(new SlotOutput(handler, 3, 190, 90));
		this.addSlotToContainer(new SlotOutput(handler, 4, 190, 68));
		this.addSlotToContainer(new SlotOutput(handler, 5, 190, 46));
		this.addSlotToContainer(new SlotOutput(handler, 6, 190, 25));
		this.addSlotToContainer(new SlotOutput(handler, 7, 120, 6));
		this.addSlotToContainer(new SlotOutput(handler, 8, 68, 25));
        this.addSlotToContainer(new SlotOutput(handler, 9, 68, 46));
		
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

		for (IContainerListener listener : this.listeners)
		{
			if (this.extractTime != this.tile.getField(0))
				listener.sendWindowProperty(this, 0, this.tile.getField(0));
			if (this.totalExtractTime != this.tile.getField(1))
				listener.sendWindowProperty(this, 1, this.tile.getField(1));
		}
		
		this.extractTime = this.tile.getField(0);
		this.totalExtractTime = this.tile.getField(1);
	}

	@Override
	public void updateProgressBar(int id, int data)
	{
		this.tile.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tile.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
		ItemStack stackToTransfer = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			stackToTransfer = slotStack.copy();

			if (index > 2 && index < 10)
			{
				if(!this.mergeItemStack(slotStack, 10, 46, true))
					return ItemStack.EMPTY;

				slot.onSlotChange(slotStack, stackToTransfer);
			}
			else if (index != 0 && index != 1 && index != 2)
			{
				if(slotStack.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
				{
					if (!this.mergeItemStack(slotStack, 0, 1, false))
						return ItemStack.EMPTY;
				}
				else if(slotStack.getItem() instanceof BlockBarrel.ItemBlockBarrel)
				{
					if (!this.mergeItemStack(slotStack, 1, 2, false))
						return ItemStack.EMPTY;
				}
				else if (slotStack.getItem().equals(ItemsInit.FILTER))
				{
					if (!this.mergeItemStack(slotStack, 2, 3, false))
						return ItemStack.EMPTY;
				}
				else if (index < 37)
				{
					if (!this.mergeItemStack(slotStack, 37, 46, false))
						return ItemStack.EMPTY;
				}
				else if (index < 46 && !this.mergeItemStack(slotStack, 10, 37, false))
					return ItemStack.EMPTY;
			}
			else if (!this.mergeItemStack(slotStack, 10, 46, false))
			{
				return ItemStack.EMPTY;
			}

			if (slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (slotStack.getCount() == stackToTransfer.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, slotStack);
		}

		return stackToTransfer;
    }
}
