package io.github.mineria_mc.mineria.common.blocks.apothecary_table;

import io.github.mineria_mc.mineria.common.containers.ApothecaryTableMenu;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.common.init.MineriaBlockEntities;
import io.github.mineria_mc.mineria.common.recipe.AbstractApothecaryTableRecipe;
import io.github.mineria_mc.mineria.common.recipe.ApothecaryFillingRecipe;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import io.github.mineria_mc.mineria.util.MineriaLockableBlockEntity;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class ApothecaryTableBlockEntity extends MineriaLockableBlockEntity {
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            return switch (index) {
                case 0 -> applicationTime;
                case 1 -> getLiquidAmount();
                case 2 -> getPoisonSourceOrdinal();
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> applicationTime = value;
                case 1 -> setLiquidAmount(value);
                case 2 -> setPoisonSourceFromOrdinal(value);
            }

        }

        public int getCount() {
            return 3;
        }
    };

    @Nullable
    private PoisonSource poisonSource = null;
    private int liquidAmount;
    public int applicationTime;
    public final int totalApplicationTime = 80;

    public ApothecaryTableBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntities.APOTHECARY_TABLE.get(), pos, state, new MineriaItemStackHandler(3));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.apothecary_table");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory playerInv) {
        return new ApothecaryTableMenu(windowId, playerInv, this, dataAccess);
    }

    private boolean isApplying() {
        return applicationTime > 0;
    }

    public void serverTick(Level level) {
        boolean alreadyExtracting = isApplying();
        boolean changed = false;

        if (!level.isClientSide) {
            ApothecaryFillingRecipe fillingRecipe = findFillingRecipe();
            if (fillingRecipe != null && this.liquidAmount < 5) {
                storePoison(fillingRecipe);
                changed = true;
            }

            AbstractApothecaryTableRecipe recipe = findRecipe();

            if (canApply(recipe)) {
                ++this.applicationTime;

                if (this.applicationTime == this.totalApplicationTime) {
                    this.applicationTime = 0;
                    applyPoison(recipe);
                    changed = true;
                }
            }

            if (!canApply(recipe) && this.applicationTime > 0) {
                this.applicationTime = Mth.clamp(this.applicationTime - 2, 0, this.totalApplicationTime);
            }

            if (alreadyExtracting != isApplying()) {
                changed = true;
            }
        }

        if (changed) {
            setChanged();
        }
    }

    private void storePoison(ApothecaryFillingRecipe recipe) {
        ItemStack stack = this.inventory.getStackInSlot(0);
        PoisonSource outputPoison = recipe.getOutputPoison();
        this.inventory.setStackInSlot(0, stack.getCraftingRemainingItem());
        if (this.poisonSource == null) {
            this.poisonSource = outputPoison;
        }
        this.liquidAmount = Math.min(liquidAmount + 1, 5);
    }

    private boolean canRemovePoison() {
        return this.poisonSource != null && this.liquidAmount > 0;
    }

    private void removePoison() {
        this.liquidAmount = Math.max(liquidAmount - 1, 0);
        if (liquidAmount == 0) poisonSource = null;
    }

    private boolean canApply(AbstractApothecaryTableRecipe recipe) {
        if (recipe == null || level == null)
            return false;

        if (canRemovePoison() && recipe.matches(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource), this.level)) {
            ItemStack output = this.inventory.getStackInSlot(2);
            ItemStack result = recipe.assemble(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource));

            if (result.isEmpty())
                return false;

            if (output.isEmpty() || output.sameItem(result) && ItemStack.tagMatches(output, result)) {
                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void applyPoison(AbstractApothecaryTableRecipe recipe) {
        if (recipe == null)
            return;

        if (canApply(recipe)) {
            ItemStack input = this.inventory.getStackInSlot(1);
            ItemStack output = this.inventory.getStackInSlot(2);
            ItemStack result = recipe.assemble(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource));

            if (output.isEmpty())
                this.inventory.setStackInSlot(2, result);
            else if (output.sameItem(result) && ItemStack.tagMatches(output, result))
                output.grow(result.getCount());

            input.shrink(1);
            removePoison();
        }
    }

    public int getPoisonSourceOrdinal() {
        return poisonSource == null ? -1 : poisonSource.ordinal();
    }

    public void setPoisonSourceFromOrdinal(int ordinal) {
        this.poisonSource = PoisonSource.byOrdinal(ordinal);
    }

    public int getLiquidAmount() {
        return liquidAmount;
    }

    public void setLiquidAmount(int liquidAmount) {
        this.liquidAmount = Math.min(5, liquidAmount);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.poisonSource = nbt.contains("PoisonSource") ? PoisonSource.byName(ResourceLocation.tryParse(nbt.getString("PoisonSource"))) : null;
        this.liquidAmount = nbt.getInt("Amount");
        this.applicationTime = nbt.getInt("ApplicationTime");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        if (poisonSource != null) compound.putString("PoisonSource", poisonSource.getId().toString());
        compound.putInt("Amount", liquidAmount);
        compound.putInt("ApplicationTime", applicationTime);
    }

    @Nullable
    private AbstractApothecaryTableRecipe findRecipe() {
        if(level == null) {
            return null;
        }

        Set<AbstractApothecaryTableRecipe> recipes = MineriaUtils.findRecipesByType(MineriaRecipeTypes.APOTHECARY_TABLE.get(), this.level);
        for (AbstractApothecaryTableRecipe recipe : recipes) {
            if (recipe.matches(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource), this.level)) {
                return recipe;
            }
        }
        return null;
    }
    
    @Nullable
    private ApothecaryFillingRecipe findFillingRecipe() {
        if(level == null) {
            return null;
        }

        for (ApothecaryFillingRecipe recipe : MineriaUtils.findRecipesByType(MineriaRecipeTypes.APOTHECARY_TABLE_FILLING.get(), this.level)) {
            if(recipe.matches(new ApothecaryTableInventoryWrapper(this.inventory, this.poisonSource), this.level)) {
                return recipe;
            }
        }
        return null;
    }
}
