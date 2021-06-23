package com.mineria.mod.blocks.extractor;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.blocks.titane_extractor.slots.FilterSlot;
import com.mineria.mod.blocks.titane_extractor.slots.OutputSlot;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import com.mineria.mod.util.MineriaContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExtractorContainer extends MineriaContainer<ExtractorTileEntity>
{
    private final FunctionalIntReferenceHolder currentExtractTime;

    public ExtractorContainer(int id, PlayerInventory playerInv, ExtractorTileEntity tileEntity)
    {
        super(ContainerTypeInit.EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 6, 116);

        this.trackInt(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static ExtractorContainer create(int id, PlayerInventory playerInv, PacketBuffer buffer)
    {
        return new ExtractorContainer(id, playerInv, getTileEntity(ExtractorTileEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(ExtractorTileEntity tile)
    {
        IItemHandler handler = tile.getInventory();
        this.addSlot(new SlotItemHandler(handler, 0, 6, 18));
        this.addSlot(new SlotItemHandler(handler, 1, 41, 18));
        this.addSlot(new FilterSlot(handler, 2, 23, 90));
        this.addSlot(new OutputSlot(handler, 3, 190, 90));
        this.addSlot(new OutputSlot(handler, 4, 190, 68));
        this.addSlot(new OutputSlot(handler, 5, 190, 46));
        this.addSlot(new OutputSlot(handler, 6, 190, 25));
        this.addSlot(new OutputSlot(handler, 7, 120, 6));
        this.addSlot(new OutputSlot(handler, 8, 68, 25));
        this.addSlot(new OutputSlot(handler, 9, 68, 46));
    }

    @OnlyIn(Dist.CLIENT)
    public int getExtractTimeScaled()
    {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
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

            if (index > 2 && index < 10)
            {
                if(!this.mergeItemStack(slotStack, 10, 46, true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                if(slotStack.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
                {
                    if (!this.mergeItemStack(slotStack, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || slotStack.getItem().equals(Items.WATER_BUCKET))
                {
                    if (!this.mergeItemStack(slotStack, 1, 2, false))
                        return ItemStack.EMPTY;
                }
                else if (slotStack.getItem().equals(ItemsInit.FILTER))
                {
                    if (!this.mergeItemStack(slotStack, 2, 3, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 37)
                {
                    if (!this.mergeItemStack(slotStack, 37, 46, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 46 && !this.mergeItemStack(slotStack, 10, 37, false))
                    return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(slotStack, 10, 46, false))
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

            if (slotStack.getCount() == stackToTransfer.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stackToTransfer;
    }
}
