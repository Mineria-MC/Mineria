package com.mineria.mod.common.blocks.distiller;

import com.mineria.mod.common.containers.DistillerContainer;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.common.recipe.DistillerRecipe;
import com.mineria.mod.util.CustomItemStackHandler;
import com.mineria.mod.util.MineriaLockableTileEntity;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Set;

public class DistillerTileEntity extends MineriaLockableTileEntity
{
    public int burnTime;
    public int currentBurnTime;
    public int distillationTime;
    public final int totalDistillationTime = 200;

    public DistillerTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.DISTILLER.get(), pos, state, new CustomItemStackHandler(6));
    }

    @Override
    protected Component getDefaultName()
    {
        return new TranslatableComponent("tile_entity.mineria.distiller");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv)
    {
        return new DistillerContainer(id, playerInv, this);
    }

    public boolean isBurning()
    {
        return burnTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DistillerTileEntity tile)
    {
        boolean distilling = tile.isBurning();
        boolean changed = false;

        if(tile.isBurning())
        {
            --tile.burnTime;
        }

        if (!level.isClientSide)
        {
            ItemStack fuel = tile.inventory.getStackInSlot(4);

            if (tile.isBurning() || !fuel.isEmpty() && !tile.inventory.getStackInSlot(0).isEmpty())
            {
                DistillerRecipe recipe = tile.findRecipe();

                if (!tile.isBurning() && tile.canDistill(recipe))
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
                                tile.inventory.setStackInSlot(4, item1);
                            }
                        }
                    }
                }

                if(tile.isBurning() && tile.canDistill(recipe))
                {
                    ++tile.distillationTime;

                    if (tile.distillationTime == tile.totalDistillationTime)
                    {
                        tile.distillationTime = 0;
                        tile.distillItem(recipe);
                        changed = true;
                    }
                }
                else
                {
                    tile.distillationTime = Mth.clamp(tile.distillationTime - 2, 0, tile.totalDistillationTime);
                }
            }
            else if (!tile.isBurning() && tile.distillationTime > 0)
            {
                tile.distillationTime = Mth.clamp(tile.distillationTime - 2, 0, tile.totalDistillationTime);
            }

            if (distilling != tile.isBurning())
            {
                changed = true;
            }
        }

        if (changed)
        {
            tile.setChanged();
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
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.distillationTime = nbt.getInt("DistillationTime");
    }

    @Override
    public CompoundTag save(CompoundTag compound)
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
        Set<Recipe<?>> recipes = MineriaUtils.findRecipesByType(MineriaRecipeSerializers.DISTILLER_TYPE, this.level);
        if(recipes != null)
            for(Recipe<?> recipe : recipes)
                if(recipe instanceof DistillerRecipe)
                    if(((DistillerRecipe)recipe).matches(this.inventory))
                        return (DistillerRecipe)recipe;
        return null;
    }
}
