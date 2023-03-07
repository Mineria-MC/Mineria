package io.github.mineria_mc.mineria.common.blocks.barrel.copper;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.CopperWaterBarrelMenu;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopperWaterBarrelBlockEntity extends AbstractWaterBarrelBlockEntity implements MenuProvider {
    private final MineriaItemStackHandler inventory;
    private LazyOptional<IItemHandler> invCap;

    public CopperWaterBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.COPPER_WATER_BARREL.get(), pos, state, 16);
        this.inventory = new MineriaItemStackHandler(8);
        this.invCap = LazyOptional.of(() -> inventory);
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("tile_entity.mineria.copper_water_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        return new CopperWaterBarrelMenu(id, playerInventory, this);
    }

    @Override
    public boolean shouldDrop() {
        return super.shouldDrop() || !this.inventory.isEmpty();
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public MineriaItemStackHandler getInventory() {
        return inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return this.invCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.invCap.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.invCap = LazyOptional.of(() -> this.inventory);
    }
}
