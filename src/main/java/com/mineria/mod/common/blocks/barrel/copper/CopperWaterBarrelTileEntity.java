package com.mineria.mod.common.blocks.barrel.copper;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.containers.CopperWaterBarrelContainer;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CopperWaterBarrelTileEntity extends AbstractWaterBarrelTileEntity implements MenuProvider
{
    private final CustomItemStackHandler inventory;

    public CopperWaterBarrelTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.COPPER_WATER_BARREL.get(), pos, state, 16);
        this.inventory = new CustomItemStackHandler(8);
    }

    @Override
    public Component getDisplayName()
    {
        return new TranslatableComponent("tile_entity.mineria.copper_water_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player)
    {
        return new CopperWaterBarrelContainer(id, playerInventory, this);
    }

    @Override
    public boolean shouldDrop()
    {
        return super.shouldDrop() || !this.inventory.isEmpty();
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
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return nbt;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        this.load(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        this.load(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        CompoundTag nbt = new CompoundTag();
        this.save(nbt);
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, nbt);
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
