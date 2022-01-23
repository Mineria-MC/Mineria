package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.blocks.titane_extractor.TitaneExtractorTileEntity;
import com.mineria.mod.common.containers.slots.ExtractorOutputSlot;
import com.mineria.mod.common.containers.slots.FilterSlot;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class TitaneExtractorContainer extends MineriaContainer<TitaneExtractorTileEntity>
{
    private final FunctionalIntReferenceHolder currentExtractTime;

    public TitaneExtractorContainer(int id, PlayerInventory playerInv, TitaneExtractorTileEntity tileEntity)
    {
        super(MineriaContainerTypes.TITANE_EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 101);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
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
        this.addSlot(new ExtractorOutputSlot(tile.getInventory(), 3, 95, 47));
    }

    @OnlyIn(Dist.CLIENT)
    public int getExtractTimeScaled()
    {
        return this.currentExtractTime.get() != 0 && this.tile.totalExtractTime != 0 ? this.currentExtractTime.get() * 53 / this.tile.totalExtractTime : 0;
    }

    /*@Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if (index == 3)
            {
                if(!this.moveItemStackTo(slotStack, 4, 40, true)) return ItemStack.EMPTY;

                slot.onQuickCraft(slotStack, stackToTransfer);
            }
            else if (index != 0 && index != 1 && index != 2)
            {
                *//*if (!TitaneExtractorRecipes.getInstance().getExtractingResult(slotStack, slotStack).isEmpty())
                {
                    if (!this.mergeItemStack(slotStack, 0, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else*//* if(slotStack.getItem().equals(new ItemStack(BlocksInit.MINERAL_SAND).getItem()))
                {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(slotStack.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem || slotStack.getItem().equals(Items.WATER_BUCKET))
                {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (slotStack.getItem().equals(ItemsInit.FILTER))
                {
                    if (!this.moveItemStackTo(slotStack, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 4 && index < 31)
                {
                    if (!this.moveItemStackTo(slotStack, 31, 40, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 31 && index < 40 && !this.moveItemStackTo(slotStack, 4, 31, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, 4, 40, false))
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
        return new StackTransferHandler(3)
                .withSpecialInput(1, AbstractWaterBarrelTileEntity::checkWaterFromStack)
                .withSpecialInput(2, stack -> stack.getItem().equals(MineriaItems.FILTER));
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return stack.getItem().equals(MineriaBlocks.getItemFromBlock(MineriaBlocks.MINERAL_SAND)) ? 0 : -1;
    }
}
