package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

// TODOLTR Add IFluidHandler capability
public abstract class AbstractWaterBarrelTileEntity extends BlockEntity
{
    private int capacity;
    private int buckets;
    private boolean destroyedByCreativePlayer;

    public AbstractWaterBarrelTileEntity(BlockEntityType<? extends AbstractWaterBarrelTileEntity> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        this.capacity = 0;
    }

    public AbstractWaterBarrelTileEntity(BlockEntityType<? extends AbstractWaterBarrelTileEntity> type, BlockPos pos, BlockState state, int capacity)
    {
        super(type, pos, state);
        this.capacity = capacity < 0 ? -1 : capacity;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.buckets = nbt.contains("Water") ? nbt.getInt("Water") : nbt.getInt("Buckets");
        this.capacity = nbt.contains("MaxWater") ? nbt.getInt("MaxWater") : nbt.getInt("Capacity");
    }

    @Override
    public CompoundTag save(CompoundTag compound)
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
        CompoundTag stackTag = barrel.getTag();
        if(stackTag == null || !stackTag.contains("BlockEntityTag", 10))
            return false;
        CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
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
                CompoundTag stackTag = waterSource.getTag();
                CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
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

        CompoundTag stackTag = waterSource.getTag();
        CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
        int fluidBuckets = blockEntityTag.getInt("Buckets");
        blockEntityTag.putInt("Buckets", fluidBuckets < 0 ? fluidBuckets : --fluidBuckets);
        return waterSource;
    }
}
