package io.github.mineria_mc.mineria.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Abstract class for Mineria tile entities.
 */
public abstract class MineriaLockableBlockEntity extends BaseContainerBlockEntity implements HopperHandler {
    protected final MineriaItemStackHandler inventory;

    protected MineriaLockableBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state, MineriaItemStackHandler inventory) {
        super(typeIn, pos, state);
        this.inventory = inventory;
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getItem(int index) {
        return this.inventory.getStackInSlot(index);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int index, int count) {
        return this.inventory.extractItem(index, count, false);
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack previous = this.inventory.getStackInSlot(index);
        this.inventory.setStackInSlot(index, ItemStack.EMPTY);
        return previous;
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        this.inventory.setStackInSlot(index, stack);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        if(level == null) {
            return false;
        }
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("Items")) deserializeOld(nbt, this.inventory); // Used if the NBT data is not up-to-date.
        else this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    public MineriaItemStackHandler getInventory() {
        return inventory;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nonnull
    @Override
    protected IItemHandler createUnSidedHandler() {
        return this.inventory;
    }

    @Override
    public boolean canInsertHopperItem(int index, ItemStack stack) {
        return this.inventory.canInsertHopperItem(index, stack);
    }

    @Override
    public boolean canExtractHopperItem(int index, ItemStack stack) {
        return this.inventory.canExtractHopperItem(index, stack);
    }

    private static void deserializeOld(CompoundTag nbt, MineriaItemStackHandler inventory) {
        NonNullList<ItemStack> inv = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inv);
        inventory.setNonNullList(inv);
    }
}
