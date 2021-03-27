package com.mineria.mod.blocks.barrel;

import com.mineria.mod.init.TileEntitiesInit;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class WaterBarrelTileEntity extends TileEntity
{
    private final int maxWaterBuckets;
    private int waterBuckets;
    private boolean destroyedByCreativePlayer;

    public WaterBarrelTileEntity()
    {
        super(TileEntitiesInit.WATER_BARREL.get());
        this.maxWaterBuckets = 0;
    }

    public WaterBarrelTileEntity(int maxWaterBuckets)
    {
        super(TileEntitiesInit.WATER_BARREL.get());
        this.maxWaterBuckets = maxWaterBuckets;
        if(maxWaterBuckets <= -1)
            this.waterBuckets = -1;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.waterBuckets = nbt.getInt("Water");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putInt("Water", this.waterBuckets);
        return compound;
    }

    public boolean isEmpty()
    {
        return this.waterBuckets <= 0;
    }

    private boolean isFull()
    {
        return this.waterBuckets == this.maxWaterBuckets;
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

    public boolean increaseWaterBuckets()
    {
        if(!isFull())
        {
            ++this.waterBuckets;
            return true;
        }
        return false;
    }

    public static boolean checkWaterFromStack(ItemStack barrel)
    {
        CompoundNBT stackTag = barrel.getTag();
        if(stackTag == null || !stackTag.contains("BlockEntityTag", 10))
            return false;
        CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");
        if(!blockEntityTag.contains("Water"))
            return false;
        return blockEntityTag.getInt("Water") != 0;
    }

    public static void decreaseWaterFromStack(ItemStack barrel)
    {
        CompoundNBT stackTag = barrel.getTag();
        CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");
        int water = blockEntityTag.getInt("Water");
        blockEntityTag.putInt("Water", water < 0 ? water : --water);
    }
}
