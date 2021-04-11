package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.blocks.xp_block.slots.SlotXpBlock;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ContainerXpBlock extends Container
{
	private final TileEntityXpBlock tile;
    
    public ContainerXpBlock(InventoryPlayer playerInventory, TileEntityXpBlock tile)
    {
        this.tile = tile;
        addSlotToContainer(new SlotXpBlock(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 0, 113, 21));
        
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
        if (!this.tile.getWorld().isRemote)
        {
            CustomItemStackHandler handler = tile.getInventory();
            ItemStack previousStack = handler.getStackInSlot(0);
            handler.setStackInSlot(0, ItemStack.EMPTY);

            if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected())
            {
                playerIn.dropItem(previousStack, false);
            }
            else
            {
                playerIn.inventory.placeItemBackInInventory(tile.getWorld(), previousStack);
            }
        }
    }
    
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return false;
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
        Slot slot = this.inventorySlots.get(0);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stackToTransfer = slotStack.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(slotStack, 1, 37, true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else if (this.mergeItemStack(slotStack, 0, 1, false))
            {
                return ItemStack.EMPTY;
            }
            else if (index >= 1 && index < 28)
            {
                if (!this.mergeItemStack(slotStack, 28, 37, false))
                    return ItemStack.EMPTY;
            }
            else if (index >= 28 && index < 37)
            {
                if (!this.mergeItemStack(slotStack, 1, 28, false))
                    return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(slotStack, 1, 37, false))
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
