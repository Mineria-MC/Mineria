package io.github.mineria_mc.mineria.common.blocks.titane_extractor;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.TitaneExtractorMenu;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import io.github.mineria_mc.mineria.util.MineriaLockableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;

public class TitaneExtractorBlockEntity extends MineriaLockableTileEntity {
    public int extractTime;
    public int totalExtractTime = 200;

    public TitaneExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.TITANE_EXTRACTOR.get(), pos, state, new MineriaItemStackHandler(4));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.titane_extractor");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory player) {
        return new TitaneExtractorMenu(id, player, this);
    }

    public boolean isExtracting() {
        return this.extractTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TitaneExtractorBlockEntity tile) {
        boolean alreadyExtracting = tile.isExtracting();
        boolean dirty = false;

        if (!level.isClientSide) {
            if (tile.canExtract()) {
                ++tile.extractTime;

                if (tile.extractTime == tile.totalExtractTime) {
                    tile.extractTime = 0;
                    tile.extractItem();
                    dirty = true;
                }
            }
            if (!tile.canExtract() && tile.extractTime > 0) {
                tile.extractTime = Mth.clamp(tile.extractTime - 2, 0, tile.totalExtractTime);
            }

            if (alreadyExtracting != tile.isExtracting() && alreadyExtracting != tile.canExtract()) {
                dirty = true;
                level.setBlockAndUpdate(pos, state.setValue(TitaneExtractorBlock.LIT, tile.isExtracting()));
            }
        }

        if (dirty) {
            tile.setChanged();
        }
    }

    private boolean canExtract() {
        ItemStack input0 = this.inventory.getStackInSlot(0);
        ItemStack input1 = this.inventory.getStackInSlot(1);
        ItemStack input2 = this.inventory.getStackInSlot(2);
        boolean hasWater = AbstractWaterBarrelBlockEntity.checkFluidFromStack(input1, Fluids.WATER);

        if (!input0.isEmpty() && !input1.isEmpty() && !input2.isEmpty() && hasWater) {
            if (input0.sameItem(new ItemStack(MineriaBlocks.MINERAL_SAND.get())) && input2.sameItem(new ItemStack(MineriaItems.FILTER.get()))) {
                ItemStack output = this.inventory.getStackInSlot(3);
                ItemStack result = new ItemStack(MineriaItems.TITANE_NUGGET.get(), 1);

                if (!output.sameItem(result) && !output.isEmpty()) {
                    return false;
                }

                int res = output.getCount() + result.getCount();
                return res <= 64 && res <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private void extractItem() {
        if (this.canExtract()) {
            ItemStack input0 = this.inventory.getStackInSlot(0);
            ItemStack input1 = this.inventory.getStackInSlot(1);
            ItemStack input2 = this.inventory.getStackInSlot(2);
            ItemStack result = new ItemStack(MineriaItems.TITANE_NUGGET.get(), 1);
            ItemStack output = this.inventory.getStackInSlot(3);

            if (output.isEmpty()) {
                this.inventory.setStackInSlot(3, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }

            input0.shrink(1);
            this.inventory.setStackInSlot(1, AbstractWaterBarrelBlockEntity.decreaseFluidFromStack(input1));
            input2.shrink(1);
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ExtractTime", this.extractTime);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.extractTime = nbt.getInt("ExtractTime");
    }
}
