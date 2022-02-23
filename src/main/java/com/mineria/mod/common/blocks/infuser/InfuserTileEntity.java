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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Set;

public class InfuserTileEntity extends MineriaLockableTileEntity implements RecipeHolder
{
    public int burnTime;
    public int currentBurnTime;
    public int infuseTime;
    public final int totalInfuseTime = 2400;

    public InfuserTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.INFUSER.get(), pos, state, new CustomItemStackHandler(4));
    }

    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("tile_entity.mineria.infuser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player)
    {
        return new InfuserContainer(id, player, this);
    }

    private boolean isBurning()
    {
        return burnTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InfuserTileEntity tile)
    {
        boolean infusing = tile.isBurning();
        boolean changed = false;

        if (tile.isBurning())
        {
            --tile.burnTime;
        }

        if (!level.isClientSide)
        {
            ItemStack fuel = tile.inventory.getStackInSlot(2);

            if (tile.isBurning() || !fuel.isEmpty() && !tile.inventory.getStackInSlot(0).isEmpty())
            {
                InfuserRecipe recipe = tile.findRecipe();

                if (!tile.isBurning() && tile.canInfuse(recipe))
                {
                    tile.burnTime = ForgeHooks.getBurnTime(fuel, null);
                    tile.currentBurnTime = tile.burnTime;

                    if (tile.isBurning())
                    {
                        changed = true;

                        if (!fuel.isEmpty())
                        {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(fuel);
                                tile.inventory.setStackInSlot(2, item1);
                            }
                        }
                    }
                }

                if(tile.isBurning() && tile.canInfuse(recipe))
                {
                    ++tile.infuseTime;

                    if (tile.infuseTime == tile.totalInfuseTime)
                    {
                        tile.infuseTime = 0;
                        tile.infuseItem(recipe);
                        changed = true;
                    }
                }
                else
                {
                    tile.infuseTime = Mth.clamp(tile.infuseTime - 2, 0, tile.totalInfuseTime);
                }
            }
            else if (!tile.isBurning() && tile.infuseTime > 0)
            {
                tile.infuseTime = Mth.clamp(tile.infuseTime - 2, 0, tile.totalInfuseTime);
            }

            if (infusing != tile.isBurning())
            {
                changed = true;
                level.setBlock(pos, state.setValue(InfuserBlock.LIT, tile.isBurning()), 3);
            }
        }

        if (changed)
        {
            tile.setChanged();
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
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.burnTime = nbt.getInt("BurnTime");
        this.infuseTime = nbt.getInt("InfuseTime");
    }

    @Override
    public CompoundTag save(CompoundTag compound)
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
        Set<Recipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.INFUSER_TYPE, this.level);
        if(recipes != null)
            for(Recipe<?> recipe : recipes)
                if(recipe instanceof InfuserRecipe)
                    if(((InfuserRecipe)recipe).matches(new RecipeWrapper(this.inventory), this.level))
                        return (InfuserRecipe)recipe;
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe)
    {

    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed()
    {
        return null;
    }
}
