package com.mineria.mod.blocks.barrel;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBarrel extends TileEntity
{
    private int maxWaterBuckets;
    private int waterBuckets;
    private boolean destroyedByCreativePlayer;

    public TileEntityBarrel()
    {
    }

    public TileEntityBarrel(int maxWaterBuckets)
    {
        this.maxWaterBuckets = maxWaterBuckets < 0 ? -1 : maxWaterBuckets;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.waterBuckets = compound.getInteger("Water");
        this.maxWaterBuckets = compound.getInteger("MaxWater");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Water", this.waterBuckets);
        compound.setInteger("MaxWater", this.maxWaterBuckets);
        return compound;
    }

    public boolean isEmpty()
    {
        return this.waterBuckets == 0;
    }

    private boolean isFull()
    {
        return this.waterBuckets == this.maxWaterBuckets;
    }

    private boolean isInfinite()
    {
        return this.waterBuckets < 0;
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

    public boolean increaseWaterBuckets()
    {
        if(!isFull())
        {
            if(!isInfinite())
                ++this.waterBuckets;
            return true;
        }
        return false;
    }

    public boolean decreaseWaterBuckets()
    {
        if(!isEmpty())
        {
            if(!isInfinite())
                --waterBuckets;
            return true;
        }
        return false;
    }

    public int getWaterBuckets()
    {
        return this.waterBuckets;
    }

    public static boolean checkWaterFromStack(ItemStack barrel)
    {
        NBTTagCompound stackTag = barrel.getTagCompound();
        if(stackTag == null || !stackTag.hasKey("BlockEntityTag", 10))
            return false;
        NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");
        if(!blockEntityTag.hasKey("Water"))
            return false;
        return blockEntityTag.getInteger("Water") != 0;
    }

    public static void decreaseWaterFromStack(ItemStack barrel)
    {
        NBTTagCompound stackTag = barrel.getTagCompound();
        NBTTagCompound blockEntityTag = stackTag.getCompoundTag("BlockEntityTag");
        int water = blockEntityTag.getInteger("Water");
        blockEntityTag.setInteger("Water", water < 0 ? water : --water);
    }
}
