package com.mineria.mod.blocks.barrel.golden;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityGoldenWaterBarrel extends AbstractTileEntityWaterBarrel
{
    private final CustomItemStackHandler inventory = new CustomItemStackHandler(20)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            TileEntityGoldenWaterBarrel.this.reloadBlockState();
        }
    };

    public TileEntityGoldenWaterBarrel()
    {
        super(32);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("container.golden_water_barrel");
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) inventory;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("Inventory", this.inventory.serializeNBT());
        compound.setInteger("Potions", getPotionsState());
        return compound;
    }

    public boolean isInventoryEmpty()
    {
        for(ItemStack stack : this.inventory.toNonNullList())
        {
            if(!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public boolean shouldDrop()
    {
        return super.shouldDrop() || !isInventoryEmpty();
    }

    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public CustomItemStackHandler getInventory()
    {
        return inventory;
    }

    public int getFullPotionSlotsCount()
    {
        int result = 0;
        for(ItemStack stack : this.inventory.toNonNullList().subList(4, 19))
        {
            if(stack.getItem() instanceof ItemPotion)
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

    public void reloadBlockState()
    {
        BlockGoldenWaterBarrel.setState(getPotionsState(), this.world, pos);
    }
}
