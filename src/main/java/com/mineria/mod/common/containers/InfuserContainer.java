package com.mineria.mod.common.containers;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.blocks.infuser.InfuserTileEntity;
import com.mineria.mod.common.containers.slots.FuelSlot;
import com.mineria.mod.common.containers.slots.InfuserOutputSlot;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaContainerTypes;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.recipe.InfuserRecipe;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class InfuserContainer extends MineriaContainer<InfuserTileEntity>
{
    private final FunctionalIntReferenceHolder infuseTime;
    private final FunctionalIntReferenceHolder burnTime;
    private final FunctionalIntReferenceHolder currentBurnTime;

    public InfuserContainer(int id, PlayerInventory playerInv, InfuserTileEntity tileEntity)
    {
        super(MineriaContainerTypes.INFUSER.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.addDataSlot(infuseTime = new FunctionalIntReferenceHolder(() -> tileEntity.infuseTime, value -> tileEntity.infuseTime = value));
        this.addDataSlot(burnTime = new FunctionalIntReferenceHolder(() -> tileEntity.burnTime, value -> tileEntity.burnTime = value));
        this.addDataSlot(currentBurnTime = new FunctionalIntReferenceHolder(() -> tileEntity.currentBurnTime, value -> tileEntity.currentBurnTime = value));
    }

    public static InfuserContainer create(int id, PlayerInventory playerInv, PacketBuffer buffer)
    {
        return new InfuserContainer(id, playerInv, getTileEntity(InfuserTileEntity.class, playerInv, buffer));
    }

    @Override
    protected void createInventorySlots(InfuserTileEntity tile)
    {
        this.addSlot(new SlotItemHandler(tile.getInventory(), 0, 14, 10));
        this.addSlot(new SlotItemHandler(tile.getInventory(), 1, 47, 35));
        this.addSlot(new FuelSlot(tile.getInventory(), 2, 130, 35));
        this.addSlot(new InfuserOutputSlot(tile.getInventory(), 3, 91, 35));
    }

    @OnlyIn(Dist.CLIENT)
    public int getInfuseTimeScaled(int pixels)
    {
        return this.infuseTime.get() != 0 && this.tile.totalInfuseTime != 0 ? this.infuseTime.get() * pixels / this.tile.totalInfuseTime : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnTimeScaled(int pixels)
    {
        int i = this.currentBurnTime.get();

        if (i == 0)
        {
            i = 2400;
        }

        return this.burnTime.get() * pixels / i;
    }

    public InfuserTileEntity getTileEntity()
    {
        return tile;
    }

    @Override
    protected StackTransferHandler getStackTransferHandler()
    {
        return new StackTransferHandler(3)
                .withFuel(2, null)
                .withSpecialInput(1, AbstractWaterBarrelTileEntity::checkWaterFromStack)
                .withSpecialInput(3, stack -> stack.getItem().equals(MineriaItems.CUP));
    }

    /*@Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot != null && slot.hasItem())
        {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();

            if(index == 2)
            {
                if(!this.moveItemStackTo(stack1, 3, 39, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(stack1, stack);
            }
            else if(index != 1 && index != 0)
            {
                if(hasRecipe(stack1))
                {
                    if(!this.moveItemStackTo(stack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(stack1.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem)
                {
                    if(!this.moveItemStackTo(stack1, 1, 2, false)) return ItemStack.EMPTY;
                }
                else if(ForgeHooks.getBurnTime(stack1) > 0)
                {
                    if(!this.moveItemStackTo(stack1, 2, 3, false)) return ItemStack.EMPTY;
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.moveItemStackTo(stack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.moveItemStackTo(stack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(stack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (stack1.getCount() == stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack1);
        }
        return stack;
    }

    private boolean hasRecipe(ItemStack stack)
    {
        @Nullable Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.INFUSER_TYPE, this.tile.getLevel());
        if(recipes != null)
        {
            return recipes.stream().anyMatch(recipe -> ((InfuserRecipe)recipe).getInput().test(stack));
        }

        return false;
    }*/

    @Override
    protected int getIndexForRecipe(ItemStack stack)
    {
        return findRecipeForStack(stack, MineriaRecipeSerializers.INFUSER_TYPE, InfuserRecipe.class) == null ? -1 : 0;
    }
}
