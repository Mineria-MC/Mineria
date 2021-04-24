package com.mineria.mod.blocks.barrel;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

public abstract class AbstractTileEntityWaterBarrel extends TileEntity
{
    protected int capacity;
    protected int buckets;
    private boolean destroyedByCreativePlayer;

    //We need a constructor with no parameters for the static method #create in TileEntity.
    public AbstractTileEntityWaterBarrel()
    {
    }

    public AbstractTileEntityWaterBarrel(int capacity)
    {
        this.capacity = capacity < 0 ? -1 : capacity;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.buckets = compound.hasKey("Water") ? compound.getInteger("Water") : compound.getInteger("Buckets");
        this.capacity = compound.hasKey("MaxWater") ? compound.getInteger("MaxWater") : compound.getInteger("Capacity");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Buckets", this.buckets);
        compound.setInteger("Capacity", this.capacity);
        return compound;
    }

    public boolean isEmpty()
    {
        return this.buckets == 0;
    }

    private boolean isFull()
    {
        return this.buckets == this.capacity;
    }

    private boolean isInfinite()
    {
        return this.buckets < 0;
    }

    public boolean isDestroyedByCreativePlayer()
    {
        return this.destroyedByCreativePlayer;
    }

    public void setDestroyedByCreativePlayer(boolean isCreative)
    {
        this.destroyedByCreativePlayer = isCreative;
    }

    public boolean shouldDrop()
    {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() && !this.isInfinite();
    }

    public boolean increaseFluidBuckets()
    {
        if(!isFull())
        {
            if(!isInfinite())
                ++this.buckets;
            return true;
        }
        return false;
    }

    public boolean decreaseFluidBuckets()
    {
        if(!isEmpty())
        {
            if(!isInfinite())
                --buckets;
            return true;
        }
        return false;
    }

    public int getBuckets()
    {
        return this.buckets;
    }

    public static boolean checkFluidFromStack(ItemStack barrel)
    {
        NBTTagCompound stackTag = barrel.getTagCompound();
        if(stackTag == null || !stackTag.hasKey("BlockEntityTag", 10))
            return false;
        NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");
        if(!blockEntityTag.hasKey("Buckets"))
            return false;
        return blockEntityTag.getInteger("Buckets") != 0;
    }

    public static boolean checkWaterFromStack(ItemStack barrel)
    {
        if(checkFluidFromStack(barrel))
        {
            if(barrel.getItem().getRegistryName().getResourcePath().equals("iron_barrel"))
            {
                NBTTagCompound stackTag = barrel.getTagCompound();
                NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");
                return blockEntityTag.hasKey("StoredFluid") && blockEntityTag.getString("StoredFluid").equals(FluidRegistry.WATER.getName());
            }
            return true;
        }
        return false;
    }

    public static void decreaseFluidFromStack(ItemStack barrel)
    {
        NBTTagCompound stackTag = barrel.getTagCompound();
        NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");
        int fluidBuckets = blockEntityTag.getInteger("Buckets");
        blockEntityTag.setInteger("Buckets", fluidBuckets < 0 ? fluidBuckets : --fluidBuckets);
    }


}
