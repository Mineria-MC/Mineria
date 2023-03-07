package io.github.mineria_mc.mineria.common.blocks.barrel.golden;

import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.containers.GoldenWaterBarrelMenu;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import io.github.mineria_mc.mineria.util.MineriaItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoldenWaterBarrelBlockEntity extends AbstractWaterBarrelBlockEntity implements MenuProvider {
    protected final MineriaItemStackHandler inventory;
    protected LazyOptional<IItemHandler> invCap;

    public GoldenWaterBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.GOLDEN_WATER_BARREL.get(), pos, state, 32);
        this.inventory = new MineriaItemStackHandler(20) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                GoldenWaterBarrelBlockEntity.this.reloadBlockState();
            }
        };
        this.invCap = LazyOptional.of(() -> this.inventory);
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return Component.translatable("tile_entity.mineria.golden_water_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInv, @Nonnull Player player) {
        return new GoldenWaterBarrelMenu(id, playerInv, this);
    }

    @Override
    public boolean shouldDrop() {
        return super.shouldDrop() || !this.inventory.isEmpty();
    }

    protected void reloadBlockState() {
        if (level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(GoldenWaterBarrelBlock.POTIONS, this.getPotionsState()));
        }
    }

    protected int getFullPotionSlotsCount() {
        int result = 0;
        for (ItemStack stack : this.inventory.toNonNullList().subList(4, 20)) {
            if (stack.getItem() instanceof PotionItem) {
                result++;
            }
        }
        return result;
    }

    protected int getPotionsState() {
        return Mth.ceil(getFullPotionSlotsCount() / 4f);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.serializeNBT());
        nbt.putInt("Potions", this.getPotionsState()); // For ItemStack NBT
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
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.invCap.cast();
        }
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
