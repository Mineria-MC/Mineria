package io.github.mineria_mc.mineria.common.blocks.infuser;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.InfuserMenu;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.common.recipe.InfuserRecipe;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import io.github.mineria_mc.mineria.util.MineriaLockableTileEntity;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class InfuserBlockEntity extends MineriaLockableTileEntity {
    public int burnTime;
    public int currentBurnTime;
    public int infuseTime;
    public final int totalInfuseTime = 2400;

    public InfuserBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.INFUSER.get(), pos, state, new MineriaItemStackHandler(4));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.infuser");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new InfuserMenu(id, player, this);
    }

    private boolean isBurning() {
        return burnTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InfuserBlockEntity tile) {
        boolean infusing = tile.isBurning();
        boolean changed = false;

        if (tile.isBurning()) {
            --tile.burnTime;
        }

        if (!level.isClientSide) {
            ItemStack fuel = tile.inventory.getStackInSlot(2);

            if (tile.isBurning() || !fuel.isEmpty() && !tile.inventory.getStackInSlot(0).isEmpty()) {
                InfuserRecipe recipe = tile.findRecipe();

                if (!tile.isBurning() && tile.canInfuse(recipe)) {
                    tile.burnTime = ForgeHooks.getBurnTime(fuel, null);
                    tile.currentBurnTime = tile.burnTime;

                    if (tile.isBurning()) {
                        changed = true;

                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack item1 = item.getCraftingRemainingItem(fuel);
                                tile.inventory.setStackInSlot(2, item1);
                            }
                        }
                    }
                }

                if (tile.isBurning() && tile.canInfuse(recipe)) {
                    ++tile.infuseTime;

                    if (tile.infuseTime == tile.totalInfuseTime) {
                        tile.infuseTime = 0;
                        tile.infuseItem(recipe);
                        changed = true;
                    }
                } else {
                    tile.infuseTime = Mth.clamp(tile.infuseTime - 2, 0, tile.totalInfuseTime);
                }
            } else if (!tile.isBurning() && tile.infuseTime > 0) {
                tile.infuseTime = Mth.clamp(tile.infuseTime - 2, 0, tile.totalInfuseTime);
            }

            if (infusing != tile.isBurning()) {
                changed = true;
                level.setBlock(pos, state.setValue(InfuserBlock.LIT, tile.isBurning()), 3);
            }
        }

        if (changed) {
            tile.setChanged();
        }
    }

    public void infuseItem(InfuserRecipe recipe) {
        if (this.canInfuse(recipe)) {
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack secondaryInput = this.inventory.getStackInSlot(1);
            ItemStack result = recipe.getResultItem();
            ItemStack output = this.inventory.getStackInSlot(3);

            if (recipe.getContainer().test(output)) {
                this.inventory.setStackInSlot(3, result.copy());
            }

            input.shrink(1);
            recipe.getSecondaryInput()
                    .ifLeft(ingredient -> secondaryInput.shrink(1))
                    .ifRight(fluid -> inventory.setStackInSlot(1, AbstractWaterBarrelBlockEntity.decreaseFluidFromStack(secondaryInput)));
        }
    }

    private boolean canInfuse(@Nullable InfuserRecipe recipe) {
        ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack secondaryInput = this.inventory.getStackInSlot(1);
        ItemStack output = this.inventory.getStackInSlot(3);

        if (recipe == null || input.isEmpty() || !recipe.secondaryInputTest().test(secondaryInput) || output.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.getResultItem();

        if (result.isEmpty() || output.getCount() != 1) {
            return false;
        }

        return recipe.getContainer().test(output);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.currentBurnTime = nbt.getInt("CurrentBurnTime");
        this.burnTime = nbt.getInt("BurnTime");
        this.infuseTime = nbt.getInt("InfuseTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("CurrentBurnTime", this.currentBurnTime);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("InfuseTime", this.infuseTime);
    }

    @Nullable
    private InfuserRecipe findRecipe() {
        if(level == null) {
            return null;
        }

        Set<InfuserRecipe> recipes = MineriaUtils.findRecipesByType(MineriaRecipeTypes.INFUSER.get(), this.level);
        for (InfuserRecipe recipe : recipes) {
            if (recipe.matches(new RecipeWrapper(this.inventory), this.level)) {
                return recipe;
            }
        }
        return null;
    }
}
