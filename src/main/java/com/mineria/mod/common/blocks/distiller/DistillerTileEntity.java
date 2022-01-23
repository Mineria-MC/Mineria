package com.mineria.mod.common.blocks.distiller;

import com.mineria.mod.common.containers.DistillerContainer;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.common.recipe.DistillerRecipe;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Set;

public class DistillerTileEntity extends MineriaLockableTileEntity implements ITickableTileEntity
{
    public int burnTime;
    public int currentBurnTime;
    public int distillationTime;
    public final int totalDistillationTime = 200;

    public DistillerTileEntity()
    {
        super(MineriaTileEntities.DISTILLER.get(), new CustomItemStackHandler(6));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.distiller");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInv)
    {
        return new DistillerContainer(id, playerInv, this);
    }

    public boolean isBurning()
    {
        return burnTime > 0;
    }

    @Override
    public void tick()
    {
        boolean distilling = this.isBurning();
        boolean changed = false;

        if(this.isBurning())
        {
            --this.burnTime;
        }

        if (!this.level.isClientSide)
        {
            ItemStack fuel = this.inventory.getStackInSlot(4);

            if (this.isBurning() || !fuel.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty())
            {
                DistillerRecipe recipe = findRecipe();

                if (!this.isBurning() && this.canDistill(recipe))
                {
                    this.burnTime = ForgeHooks.getBurnTime(fuel, null);
                    this.currentBurnTime = this.burnTime;

                    if (this.isBurning())
                    {
                        changed = true;

                        if (!fuel.isEmpty())
                        {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(fuel);
                                this.inventory.setStackInSlot(4, item1);
                            }
                        }
                    }
                }

                if(this.isBurning() && this.canDistill(recipe))
                {
                    ++this.distillationTime;

                    if (this.distillationTime == this.totalDistillationTime)
                    {
                        this.distillationTime = 0;
                        this.distillItem(recipe);
                        changed = true;
                    }
                }
                else
                {
                    this.distillationTime = MathHelper.clamp(this.distillationTime - 2, 0, this.totalDistillationTime);
                }
            }
            else if (!this.isBurning() && this.distillationTime > 0)
            {
                this.distillationTime = MathHelper.clamp(this.distillationTime - 2, 0, this.totalDistillationTime);
            }

            if (distilling != this.isBurning())
            {
                changed = true;
            }
        }

        if (changed)
        {
            this.setChanged();
        }
    }

    private boolean canDistill(@Nullable DistillerRecipe recipe)
    {
        if(recipe != null && recipe.matches(this.inventory))
        {
            ItemStack output = this.inventory.getStackInSlot(5);
            ItemStack result = recipe.getResultItem();

            if(result.isEmpty())
                return false;

            if(output.isEmpty() || output.sameItem(result))
            {
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void distillItem(DistillerRecipe recipe)
    {
        if(canDistill(recipe))
        {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack output = this.inventory.getStackInSlot(5);
            ItemStack result = recipe.getResultItem();

            if(output.isEmpty())
                this.inventory.setStackInSlot(5, result.copy());
            else if(output.sameItem(result))
                output.grow(result.getCount());

            input.shrink(1);
            shrinkIfPresent(ingredients, 1);
            shrinkIfPresent(ingredients, 2);
            shrinkIfPresent(ingredients, 3);
        }
    }

    private void shrinkIfPresent(NonNullList<Ingredient> ingredients, int index)
    {
        if(!ingredients.get(index).isEmpty()) this.inventory.getStackInSlot(index).shrink(1);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.distillationTime = nbt.getInt("DistillationTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        super.save(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CurrentBurnTime", this.currentBurnTime);
        compound.putInt("DistillationTime", this.distillationTime);
        return compound;
    }

    @Nullable
    private DistillerRecipe findRecipe()
    {
        Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.DISTILLER_TYPE, this.level);
        if(recipes != null)
            for(IRecipe<?> recipe : recipes)
                if(recipe instanceof DistillerRecipe)
                    if(((DistillerRecipe)recipe).matches(this.inventory))
                        return (DistillerRecipe)recipe;
        return null;
    }
}
