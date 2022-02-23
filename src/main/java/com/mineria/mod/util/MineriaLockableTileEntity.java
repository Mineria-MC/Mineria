package com.mineria.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Abstract class for Mineria tile entities.
 */
// TODO
public abstract class MineriaLockableTileEntity extends BaseContainerBlockEntity
{
    protected final CustomItemStackHandler inventory;

    protected MineriaLockableTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state, CustomItemStackHandler inventory)
    {
        super(typeIn, pos, state);
        this.inventory = inventory;
    }

    @Override
    public int getContainerSize()
    {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty()
    {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int index)
    {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count)
    {
        return this.inventory.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index)
    {
        this.inventory.removeStackFromSlot(index);
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack)
    {
        this.inventory.setStackInSlot(index, stack);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent()
    {
        this.inventory.clear();
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        super.save(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        if(nbt.contains("Items")) deserializeOld(nbt, this.inventory); // Used if the NBT data is not up to date.
        else this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, nbt);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        this.load(tag);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    /*public int getDataSlot(int index)
    {
        return 0;
    }

    public void setDataSlot(int index, int value)
    {
    }

    public int getDataSlotsCount()
    {
        return 0;
    }*/

    private static void deserializeOld(CompoundTag nbt, CustomItemStackHandler inventory)
    {
        NonNullList<ItemStack> inv = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inv);
        inventory.setNonNullList(inv);
    }
}
