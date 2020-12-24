package com.mineria.mod.blocks.barrel;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBarrel extends TileEntity
{
    private final int maxWaterBuckets;
    private int waterBuckets;
    private boolean destroyedByCreativePlayer;

    public TileEntityBarrel(int maxWaterBuckets)
    {
        this.maxWaterBuckets = maxWaterBuckets;
        if(maxWaterBuckets <= -1)
        {
            this.waterBuckets = -1;
        }
    }

    public boolean isEmpty()
    {
        return this.waterBuckets == 0;
    }

    private boolean isFull()
    {
        return this.waterBuckets == this.maxWaterBuckets;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.waterBuckets = compound.getInteger("Water");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Water", this.waterBuckets);
        return compound;
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
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty();
    }

    public boolean setWatetBucket()
    {
        if(!isFull())
        {
            ++this.waterBuckets;
            return true;
        }
        return false;
    }

    public int getWaterBuckets()
    {
        return this.waterBuckets;
    }
}
