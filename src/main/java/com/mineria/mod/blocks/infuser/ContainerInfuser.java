package com.mineria.mod.blocks.infuser;

import com.mineria.mod.blocks.barrel.AbstractBlockWaterBarrel;
import com.mineria.mod.blocks.infuser.slots.FuelSlot;
import com.mineria.mod.blocks.infuser.slots.InfuserOutputSlot;
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

public class ContainerInfuser extends Container
{
    private final TileEntityInfuser tile;
    private int infuseTime;
    private int totalInfuseTime;
    private int burnTime;
    private int currentBurnTime;

    public ContainerInfuser(InventoryPlayer playerInventory, TileEntityInfuser tile)
    {
        this.tile = tile;
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        this.addSlotToContainer(new SlotItemHandler(handler, 0, 14, 10));
        this.addSlotToContainer(new SlotItemHandler(handler, 1, 47, 35));
        this.addSlotToContainer(new FuelSlot(handler, 2, 130, 35));
        this.addSlotToContainer(new InfuserOutputSlot(handler, 3, 91, 35));

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners)
        {
            if (this.burnTime != this.tile.getField(0))
                listener.sendWindowProperty(this, 0, this.tile.getField(0));
            if (this.currentBurnTime != this.tile.getField(1))
                listener.sendWindowProperty(this, 1, this.tile.getField(1));
            if (this.infuseTime != this.tile.getField(2))
                listener.sendWindowProperty(this, 2, this.tile.getField(2));
            if (this.totalInfuseTime != this.tile.getField(3))
                listener.sendWindowProperty(this, 3, this.tile.getField(3));
        }

        this.burnTime = this.tile.getField(0);
        this.currentBurnTime = this.tile.getField(1);
        this.infuseTime = this.tile.getField(2);
        this.totalInfuseTime = this.tile.getField(3);
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
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stackToTransfer = slotStack.copy();

            if(index == 3)
            {
                if(!slotStack.getItem().equals(ItemsInit.CUP))
                {
                    if(!this.mergeItemStack(slotStack, 3, 40, true))
                        return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else if(index != 2 && index != 1 && index != 0)
            {
                if(!InfuserRecipes.getInstance().getInfusingResult(slotStack).isEmpty())
                {
                    if(!this.mergeItemStack(slotStack, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof AbstractBlockWaterBarrel.ItemBlockBarrel)
                {
                    if(!this.mergeItemStack(slotStack, 1, 2, false))
                        return ItemStack.EMPTY;
                }
                else if(TileEntityInfuser.isItemFuel(slotStack))
                {
                    if(!this.mergeItemStack(slotStack, 2, 3, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem().equals(ItemsInit.CUP))
                {
                    if(!this.mergeItemStack(slotStack, 3, 4, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 31)
                {
                    if (!this.mergeItemStack(slotStack, 31, 40, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 40 && !this.mergeItemStack(slotStack, 4, 31, false))
                    return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(slotStack, 4, 40, false))
                return ItemStack.EMPTY;

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
