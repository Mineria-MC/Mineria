package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopperWaterBarrelTileEntity extends AbstractWaterBarrelTileEntity implements INamedContainerProvider
{
    private final CustomItemStackHandler inventory;

    public CopperWaterBarrelTileEntity()
    {
        super(TileEntitiesInit.COPPER_WATER_BARREL.get(), 16);
        this.inventory = new CustomItemStackHandler(8);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("tile_entity.mineria.copper_water_barrel");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player)
    {
        return new CopperWaterBarrelContainer(id, playerInventory, this);
    }

    @Override
    public boolean shouldDrop()
    {
        return super.shouldDrop() || !this.inventory.isEmpty();
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
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        this.read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.pos, 0, nbt);
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> inventory));
    }
}
