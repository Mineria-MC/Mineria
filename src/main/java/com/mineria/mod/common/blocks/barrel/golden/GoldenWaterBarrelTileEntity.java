package com.mineria.mod.common.blocks.barrel.golden;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.containers.GoldenWaterBarrelContainer;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GoldenWaterBarrelTileEntity extends AbstractWaterBarrelTileEntity implements MenuProvider
{
    private final CustomItemStackHandler inventory;

    public GoldenWaterBarrelTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.GOLDEN_WATER_BARREL.get(), pos, state, 32);
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
    public Component getDisplayName()
    {
        return new TranslatableComponent("tile_entity.mineria.golden_water_barrel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player)
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
        if(level != null)
            this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(GoldenWaterBarrelBlock.POTIONS, this.getPotionsState()));
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
    public CompoundTag save(CompoundTag compound)
    {
        super.save(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putInt("Potions", this.getPotionsState()); // For ItemStack NBT
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this.inventory));
    }
}
