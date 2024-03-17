package io.github.mineria_mc.mineria.common.block.titane_extractor;

import io.github.mineria_mc.mineria.common.containers.TitaneExtractorMenu;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockEntityRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import io.github.mineria_mc.mineria.util.MineriaLockableBlockEntity;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TitaneExtractorBlockEntity extends MineriaLockableBlockEntity implements ExtendedScreenHandlerFactory {

    public int extractTime;
    public int totalExtractTime = 200;

    public TitaneExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaBlockEntityRegistry.TITANE_EXTRACTOR, pos, state, NonNullList.withSize(4, ItemStack.EMPTY));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tile_entity.mineria.titane_extractor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
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

    @SuppressWarnings("unused")
    private boolean canExtract() {
        ItemStack input0 = this.inventory.get(0);
        ItemStack input1 = this.inventory.get(1);
        ItemStack input2 = this.inventory.get(2);
        boolean hasWater = input1.getItem() == Items.WATER_BUCKET;

        if (!input0.isEmpty() && !input1.isEmpty() && !input2.isEmpty()) {
            if (ItemStack.isSameItem(input0, new ItemStack(MineriaBlockRegistry.MINERAL_SAND)) && ItemStack.isSameItem(input2, new ItemStack(MineriaItemRegistry.FILTER))) {
                ItemStack output = this.inventory.get(3);
                ItemStack result = new ItemStack(MineriaItemRegistry.TITANE_NUGGET, 1);

                if (!ItemStack.isSameItem(output, result) && !output.isEmpty()) {
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
            ItemStack input0 = this.inventory.get(0);
            ItemStack input1 = this.inventory.get(1);
            ItemStack input2 = this.inventory.get(2);
            ItemStack result = new ItemStack(MineriaItemRegistry.TITANE_NUGGET, 1);
            ItemStack output = this.inventory.get(3);

            if (output.isEmpty()) {
                this.inventory.set(3, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }

            input0.shrink(1);
            this.inventory.set(1, MineriaUtil.decreaseFluidFromStack(input1));
            input2.shrink(1);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("ExtractTime", this.extractTime);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.extractTime = nbt.getInt("ExtractTime");
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }
}
