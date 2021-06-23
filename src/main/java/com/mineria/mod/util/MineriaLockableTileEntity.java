package com.mineria.mod.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public abstract class MineriaLockableTileEntity extends LockableTileEntity
{
    protected final CustomItemStackHandler inventory;

    protected MineriaLockableTileEntity(TileEntityType<?> typeIn, CustomItemStackHandler inventory)
    {
        super(typeIn);
        this.inventory = inventory;
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty()
    {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return this.inventory.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        this.inventory.removeStackFromSlot(index);
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventory.setStackInSlot(index, stack);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear()
    {
        this.inventory.clear();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        if(nbt.contains("Items")) deserializeOld(nbt, this.inventory); // Used if the NBT data is not up to date.
        else this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.pos, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        this.read(state, tag);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }

    @Deprecated
    private static void deserializeOld(CompoundNBT nbt, CustomItemStackHandler inventory)
    {
        NonNullList<ItemStack> inv = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, inv);
        inventory.setNonNullList(inv);
    }
}
