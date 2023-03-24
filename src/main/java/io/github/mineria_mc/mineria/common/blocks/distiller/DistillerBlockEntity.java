package io.github.mineria_mc.mineria.common.blocks.distiller;

import io.github.mineria_mc.mineria.common.containers.DistillerMenu;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import io.github.mineria_mc.mineria.common.recipe.DistillerRecipe;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import io.github.mineria_mc.mineria.util.MineriaLockableBlockEntity;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class DistillerBlockEntity extends MineriaLockableBlockEntity {
    public int burnTime;
    public int currentBurnTime;
    public int distillationTime;
    public final int totalDistillationTime = 200;

    public DistillerBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntities.DISTILLER.get(), pos, state, new MineriaItemStackHandler(6));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.distiller");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInv) {
        return new DistillerMenu(id, playerInv, this);
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DistillerBlockEntity tile) {
        boolean distilling = tile.isBurning();
        boolean changed = false;

        if (tile.isBurning()) {
            --tile.burnTime;
        }

        if (!level.isClientSide) {
            ItemStack fuel = tile.inventory.getStackInSlot(4);

            if (tile.isBurning() || !fuel.isEmpty() && !tile.inventory.getStackInSlot(0).isEmpty()) {
                DistillerRecipe recipe = tile.findRecipe();

                if (!tile.isBurning() && tile.canDistill(recipe)) {
                    tile.burnTime = ForgeHooks.getBurnTime(fuel, null);
                    tile.currentBurnTime = tile.burnTime;

                    if (tile.isBurning()) {
                        changed = true;

                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack item1 = item.getCraftingRemainingItem(fuel);
                                tile.inventory.setStackInSlot(4, item1);
                            }
                        }
                    }
                }

                if (tile.isBurning() && tile.canDistill(recipe)) {
                    ++tile.distillationTime;

                    if (tile.distillationTime == tile.totalDistillationTime) {
                        tile.distillationTime = 0;
                        tile.distillItem(recipe);
                        changed = true;
                    }
                } else {
                    tile.distillationTime = Mth.clamp(tile.distillationTime - 2, 0, tile.totalDistillationTime);
                }
            } else if (!tile.isBurning() && tile.distillationTime > 0) {
                tile.distillationTime = Mth.clamp(tile.distillationTime - 2, 0, tile.totalDistillationTime);
            }

            if (distilling != tile.isBurning()) {
                changed = true;
            }
        }

        if (changed) {
            tile.setChanged();
        }
    }

    private boolean canDistill(@Nullable DistillerRecipe recipe) {
        if (recipe != null && recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
            ItemStack output = this.inventory.getStackInSlot(5);
            ItemStack result = recipe.getResultItem();

            if (result.isEmpty()) {
                return false;
            }

            if (output.isEmpty() || output.sameItem(result)) {
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void distillItem(DistillerRecipe recipe) {
        if (canDistill(recipe)) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack output = this.inventory.getStackInSlot(5);
            ItemStack result = recipe.getResultItem();

            if (output.isEmpty()) {
                this.inventory.setStackInSlot(5, result.copy());
            }
            else if (output.sameItem(result)) {
                output.grow(result.getCount());
            }

            input.shrink(1);
            shrinkIfPresent(ingredients, 1);
            shrinkIfPresent(ingredients, 2);
            shrinkIfPresent(ingredients, 3);
        }
    }

    private void shrinkIfPresent(NonNullList<Ingredient> ingredients, int index) {
        if (!ingredients.get(index).isEmpty()) {
            ItemStack stack = inventory.getStackInSlot(index);
            if(stack.getCraftingRemainingItem().isEmpty()) {
                stack.shrink(1);
            } else {
                inventory.setStackInSlot(index, stack.getCraftingRemainingItem());
            }
        }
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.burnTime = nbt.getInt("BurnTime");
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.distillationTime = nbt.getInt("DistillationTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CurrentBurnTime", this.currentBurnTime);
        compound.putInt("DistillationTime", this.distillationTime);
    }

    @Nullable
    private DistillerRecipe findRecipe() {
        Set<DistillerRecipe> recipes = MineriaUtils.findRecipesByType(MineriaRecipeTypes.DISTILLER.get(), this.level);
        for (DistillerRecipe recipe : recipes) {
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                return recipe;
            }
        }
        return null;
    }
}
