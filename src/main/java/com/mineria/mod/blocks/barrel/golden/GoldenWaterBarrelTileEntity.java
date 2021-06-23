package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoldenWaterBarrelTileEntity extends AbstractWaterBarrelTileEntity implements INamedContainerProvider
{
    private final CustomItemStackHandler inventory;

    public GoldenWaterBarrelTileEntity()
    {
        super(TileEntitiesInit.GOLDEN_WATER_BARREL.get(), 32);
        this.inventory = new CustomItemStackHandler(20) {
            @Override
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
                GoldenWaterBarrelTileEntity.this.reloadBlockState();
            }
        };
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("tile_entity.mineria.golden_water_barrel");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player)
    {
        return new GoldenWaterBarrelContainer(id, playerInv, this);
    }

    @Override
    public boolean shouldDrop()
    {
        return super.shouldDrop() || !this.inventory.isEmpty();
    }

    public void reloadBlockState()
    {
        if(world != null)
            this.world.setBlockState(this.pos, this.getBlockState().with(GoldenWaterBarrelBlock.POTIONS, this.getPotionsState()));
    }

    public int getFullPotionSlotsCount()
    {
        int result = 0;
        for(ItemStack stack : this.inventory.toNonNullList().subList(4, 19))
        {
            if(stack.getItem() instanceof PotionItem)
            {
                result++;
            }
        }
        return result;
    }

    private int getPotionsState()
    {
        int fullSlots = this.getFullPotionSlotsCount();
        int potions = 0;
        if(fullSlots > 0)
        {
            if(fullSlots < 5)
                potions = 1;
            else if(fullSlots < 9)
                potions = 2;
            else if(fullSlots < 13)
                potions = 3;
            else if(fullSlots < 17)
                potions = 4;
        }

        return potions;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putInt("Potions", this.getPotionsState()); // For ItemStack NBT
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
