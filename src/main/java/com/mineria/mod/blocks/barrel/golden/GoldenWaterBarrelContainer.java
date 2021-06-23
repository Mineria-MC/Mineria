package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.items.DrinkItem;
import com.mineria.mod.util.MineriaContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GoldenWaterBarrelContainer extends MineriaContainer<GoldenWaterBarrelTileEntity>
{
    public GoldenWaterBarrelContainer(int id, PlayerInventory playerInv, GoldenWaterBarrelTileEntity tileEntity)
    {
        super(ContainerTypeInit.GOLDEN_WATER_BARREL.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 98);
    }

    public static GoldenWaterBarrelContainer create(int id, PlayerInventory playerInv, PacketBuffer data)
    {
        return new GoldenWaterBarrelContainer(id, playerInv, getTileEntity(GoldenWaterBarrelTileEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(GoldenWaterBarrelTileEntity tile)
    {
        for(int y = 0; y < 4; y++)
        {
            this.addSlot(new SlotItemHandler(tile.getInventory(), y, 26, 16 + y * 18)
            {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof DrinkItem || stack.getItem().equals(ItemsInit.CUP);
                }
            });
        }

        for(int y = 0; y < 4; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                this.addSlot(new SlotItemHandler(tile.getInventory(), x + y*4 + 4, 62 + x*18, 16 + y*18)
                {
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack)
                    {
                        return stack.getItem() instanceof PotionItem || stack.getItem() instanceof GlassBottleItem;
                    }
                });
            }
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

            if(index < 20)
            {
                if (!this.mergeItemStack(slotStack, 20, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else
            {
                if(slotStack.getItem() instanceof DrinkItem || slotStack.getItem().equals(ItemsInit.CUP))
                {
                    if(!this.mergeItemStack(slotStack, 0, 4, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof PotionItem || slotStack.getItem() instanceof GlassBottleItem)
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

        return stackToTransfer;
    }
}
