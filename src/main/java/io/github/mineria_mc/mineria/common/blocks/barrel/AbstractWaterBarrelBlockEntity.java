package io.github.mineria_mc.mineria.common.blocks.barrel;

import io.github.mineria_mc.mineria.common.blocks.barrel.iron.IronFluidBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

// TODO Add IFluidHandler capability
public abstract class AbstractWaterBarrelBlockEntity extends BlockEntity {
    protected int capacity;
    protected int buckets;
    protected boolean destroyedByCreativePlayer;

    public AbstractWaterBarrelBlockEntity(BlockEntityType<? extends AbstractWaterBarrelBlockEntity> type, BlockPos pos, BlockState state) {
        this(type, pos, state, 0);
    }

    public AbstractWaterBarrelBlockEntity(BlockEntityType<? extends AbstractWaterBarrelBlockEntity> type, BlockPos pos, BlockState state, int capacity) {
        super(type, pos, state);
        this.capacity = capacity < 0 ? -1 : capacity;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.buckets = Math.max(0, nbt.contains("Water") ? nbt.getInt("Water") : nbt.getInt("Buckets"));
        this.capacity = nbt.contains("MaxWater") ? nbt.getInt("MaxWater") : nbt.getInt("Capacity");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("Buckets", this.buckets);
        nbt.putInt("Capacity", this.capacity);
    }

    public boolean isEmpty() {
        return this.buckets == 0;
    }

    protected boolean isFull() {
        return this.buckets == this.capacity;
    }

    // aka. 'is not infinite'
    public boolean isFinite() {
        return this.capacity >= 0;
    }

    /**
     * NOTE: This must be manually serialized if a field value is returned. See {@link IronFluidBarrelBlockEntity#getContainedFluid()}.
     * @return The contained fluid (used to filter the accepted fluid type) or empty if no fluid is stored and the barrel can store any fluid.
     */
    @Nonnull
    public Fluid getContainedFluid() {
        // Because the basic barrels only store water.
        return Fluids.WATER;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isDestroyedByCreativePlayer() {
        return this.destroyedByCreativePlayer;
    }

    public void setDestroyedByCreativePlayer(boolean isCreative) {
        this.destroyedByCreativePlayer = isCreative;
    }

    public boolean shouldDrop() {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() && this.isFinite();
    }

    public int getBuckets() {
        return buckets;
    }

    public boolean addFluid(FluidStack stack) {
        if (isFull()) {
            return false;
        }
        if(!isValidFluid(stack.getFluid()) || stack.getAmount() < FluidType.BUCKET_VOLUME) {
            return false;
        }
        if (isFinite()) {
            ++this.buckets;
            setChanged();
        }
        return true;
    }

    public boolean removeFluid(IFluidHandler fluidHandler, FluidStack stack) {
        if (isEmpty()) {
            return false;
        }
        if(!stack.isEmpty() || fluidHandler.fill(new FluidStack(getContainedFluid(), FluidType.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE) != FluidType.BUCKET_VOLUME) {
            return false;
        }
        if (isFinite()) {
            --this.buckets;
            setChanged();
        }
        return true;
    }

    protected boolean isValidFluid(Fluid fluid) {
        Fluid contained = getContainedFluid();
        return contained == Fluids.EMPTY || contained.isSame(fluid);
    }

    public static boolean checkFluidFromStack(ItemStack barrel) {
        CompoundTag stackTag = barrel.getTag();
        if (stackTag == null || !stackTag.contains("BlockEntityTag", 10)) {
            return false;
        }
        CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
        if (!blockEntityTag.contains("Buckets")) {
            return false;
        }
        return blockEntityTag.getInt("Buckets") != 0;
    }

    @Nonnull
    public static Fluid getFluidFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) {
            return Fluids.EMPTY;
        }
        CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
        if(!blockEntityTag.contains("StoredFluid")) {
            return Fluids.EMPTY;
        }
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(blockEntityTag.getString("StoredFluid")));
        return fluid == null ? Fluids.EMPTY : fluid;
    }

    public static boolean checkFluidFromStack(ItemStack waterSource, @Nonnull Fluid fluid) {
        if (waterSource.getItem() instanceof BucketItem) {
            return ((BucketItem) waterSource.getItem()).getFluid().equals(Fluids.WATER);
        }

        if (checkFluidFromStack(waterSource)) {
            Fluid stored = getFluidFromStack(waterSource);
            // if fluid is empty then there is water as we checked earlier if buckets were stored.
            return (stored == Fluids.EMPTY && fluid.isSame(Fluids.WATER)) || stored.isSame(fluid);
        }
        return false;
    }

    public static ItemStack decreaseFluidFromStack(ItemStack waterSource) {
        if (waterSource.getItem() instanceof BucketItem && ((BucketItem) waterSource.getItem()).getFluid().equals(Fluids.WATER)) {
            return new ItemStack(Items.BUCKET);
        }

        CompoundTag stackTag = waterSource.getTag();
        if(stackTag != null) {
            CompoundTag blockEntityTag = stackTag.getCompound("BlockEntityTag");
            int fluidBuckets = blockEntityTag.getInt("Buckets");
            blockEntityTag.putInt("Buckets", fluidBuckets < 0 ? fluidBuckets : --fluidBuckets);
        }
        return waterSource;
    }
}
