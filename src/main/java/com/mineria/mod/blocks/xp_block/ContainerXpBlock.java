package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.blocks.xp_block.slots.SlotXpBlock;
import com.mineria.mod.init.BlocksInit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerXpBlock extends Container
{
	private final TileEntityXpBlock tileXpBlock;
	private final World world;
	private final BlockPos pos;
    private final EntityPlayer player;
    
    public ContainerXpBlock(InventoryPlayer playerInventory, TileEntityXpBlock xpBlockInv, World worldIn, BlockPos posIn)
    {
    	this.world = worldIn;
        this.pos = posIn;
        this.player = playerInventory.player;
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

        if (!this.world.isRemote)
        {
            this.clearContainer(playerIn, this.world, this.tileXpBlock);
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
		if (this.world.getBlockState(this.pos).getBlock() != BlocksInit.xp_block)
        {
            return false;
        }
        else
        {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
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
