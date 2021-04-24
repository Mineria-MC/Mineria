package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTitaneExtractor extends Container
{
	private final TileEntityTitaneExtractor tile;
    private int extractTime, totalExtractTime;
	
    public ContainerTitaneExtractor(InventoryPlayer playerInventory, TileEntityTitaneExtractor tile)
    {
    	this.tile = tile;
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

    	this.addSlotToContainer(new SlotItemHandler(handler, 0, 10, 7));
		this.addSlotToContainer(new SlotItemHandler(handler, 1, 41, 7));
		this.addSlotToContainer(new SlotFilter(handler, 2, 24, 78));
		this.addSlotToContainer(new SlotOutput(handler, 3, 95, 47));
		
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 20 + x*18, 101 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 20 + x * 18, 159));
		}
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = this.listeners.get(i);

            if (this.extractTime != this.tile.getField(0))
            {
                listener.sendWindowProperty(this, 0, this.tile.getField(0));
            }

            if (this.totalExtractTime != this.tile.getField(1))
            {
                listener.sendWindowProperty(this, 1, this.tile.getField(1));
            }
        }

        this.extractTime = this.tile.getField(0);
        this.totalExtractTime = this.tile.getField(1);
    }
    
    @SideOnly(Side.CLIENT)
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
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index == 3)
            {
                if(!this.mergeItemStack(stack1, 4, 40, true)) return ItemStack.EMPTY;

                slot.onSlotChange(stack1, stack);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                if(stack1.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
                {
                    if (!this.mergeItemStack(stack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(stack1.getItem() instanceof AbstractBlockWaterBarrel.ItemBlockBarrel)
                {
                    if (!this.mergeItemStack(stack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (stack1.getItem().equals(ItemsInit.FILTER))
                {
                    if (!this.mergeItemStack(stack1, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 31)
                {
                    if (!this.mergeItemStack(stack1, 31, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 40 && !this.mergeItemStack(stack1, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(stack1, 4, 40, false))
            {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack1);
        }

        return stack;
    }
}
