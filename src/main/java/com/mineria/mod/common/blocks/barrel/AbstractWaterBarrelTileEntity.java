package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

// TODOLTR Add IFluidHandler capability
public abstract class AbstractWaterBarrelTileEntity extends TileEntity
{
    private int capacity;
    private int buckets;
    private boolean destroyedByCreativePlayer;

    public AbstractWaterBarrelTileEntity(TileEntityType<? extends AbstractWaterBarrelTileEntity> type)
    {
        super(type);
        this.capacity = 0;
    }

    public AbstractWaterBarrelTileEntity(TileEntityType<? extends AbstractWaterBarrelTileEntity> type, int capacity)
    {
        super(type);
        this.capacity = capacity < 0 ? -1 : capacity;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
        this.buckets = nbt.contains("Water") ? nbt.getInt("Water") : nbt.getInt("Buckets");
        this.capacity = nbt.contains("MaxWater") ? nbt.getInt("MaxWater") : nbt.getInt("Capacity");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        super.save(compound);
        compound.putInt("Buckets", this.buckets);
        compound.putInt("Capacity", this.capacity);
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
        return this.capacity < 0;
    }

    public int getCapacity()
    {
        return capacity;
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

    public int getBuckets()
    {
        return buckets;
    }

    public boolean addFluid()
    {
        if(!isFull())
        {
            if(!isInfinite())
                ++this.buckets;
            return true;
        }
        return false;
    }

    public boolean removeFluid()
    {
        if(!isEmpty())
        {
            if(!isInfinite())
                --this.buckets;
            return true;
        }
        return false;
    }

    public static boolean checkFluidFromStack(ItemStack barrel)
    {
        CompoundNBT stackTag = barrel.getTag();
        if(stackTag == null || !stackTag.contains("BlockEntityTag", 10))
            return false;
        CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");
        if(!blockEntityTag.contains("Buckets"))
            return false;
        return blockEntityTag.getInt("Buckets") != 0;
    }

    public static boolean checkWaterFromStack(ItemStack waterSource)
    {
        if(waterSource.getItem() instanceof BucketItem)
            return ((BucketItem) waterSource.getItem()).getFluid().equals(Fluids.WATER);

        if(checkFluidFromStack(waterSource))
        {
            if(waterSource.getItem().equals(MineriaBlocks.getItemFromBlock(MineriaBlocks.IRON_FLUID_BARREL)))
            {
                CompoundNBT stackTag = waterSource.getTag();
                CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");
                return blockEntityTag.contains("StoredFluid") && blockEntityTag.getString("StoredFluid").equals(Fluids.WATER.getRegistryName().toString());
            }
            return true;
        }
        return false;
    }

    public static ItemStack decreaseFluidFromStack(ItemStack waterSource)
    {
        if(waterSource.getItem() instanceof BucketItem && ((BucketItem) waterSource.getItem()).getFluid().equals(Fluids.WATER))
            return new ItemStack(Items.BUCKET);

        CompoundNBT stackTag = waterSource.getTag();
        CompoundNBT blockEntityTag = stackTag.getCompound("BlockEntityTag");
        int fluidBuckets = blockEntityTag.getInt("Buckets");
        blockEntityTag.putInt("Buckets", fluidBuckets < 0 ? fluidBuckets : --fluidBuckets);
        return waterSource;
    }
}
