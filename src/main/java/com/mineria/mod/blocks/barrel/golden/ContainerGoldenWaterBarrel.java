package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.items.ItemDrink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerGoldenWaterBarrel extends Container
{
    private final TileEntityGoldenWaterBarrel tile;

    public ContainerGoldenWaterBarrel(InventoryPlayer playerInventory, TileEntityGoldenWaterBarrel tile)
    {
        this.tile = tile;
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        for(int y = 0; y < 4; y++)
        {
            this.addSlotToContainer(new SlotItemHandler(handler, y, 26, 16 + y * 18)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof ItemDrink || stack.getItem().equals(ItemsInit.CUP);
                }
            });
        }

        for(int y = 0; y < 4; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                this.addSlotToContainer(new SlotItemHandler(handler, x + y*4 + 4, 62 + x*18, 16 + y*18)
                {
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack)
                    {
                        return stack.getItem() instanceof ItemPotion || stack.getItem() instanceof ItemGlassBottle;
                    }
                });
            }
        }

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 8 + x*18, 98 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 156));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tile.isUsableByPlayer(playerIn);
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

            if (index < 9)
            {
                if (!this.mergeItemStack(slotStack, 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(slotStack, 0, 9, false))
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
        }

        return stackToTransfer;
    }
}
