package io.github.mineria_mc.mineria.common.blocks.extractor;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.ExtractorMenu;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.common.recipe.ExtractorRecipe;
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
import java.util.*;

public class ExtractorTileEntity extends MineriaLockableTileEntity {
    public int extractTime;
    public int totalExtractTime = 400;

    public ExtractorTileEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.EXTRACTOR.get(), pos, state, new MineriaItemStackHandler(10));
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.extractor");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInv) {
        return new ExtractorMenu(id, playerInv, this);
    }

    public boolean isExtracting() {
        return this.extractTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExtractorTileEntity tile) {
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
                level.setBlockAndUpdate(pos, state.setValue(ExtractorBlock.LIT, tile.isExtracting()));
            }
        }

        if (dirty) {
            tile.setChanged();
        }
    }

    private boolean canExtract() {
        ItemStack input = this.inventory.getStackInSlot(0);
        ItemStack waterSource = this.inventory.getStackInSlot(1);
        ItemStack filter = this.inventory.getStackInSlot(2);
        boolean hasWater = AbstractWaterBarrelBlockEntity.checkFluidFromStack(waterSource, Fluids.WATER);

        if (input.isEmpty() || waterSource.isEmpty() || filter.isEmpty() || !hasWater) {
            return false;
        } else {

            for (int index = 3; index < this.inventory.getSlots(); index++) {
                ItemStack output = this.inventory.getStackInSlot(index);
                ItemStack result = getOutputStackFromSlot(index, ExtractorRecipe.DEFAULT_RECIPE.get().getOutputsMap());

                if (output.isEmpty()) {
                    continue;
                } else if (!output.sameItem(result)) {
                    return false;
                }

                int res = output.getCount() + result.getCount();
                if (!(res <= 64 && res <= output.getMaxStackSize())) {
                    return false;
                }
            }
            return true;
        }
    }

    private void extractItem() {
        if (this.canExtract()) {
            ItemStack input = this.inventory.getStackInSlot(0);
            ItemStack waterSource = this.inventory.getStackInSlot(1);
            ItemStack filter = this.inventory.getStackInSlot(2);
            Map<Integer, ItemStack> outputs = ExtractorRecipe.DEFAULT_RECIPE.get().getOutputsMap();

            final int multiplier = 2;

            for (int i = 0; i < multiplier; i++) {
                for (Map.Entry<Integer, ItemStack> entry : outputs.entrySet()) {
                    Random rand = new Random();
                    int chance = rand.nextInt(1000);

                    if (chance <= entry.getKey()) {
                        ItemStack result = entry.getValue();
                        ItemStack output = this.inventory.getStackInSlot(getOutputIndexFromStack(result, outputs));

                        if (output.getCount() + result.getCount() > 64) {
                            continue;
                        }
                        if (output.isEmpty()) {
                            this.inventory.setStackInSlot(getOutputIndexFromStack(result, outputs), result.copy());
                        } else if (output.getItem() == result.getItem()) {
                            output.grow(result.getCount());
                        }
                    }
                }
            }

            input.shrink(1);
            this.inventory.setStackInSlot(1, AbstractWaterBarrelBlockEntity.decreaseFluidFromStack(waterSource));
            filter.shrink(1);
        }
    }

    private static ItemStack getOutputStackFromSlot(int slotId, Map<Integer, ItemStack> map) {
        List<Integer> ints = new ArrayList<>(map.keySet());
        ints.sort(Comparator.reverseOrder());
        return map.get(ints.get(slotId - 3));
    }

    private static int getOutputIndexFromStack(ItemStack stack, Map<Integer, ItemStack> map) {
        List<Integer> ints = new ArrayList<>(map.keySet());
        ints.sort(Comparator.reverseOrder());
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            if (entry.getValue().sameItem(stack)) {
                return ints.indexOf(entry.getKey()) + 3;
            }
        }
        return -1;
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ExtractTime", this.extractTime);
        compound.putInt("TotalExtractTime", this.totalExtractTime);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.extractTime = nbt.getInt("ExtractTime");
        this.totalExtractTime = nbt.getInt("TotalExtractTime");
    }
}
