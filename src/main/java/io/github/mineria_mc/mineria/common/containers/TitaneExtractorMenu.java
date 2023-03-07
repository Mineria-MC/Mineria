package io.github.mineria_mc.mineria.common.containers;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.blocks.titane_extractor.TitaneExtractorBlockEntity;
import io.github.mineria_mc.mineria.common.containers.slots.ExtractorOutputSlot;
import io.github.mineria_mc.mineria.common.containers.slots.FilterSlot;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.util.FunctionalIntReferenceHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class TitaneExtractorMenu extends MineriaMenu<TitaneExtractorBlockEntity> {
    private final FunctionalIntReferenceHolder currentExtractTime;

    public TitaneExtractorMenu(int id, Inventory playerInv, TitaneExtractorBlockEntity tileEntity) {
        super(MineriaMenuTypes.TITANE_EXTRACTOR.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 101);

        this.addDataSlot(currentExtractTime = new FunctionalIntReferenceHolder(() -> tileEntity.extractTime, (value) -> tileEntity.extractTime = value));
    }

    public static TitaneExtractorMenu create(int windowID, Inventory playerInv, FriendlyByteBuf data) {
        return new TitaneExtractorMenu(windowID, playerInv, getTileEntity(TitaneExtractorBlockEntity.class, playerInv, data));
    }

    @Override
    protected void createInventorySlots(TitaneExtractorBlockEntity tile) {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 10, 7));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 41, 7));
        this.addSlot(new FilterSlot(tile.getInventory(), 2, 24, 78));
        this.addSlot(new ExtractorOutputSlot(tile.getInventory(), 3, 95, 47));
    }

    @OnlyIn(Dist.CLIENT)
    public int getExtractTimeScaled() {
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
    protected StackTransferHandler getStackTransferHandler() {
        return new StackTransferHandler(3)
                .withSpecialInput(1, stack -> AbstractWaterBarrelBlockEntity.checkFluidFromStack(stack, Fluids.WATER))
                .withSpecialInput(2, stack -> stack.is(MineriaItems.FILTER.get()));
    }

    @Override
    protected int getIndexForRecipe(ItemStack stack) {
        return stack.getItem().equals(MineriaBlocks.getItemFromBlock(MineriaBlocks.MINERAL_SAND.get())) ? 0 : -1;
    }

    @Nullable
    @Override
    protected RecipeType<?> getRecipeType() {
        return null;
    }
}
