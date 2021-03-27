package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.blocks.xp_block.slots.XpBlockSlot;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.util.MineriaContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class XpBlockContainer extends MineriaContainer<XpBlockTileEntity>
{
    public XpBlockContainer(int id, PlayerInventory playerInv, XpBlockTileEntity tileEntity)
    {
        super(ContainerTypeInit.XP_BLOCK.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 9, 56);
    }

    public static XpBlockContainer create(int id, PlayerInventory playerInv, PacketBuffer buffer)
    {
        return new XpBlockContainer(id, playerInv, getTileEntity(XpBlockTileEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(XpBlockTileEntity tile)
    {
        this.addSlot(new XpBlockSlot(tile.getInventory(), 0, 113, 21));
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.tile.getWorld().isRemote)
        {
            this.clearContainer(playerIn, this.tile.getWorld(), this.tile);
        }
        this.tile.closeInventory(playerIn);
    }

    public BlockPos getTileEntityPos()
    {
        return tile.getPos();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
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
            else if (this.mergeItemStack(itemstack1, 0, 1, false))
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
