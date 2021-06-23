package com.mineria.mod.blocks.titane_extractor;

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
import net.minecraftforge.items.SlotItemHandler;

public class TitaneExtractorContainer extends MineriaContainer<TitaneExtractorTileEntity>
{
    private final FunctionalIntReferenceHolder currentExtractTime;

    public TitaneExtractorContainer(int id, PlayerInventory playerInv, TitaneExtractorTileEntity tileEntity)
    {
        super(ContainerTypeInit.TITANE_EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 20, 101);

        this.trackInt(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static TitaneExtractorContainer create(int windowID, PlayerInventory playerInv, PacketBuffer data)
    {
        return new TitaneExtractorContainer(windowID, playerInv, getTileEntity(TitaneExtractorTileEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(TitaneExtractorTileEntity tile)
    {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 10, 7));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 41, 7));
        this.addSlot(new FilterSlot(tile.getInventory(), 2, 24, 78));
        this.addSlot(new OutputSlot(tile.getInventory(), 3, 95, 47));
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

            if (index == 3)
            {
                if(!this.mergeItemStack(slotStack, 4, 40, true)) return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, stackToTransfer);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                /*if (!TitaneExtractorRecipes.getInstance().getExtractingResult(slotStack, slotStack).isEmpty())
                {
                    if (!this.mergeItemStack(slotStack, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else*/ if(slotStack.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
                {
                    if (!this.mergeItemStack(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(slotStack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || slotStack.getItem().equals(Items.WATER_BUCKET))
                {
                    if (!this.mergeItemStack(slotStack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (slotStack.getItem().equals(ItemsInit.FILTER))
                {
                    if (!this.mergeItemStack(slotStack, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 4 && index < 31)
                {
                    if (!this.mergeItemStack(slotStack, 31, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 31 && index < 40 && !this.mergeItemStack(slotStack, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(slotStack, 4, 40, false))
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
