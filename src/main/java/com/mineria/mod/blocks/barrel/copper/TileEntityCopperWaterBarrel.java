package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityCopperWaterBarrel extends AbstractTileEntityWaterBarrel
{
    private final CustomItemStackHandler inventory = new CustomItemStackHandler(8);

    public TileEntityCopperWaterBarrel()
    {
        super(16);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("container.copper_water_barrel");
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
}
