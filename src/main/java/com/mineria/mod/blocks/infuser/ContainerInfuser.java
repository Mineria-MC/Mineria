package com.mineria.mod.blocks.infuser;

import com.mineria.mod.blocks.infuser.slots.InfuserFuelSlot;
import com.mineria.mod.blocks.infuser.slots.InfuserOutputSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInfuser extends Container
{
    private final TileEntityInfuser tileInfuser;
    private int infuseTime;
    private int totalInfuseTime;
    private int burnTime;
    private int currentBurnTime;

    public ContainerInfuser(InventoryPlayer playerInventory, TileEntityInfuser infuserInventory)
    {
        this.tileInfuser = infuserInventory;
        this.addSlotToContainer(new Slot(infuserInventory, 0, 14, 10));
        this.addSlotToContainer(new Slot(infuserInventory, 1, 47, 35));
        this.addSlotToContainer(new InfuserFuelSlot(infuserInventory, 2, 130, 35));
        this.addSlotToContainer(new InfuserOutputSlot(infuserInventory, 3, 91, 35));

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

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileInfuser);
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.infuseTime != this.tileInfuser.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileInfuser.getField(2));
            }

            if (this.burnTime != this.tileInfuser.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileInfuser.getField(0));
            }

            if (this.currentBurnTime != this.tileInfuser.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileInfuser.getField(1));
            }

            if (this.totalInfuseTime != this.tileInfuser.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileInfuser.getField(3));
            }
        }

        this.infuseTime = this.tileInfuser.getField(2);
        this.burnTime = this.tileInfuser.getField(0);
        this.currentBurnTime = this.tileInfuser.getField(1);
        this.totalInfuseTime = this.tileInfuser.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileInfuser.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileInfuser.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index == 2)
            {
                if(!this.mergeItemStack(stack1, 3, 39, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack1, stack);
            }
            else if(index != 1 && index != 0)
            {
                if(!InfuserRecipes.getInstance().getInfusingResult(stack1, stack1).isEmpty())
                {
                    if(!this.mergeItemStack(stack1, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(TileEntityInfuser.isItemFuel(stack1))
                {
                    if(!this.mergeItemStack(stack1, 3, 4, false)) return ItemStack.EMPTY;
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(stack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(stack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(stack1, 3, 39, false))
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
