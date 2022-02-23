package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.blocks.extractor.ExtractorTileEntity;
import com.mineria.mod.common.containers.slots.ExtractorOutputSlot;
import com.mineria.mod.common.containers.slots.FilterSlot;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ExtractorContainer extends MineriaContainer<ExtractorTileEntity>
{
    private final FunctionalIntReferenceHolder currentExtractTime;

    public ExtractorContainer(int id, Inventory playerInv, ExtractorTileEntity tileEntity)
    {
        super(MineriaContainerTypes.EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 118);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static ExtractorContainer create(int id, Inventory playerInv, FriendlyByteBuf buffer)
    {
        return new ExtractorContainer(id, playerInv, getTileEntity(ExtractorTileEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(ExtractorTileEntity tile)
    {
        IItemHandler handler = tile.getInventory();
        this.addSlot(new SlotItemHandler(handler, 0, 8, 20));
        this.addSlot(new SlotItemHandler(handler, 1, 43, 20));
        this.addSlot(new FilterSlot(handler, 2, 25, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 3, 137, 92));
        this.addSlot(new ExtractorOutputSlot(handler, 4, 137, 70));
        this.addSlot(new ExtractorOutputSlot(handler, 5, 137, 48));
        this.addSlot(new ExtractorOutputSlot(handler, 6, 137, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 7, 102, 8));
        this.addSlot(new ExtractorOutputSlot(handler, 8, 70, 27));
        this.addSlot(new ExtractorOutputSlot(handler, 9, 70, 48));
    }

    @OnlyIn(Dist.CLIENT)
    public int getExtractTimeScaled()
    {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
    }

   /* @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index > 2 && index < 10)
            {
                if(!this.moveItemStackTo(slotStack, 10, 46, true))
                    return ItemStack.EMPTY;

                slot.onQuickCraft(slotStack, stackToTransfer);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                if(slotStack.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
                {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if(slotStack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || slotStack.getItem().equals(Items.WATER_BUCKET))
                {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false))
                        return ItemStack.EMPTY;
                }
                else if (slotStack.getItem().equals(ItemsInit.FILTER))
                {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 37)
                {
                    if (!this.moveItemStackTo(slotStack, 37, 46, false))
                        return ItemStack.EMPTY;
                }
                else if (index < 46 && !this.moveItemStackTo(slotStack, 10, 37, false))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(slotStack, 10, 46, false))
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

            if (slotStack.getCount() == stackToTransfer.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stackToTransfer;
    }*/

    @Override
    protected StackTransferHandler getStackTransferHandler()
    {
        return new StackTransferHandler(3, 9)
                .withSpecialInput(1, AbstractWaterBarrelTileEntity::checkWaterFromStack)
                .withSpecialInput(2, stack -> stack.getItem().equals(MineriaItems.FILTER));
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return stack.getItem().equals(MineriaBlocks.getItemFromBlock(MineriaBlocks.MINERAL_SAND)) ? 0 : -1;
    }
}
