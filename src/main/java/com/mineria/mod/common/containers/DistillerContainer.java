package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.distiller.DistillerTileEntity;
import com.mineria.mod.common.containers.slots.FuelSlot;
import com.mineria.mod.common.containers.slots.OutputSlot;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.recipe.DistillerRecipe;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class DistillerContainer extends MineriaContainer<DistillerTileEntity>
{
    private final DistillerTileEntity distiller;
    private final FunctionalIntReferenceHolder distillationTime;
    private final FunctionalIntReferenceHolder burnTime;
    private final FunctionalIntReferenceHolder currentBurnTime;

    public DistillerContainer(int id, Inventory playerInv, DistillerTileEntity distillerTile)
    {
        super(MineriaContainerTypes.DISTILLER.get(), id, distillerTile);

        this.distiller = distillerTile;

        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.addDataSlot(distillationTime = new FunctionalIntReferenceHolder(() -> distillerTile.distillationTime, value -> distillerTile.distillationTime = value));
        this.addDataSlot(burnTime = new FunctionalIntReferenceHolder(() -> distillerTile.burnTime, value -> distillerTile.burnTime = value));
        this.addDataSlot(currentBurnTime = new FunctionalIntReferenceHolder(() -> distillerTile.currentBurnTime, value -> distillerTile.currentBurnTime = value));
    }

    public static DistillerContainer create(int id, Inventory playerInv, FriendlyByteBuf buffer)
    {
        return new DistillerContainer(id, playerInv, getTileEntity(DistillerTileEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(DistillerTileEntity tile)
    {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 11, 12));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 11, 47));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 2, 55, 22));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 3, 87, 10));
        this.addSlot(new FuelSlot(tile.getInventory(), 4, 87, 44));
        this.addSlot(new OutputSlot(tile.getInventory(), 5, 133, 10));
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnTimeScaled(int pixels)
    {
        int currentBurnTime = this.currentBurnTime.get();

        if (currentBurnTime == 0)
        {
            currentBurnTime = 200;
        }

        return this.burnTime.get() * pixels / currentBurnTime;
    }

    @OnlyIn(Dist.CLIENT)
    public int getDistillationTime()
    {
        return this.distillationTime.get();
    }

    @OnlyIn(Dist.CLIENT)
    public int getTotalDistillationTime()
    {
        return this.tile.totalDistillationTime;
    }

    public DistillerTileEntity getTileEntity()
    {
        return distiller;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler()
    {
        return new StackTransferHandler(5).withFuel(4, null);
    }

    /*@Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack stackToTransfer = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        final int outputIndex = 5;
        final int[] inputs = new int[] {0, 1, 2, 3, 4};
        final int lastIndex = 41;
        final int lastInventoryIndex = lastIndex - 9;

        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stackToTransfer = slotStack.copy();

            if(index == outputIndex)
            {
                if(!this.moveItemStackTo(slotStack, outputIndex + 1, lastIndex, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(slotStack, stackToTransfer);
            }
            else if(index >= outputIndex)
            {
                final int ingredientIndex = getIndexForRecipe(slotStack);
                final int fuelIndex = 4;

                if(ingredientIndex > -1)
                {
                    if(!this.moveItemStackTo(slotStack, ingredientIndex, ingredientIndex + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(ForgeHooks.getBurnTime(slotStack, null) > 0)
                {
                    if(!this.moveItemStackTo(slotStack, fuelIndex, fuelIndex + 1, false)) return ItemStack.EMPTY;
                }
                else if (index < lastInventoryIndex)
                {
                    if (!this.moveItemStackTo(slotStack, lastInventoryIndex, lastIndex, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < lastIndex && !this.moveItemStackTo(slotStack, outputIndex + 1, lastInventoryIndex, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(slotStack, outputIndex + 1, lastIndex, false))
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
    protected int getIndexForRecipe(ItemStack stack)
    {
        return getIndexForRecipe(stack, MineriaRecipeSerializers.DISTILLER_TYPE, DistillerRecipe.class);
    }
}
