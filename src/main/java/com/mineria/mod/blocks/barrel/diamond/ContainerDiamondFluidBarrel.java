package com.mineria.mod.blocks.barrel.diamond;

import com.mineria.mod.items.ItemBarrelUpgrade;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/** Not finished
 * We currently have issues with the diamond fluid barrel, so it'll be released later.
 */
public class ContainerDiamondFluidBarrel extends Container
{
    private final TileEntityDiamondFluidBarrel tile;

    public ContainerDiamondFluidBarrel(InventoryPlayer playerInventory, TileEntityDiamondFluidBarrel tile)
    {
        this.inventorySlots = new ArrayList<>(47);
        this.tile = tile;
        IItemHandler upgrades = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

        for(int y = 0; y < 3; y++)
        {
            this.addSlotToContainer(new SlotItemHandler(upgrades, y, 15, 18 + y * 18)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof ItemBarrelUpgrade;
                }
            });
        }

        TileEntityDiamondFluidBarrel.DiamondFluidBarrelISH inventory = tile.getInventory();
        TileEntityDiamondFluidBarrel.DiamondFluidBarrelISH potions = tile.getPotionsInventory();
        inventory.setContainer(this);
        potions.setContainer(this);

        addSlots();

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y*9 + 9, 19 + x*18, 84 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 19 + x * 18, 142));
        }
    }

    public void addSlots()
    {
        CustomItemStackHandler inventory = tile.getInventory();
        CustomItemStackHandler potions = tile.getPotionsInventory();

        for(Slot slot : this.inventorySlots)
        {
            if(slot instanceof SlotItemHandler)
            {
                IItemHandler handler = ((SlotItemHandler) slot).getItemHandler();
                if(handler == inventory || handler == potions)
                {
                    this.inventoryItemStacks.remove(this.inventorySlots.indexOf(slot));
                    this.inventorySlots.remove(slot);
                }
            }
        }

        for(int count = 0; count < inventory.getSlots(); count++)
        {
            this.addSlotToContainer(new SlotItemHandler(inventory, count, 41 + count * 18, 22));
        }

        for(int count = 0; count < potions.getSlots(); count++)
        {
            this.addSlotToContainer(new SlotItemHandler(potions, count, 41 + count * 18, 47)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof ItemPotion || stack.getItem() instanceof ItemGlassBottle;
                }
            });
        }
    }

    @Override
    public Slot getSlot(int slotId)
    {
        return slotId >= this.inventorySlots.size() ? new SlotItemHandler(this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP), 0, 41, 60) : this.inventorySlots.get(slotId);
    }

    @Override
    public void detectAndSendChanges()
    {

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return tile.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return ItemStack.EMPTY;
        /*ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stackToTransfer = slotStack.copy();

            if(index < 20)
            {
                if (!this.mergeItemStack(slotStack, 20, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else
            {
                if(slotStack.getItem() instanceof ItemDrink || slotStack.getItem().equals(ItemsInit.CUP))
                {
                    if(!this.mergeItemStack(slotStack, 0, 4, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof ItemPotion || slotStack.getItem() instanceof ItemGlassBottle)
                {
                    if(!this.mergeItemStack(slotStack, 4, 20, false))
                        return ItemStack.EMPTY;
                }
                else if(index < 45)
                {
                    if(!this.mergeItemStack(slotStack, this.inventorySlots.size() - 9, this.inventorySlots.size(), false))
                        return ItemStack.EMPTY;
                }
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

        return stackToTransfer;*/
    }
}
