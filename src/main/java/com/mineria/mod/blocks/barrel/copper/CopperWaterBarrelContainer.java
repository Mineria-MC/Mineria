package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.util.MineriaContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CopperWaterBarrelContainer extends MineriaContainer<CopperWaterBarrelTileEntity>
{
    public CopperWaterBarrelContainer(int id, PlayerInventory playerInv, CopperWaterBarrelTileEntity tileEntity)
    {
        super(ContainerTypeInit.COPPER_WATER_BARREL.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 44);
    }

    public static CopperWaterBarrelContainer create(int windowID, PlayerInventory playerInv, PacketBuffer data)
    {
        return new CopperWaterBarrelContainer(windowID, playerInv, getTileEntity(CopperWaterBarrelTileEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(CopperWaterBarrelTileEntity tile)
    {
        for(int slotIndex = 0; slotIndex < 8 ; slotIndex++)
        {
            this.addSlot(new SlotItemHandler(tile.getInventory(), slotIndex, 17 + slotIndex * 18, 13) {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return !(stack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || stack.getItem().equals(Items.SHULKER_BOX));
                }
            });
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
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