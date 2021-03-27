package com.mineria.mod.blocks.infuser;

import com.mineria.mod.Mineria;
import com.mineria.mod.blocks.barrel.WaterBarrelTileEntity;
import com.mineria.mod.init.ItemsInit;
import com.mineria.mod.init.RecipeSerializerInit;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.recipe.InfuserRecipe;
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
        super(TileEntitiesInit.INFUSER.get(), new CustomItemStackHandler(4));
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

    private boolean isInfusing()
    {
        return burnTime > 0;
    }

    @Override
    public void tick()
    {
        boolean flag = this.isInfusing();
        boolean flag1 = false;

        if (this.isInfusing())
        {
            --this.burnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack fuel = this.inventory.getStackInSlot(2);

            if (this.isInfusing() || !fuel.isEmpty() && !this.inventory.getStackInSlot(0).isEmpty())
            {
                InfuserRecipe recipe = findRecipe();

                if(recipe == null)
                {
                    return;
                }

                if (!this.isInfusing() && this.canInfuse(recipe))
                {
                    this.burnTime = ForgeHooks.getBurnTime(fuel);
                    this.currentBurnTime = this.burnTime;

                    if (this.isInfusing())
                    {
                        flag1 = true;

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

                if(this.isInfusing() && this.canInfuse(recipe))
                {
                    ++this.infuseTime;

                    if (this.infuseTime == this.totalInfuseTime)
                    {
                        this.infuseTime = 0;
                        this.infuseItem(recipe);
                        flag1 = true;
                    }
                }
                else
                {
                    this.infuseTime = 0;
                }
            }
            else if (!this.isInfusing() && this.infuseTime > 0)
            {
                this.infuseTime = MathHelper.clamp(this.infuseTime - 2, 0, this.totalInfuseTime);
            }

            if (flag != this.isInfusing())
            {
                flag1 = true;
                Mineria.LOGGER.debug("aaa");
                world.setBlockState(pos, this.getBlockState().with(InfuserBlock.LIT, this.isInfusing()), 3);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public void infuseItem(InfuserRecipe recipe)
    {
        if (this.canInfuse(recipe))
        {
            ItemStack input0 = this.inventory.getStackInSlot(0);
            ItemStack input1 = this.inventory.getStackInSlot(1);
            ItemStack result = recipe.getRecipeOutput();
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.getItem() == ItemsInit.CUP)
            {
                output.shrink(1);
                this.inventory.setStackInSlot(3, result.copy());
            }

            input0.shrink(1);
            WaterBarrelTileEntity.decreaseWaterFromStack(input1);
        }
    }

    private boolean canInfuse(InfuserRecipe recipe)
    {
        ItemStack input0 = this.inventory.getStackInSlot(0);
        ItemStack input1 = this.inventory.getStackInSlot(1);
        ItemStack output = this.inventory.getStackInSlot(3);
        boolean hasWater = WaterBarrelTileEntity.checkWaterFromStack(input1);

        if (input0.isEmpty() || input1.isEmpty() || output.isEmpty() || !hasWater)
        {
            return false;
        }
        else
        {
            ItemStack result = recipe.getRecipeOutput();

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

                return output.isItemEqual(new ItemStack(ItemsInit.CUP));
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.burnTime = nbt.getInt("BurnTime");
        this.infuseTime = nbt.getInt("InfuseTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("CurrentBurnTime", this.currentBurnTime);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("InfuseTime", this.infuseTime);
        return compound;
    }

    @Nullable
    private InfuserRecipe findRecipe()
    {
        Set<IRecipe<?>> recipes = MineriaUtils.findRecipesByType(RecipeSerializerInit.INFUSER_TYPE, this.world);
        for(IRecipe<?> recipe : recipes)
            if(recipe instanceof InfuserRecipe)
                if(((InfuserRecipe)recipe).matches(new RecipeWrapper(this.inventory), this.world))
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
