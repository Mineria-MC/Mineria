package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.barrel.golden.GoldenWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.items.DrinkItem;
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
        super(MineriaContainerTypes.GOLDEN_WATER_BARREL.get(), id, tileEntity);

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
                public boolean mayPlace(@Nonnull ItemStack stack)
                {
                    return stack.getItem() instanceof DrinkItem || stack.getItem().equals(MineriaItems.CUP);
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
                    public boolean mayPlace(@Nonnull ItemStack stack)
                    {
                        return stack.getItem() instanceof PotionItem || stack.getItem() instanceof GlassBottleItem;
                    }
                });
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if(index < 20)
            {
                if (!this.moveItemStackTo(slotStack, 20, this.slots.size(), true))
                    return ItemStack.EMPTY;

                slot.onQuickCraft(slotStack, stackToTransfer);
            }
            else
            {
                if(slotStack.getItem() instanceof DrinkItem || slotStack.getItem().equals(MineriaItems.CUP))
                {
                    if(!this.moveItemStackTo(slotStack, 0, 4, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof PotionItem || slotStack.getItem() instanceof GlassBottleItem)
                {
                    if(!this.moveItemStackTo(slotStack, 4, 20, false))
                        return ItemStack.EMPTY;
                }
                else if(index < 45)
                {
                    if(!this.moveItemStackTo(slotStack, this.slots.size() - 9, this.slots.size(), false))
                        return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (slotStack.getCount() == stackToTransfer.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stackToTransfer;
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return 0;
    }
}
