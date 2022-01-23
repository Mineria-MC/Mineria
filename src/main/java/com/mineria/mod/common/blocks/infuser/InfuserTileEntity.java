package com.mineria.mod.common.blocks.infuser;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.containers.InfuserContainer;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.common.recipe.InfuserRecipe;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Set;

public class InfuserTileEntity extends MineriaLockableTileEntity implements ITickableTileEntity, IRecipeHolder
{
    public int burnTime;
    public int currentBurnTime;
    public int infuseTime;
    public final int totalInfuseTime = 2400;

    public InfuserTileEntity()
    {
        super(MineriaTileEntities.INFUSER.get(), new CustomItemStackHandler(4));
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile_entity.mineria.infuser");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new InfuserContainer(id, player, this);
    }

    private boolean isBurning()
    {
        return burnTime > 0;
    }

    @Override
    public void tick()
    {
        boolean infusing = this.isBurning();
        boolean changed = false;

        if (this.isBurning())
        {
            --this.burnTime;
        }

        if (!this.level.isClientSide)
        {
            ItemStack fuel = this.inventory.getStackInSlot(2);

            if (this.isBurning() || !fuel.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty())
            {
                InfuserRecipe recipe = findRecipe();

                if (!this.isBurning() && this.canInfuse(recipe))
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
                                this.inventory.setStackInSlot(2, item1);
                            }
                        }
                    }
                }

                if(this.isBurning() && this.canInfuse(recipe))
                {
                    ++this.infuseTime;

                    if (this.infuseTime == this.totalInfuseTime)
                    {
                        this.infuseTime = 0;
                        this.infuseItem(recipe);
                        changed = true;
                    }
                }
                else
                {
                    this.infuseTime = MathHelper.clamp(this.infuseTime - 2, 0, this.totalInfuseTime);
                }
            }
            else if (!this.isBurning() && this.infuseTime > 0)
            {
                this.infuseTime = MathHelper.clamp(this.infuseTime - 2, 0, this.totalInfuseTime);
            }

            if (infusing != this.isBurning())
            {
                changed = true;
                level.setBlock(worldPosition, this.getBlockState().setValue(InfuserBlock.LIT, this.isBurning()), 3);
            }
        }

        if (changed)
        {
            this.setChanged();
        }
    }

    public void infuseItem(InfuserRecipe recipe)
    {
        if (this.canInfuse(recipe))
        {
            ItemStack input0 = this.inventory.getStackInSlot(0);
            ItemStack input1 = this.inventory.getStackInSlot(1);
            ItemStack result = recipe.getResultItem();
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.getItem() == MineriaItems.CUP)
            {
                output.shrink(1);
                this.inventory.setStackInSlot(3, result.copy());
            }

            input0.shrink(1);
            this.inventory.setStackInSlot(1, AbstractWaterBarrelTileEntity.decreaseFluidFromStack(input1));
        }
    }

    private boolean canInfuse(@Nullable InfuserRecipe recipe)
    {
        ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack output = this.inventory.getStackInSlot(3);
        boolean hasWater = AbstractWaterBarrelTileEntity.checkWaterFromStack(this.inventory.getStackInSlot(1));

        if (recipe == null || input.isEmpty() || output.isEmpty() || !hasWater)
        {
            return false;
        }
        else
        {
            ItemStack result = recipe.getResultItem();

            if (result.isEmpty())
            {
                return false;
            }
            else
            {
                if(output.getCount() != 1)
                {
                    return false;
                }

                return output.sameItem(new ItemStack(MineriaItems.CUP));
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.burnTime = nbt.getInt("BurnTime");
        this.infuseTime = nbt.getInt("InfuseTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        super.save(compound);
        compound.putInt("CurrentBurnTime", this.currentBurnTime);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("InfuseTime", this.infuseTime);
        return compound;
    }

    @Nullable
    private InfuserRecipe findRecipe()
    {
        Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.INFUSER_TYPE, this.level);
        if(recipes != null)
            for(IRecipe<?> recipe : recipes)
                if(recipe instanceof InfuserRecipe)
                    if(((InfuserRecipe)recipe).matches(new RecipeWrapper(this.inventory), this.level))
                        return (InfuserRecipe)recipe;
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe)
    {

    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed()
    {
        return null;
    }
}
