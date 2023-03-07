package io.github.mineria_mc.mineria.common.blocks.barrel.iron;

import com.google.common.base.Preconditions;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IronFluidBarrelBlockEntity extends AbstractWaterBarrelBlockEntity {
    protected static final Lazy<ResourceLocation> EMPTY_FLUID_ID = Lazy.of(() -> Preconditions.checkNotNull(ForgeRegistries.FLUIDS.getKey(Fluids.EMPTY), "Fluids.EMPTY registry name is missing (some annoying mod broke minecraft)"));
    @Nonnull
    private ResourceLocation storedFluidId = EMPTY_FLUID_ID.get();
    @Nullable
    private Fluid storedFluid;

    public IronFluidBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.IRON_FLUID_BARREL.get(), pos, state, 24);
    }

    @Nonnull
    @Override
    public Fluid getContainedFluid() {
        if(storedFluid == null) {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(storedFluidId);
            if(fluid == null) {
                Mineria.LOGGER.warn("Stored fluid in iron fluid barrel at '" + getBlockPos().toShortString() + "' is not registered! The barrel will be emptied.");
                storedFluidId = EMPTY_FLUID_ID.get();
                buckets = 0;
                fluid = Fluids.EMPTY;
            }
            storedFluid = fluid;
        }
        return storedFluid;
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("StoredFluid", storedFluidId.toString());
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.storedFluidId = new ResourceLocation(nbt.getString("StoredFluid"));
    }

    @Override
    public boolean addFluid(FluidStack stack) {
        if(super.addFluid(stack)) {
            if(getContainedFluid() == Fluids.EMPTY) {
                Fluid fluid = stack.getFluid();
                ResourceLocation regName = ForgeRegistries.FLUIDS.getKey(fluid);
                if(regName == null) {
                    Mineria.LOGGER.warn("Tried to store unregistered fluid in iron fluid barrel at '{}': {}", getBlockPos(), fluid);
                    return false;
                }
                storedFluidId = regName;
                storedFluid = fluid;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFluid(IFluidHandler fluidHandler, FluidStack stack) {
        if(super.removeFluid(fluidHandler, stack)) {
            if(isEmpty()) {
                storedFluidId = EMPTY_FLUID_ID.get();
                storedFluid = Fluids.EMPTY;
            }
            return true;
        }
        return false;
    }
}
