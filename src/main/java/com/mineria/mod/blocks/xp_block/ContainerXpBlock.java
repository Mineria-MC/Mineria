package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.blocks.xp_block.slots.SlotXpBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerXpBlock extends Container
{
	private final TileEntityXpBlock tileXpBlock;
    
    public ContainerXpBlock(InventoryPlayer playerInventory, TileEntityXpBlock xpBlockInv)
    {
        this.tileXpBlock = xpBlockInv;
        addSlotToContainer(new SlotXpBlock(playerInventory.player, xpBlockInv, 0, 113, 21));
        
        for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 9 + x*18, 56 + y*18));
			}
		}
		
		for(int x = 0; x < 9; x++)
		{
			this.addSlotToContainer(new Slot(playerInventory, x, 9 + x * 18, 114));
		}
	}
    
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.tileXpBlock.getWorld().isRemote)
        {
            this.clearContainer(playerIn, this.tileXpBlock.getWorld(), this.tileXpBlock);
        }
        this.tileXpBlock.closeInventory(playerIn);
    }
    
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return false;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileXpBlock.isUsableByPlayer(playerIn);
	}
	
	@Override
	public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileXpBlock);
    }
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(0);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 37, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (this.mergeItemStack(itemstack1, 0, 1, false)) //Forge Fix Shift Clicking in beacons with stacks larger then 1.
            {
                return ItemStack.EMPTY;
            }
            else if (index >= 1 && index < 28)
            {
                if (!this.mergeItemStack(itemstack1, 28, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 28 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 1, 37, false))
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
