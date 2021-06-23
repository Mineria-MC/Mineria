package com.mineria.mod.blocks.infuser;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelBlock;
import com.mineria.mod.blocks.infuser.slots.InfuserFuelSlot;
import com.mineria.mod.blocks.infuser.slots.InfuserOutputSlot;
import com.mineria.mod.init.ContainerTypeInit;
import com.mineria.mod.init.RecipeSerializerInit;
import com.mineria.mod.recipe.InfuserRecipe;
import com.mineria.mod.util.FunctionalIntReferenceHolder;
import com.mineria.mod.util.MineriaContainer;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.Set;

public class InfuserContainer extends MineriaContainer<InfuserTileEntity>
{
    private final FunctionalIntReferenceHolder infuseTime;
    private final FunctionalIntReferenceHolder burnTime;
    private final FunctionalIntReferenceHolder currentBurnTime;

    public InfuserContainer(int id, PlayerInventory playerInv, InfuserTileEntity tileEntity)
    {
        super(ContainerTypeInit.INFUSER.get(), id, tileEntity);

        this.createPlayerInventorySlots(playerInv, 8, 84);

        this.trackInt(infuseTime = new FunctionalIntReferenceHolder(() -> tileEntity.infuseTime, value -> tileEntity.infuseTime = value));
        this.trackInt(burnTime = new FunctionalIntReferenceHolder(() -> tileEntity.burnTime, value -> tileEntity.burnTime = value));
        this.trackInt(currentBurnTime = new FunctionalIntReferenceHolder(() -> tileEntity.currentBurnTime, value -> tileEntity.currentBurnTime = value));
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
        this.addSlot(new InfuserFuelSlot(tile.getInventory(), 2, 130, 35));
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
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index == 2)
            {
                if(!this.mergeItemStack(stack1, 3, 39, true)) return ItemStack.EMPTY;
                slot.onSlotChange(stack1, stack);
            }
            else if(index != 1 && index != 0)
            {
                if(hasRecipe(stack1))
                {
                    if(!this.mergeItemStack(stack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(stack1.getItem() instanceof AbstractWaterBarrelBlock.WaterBarrelBlockItem)
                {
                    if(!this.mergeItemStack(stack1, 1, 2, false)) return ItemStack.EMPTY;
                }
                else if(ForgeHooks.getBurnTime(stack1) > 0)
                {
                    if(!this.mergeItemStack(stack1, 2, 3, false)) return ItemStack.EMPTY;
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(stack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(stack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(stack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
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
        @Nullable Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(RecipeSerializerInit.INFUSER_TYPE, this.tile.getWorld());
        if(recipes != null)
        {
            return recipes.stream().anyMatch(recipe -> ((InfuserRecipe)recipe).getInput().test(stack));
        }

        return false;
    }
}
