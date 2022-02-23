package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.common.blocks.barrel.copper.CopperWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaContainerTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CopperWaterBarrelContainer extends MineriaContainer<CopperWaterBarrelTileEntity>
{
    public CopperWaterBarrelContainer(int id, Inventory playerInv, CopperWaterBarrelTileEntity tileEntity)
    {
        super(MineriaContainerTypes.COPPER_WATER_BARREL.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 44);
    }

    public static CopperWaterBarrelContainer create(int windowID, Inventory playerInv, FriendlyByteBuf data)
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
                public boolean mayPlace(@Nonnull ItemStack stack)
                {
                    return !(stack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || stack.getItem().equals(Items.SHULKER_BOX));
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index < 9)
            {
                if (!this.moveItemStackTo(slotStack, 9, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, 0, 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
        }

        return stackToTransfer;
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return 0;
    }
}