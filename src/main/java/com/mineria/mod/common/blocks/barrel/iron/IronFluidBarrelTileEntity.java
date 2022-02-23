package com.mineria.mod.common.blocks.barrel.iron;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaTileEntities;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class IronFluidBarrelTileEntity extends AbstractWaterBarrelTileEntity implements FluidBarrel
{
    private Fluid storedFluid = Fluids.EMPTY;

    public IronFluidBarrelTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.IRON_FLUID_BARREL.get(), pos, state, 24);
    }

    @Override
    public CompoundTag save(CompoundTag compound)
    {
        super.save(compound);
        compound.putString("StoredFluid", storedFluid.getRegistryName().toString());
        return compound;
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        this.storedFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(nbt.getString("StoredFluid")));
    }

    @Override
    public boolean storeFluid(Fluid fluidToStore)
    {
        return (MineriaUtils.doIf(this.storedFluid, fluid -> fluid.equals(Fluids.EMPTY), fluid -> this.storedFluid = fluidToStore) || this.storedFluid == fluidToStore) && super.addFluid();
    }

    @Override
    public Fluid getFluid()
    {
        super.removeFluid();
        Fluid result = storedFluid;
        if(this.isEmpty())
            storedFluid = Fluids.EMPTY;
        return result;
    }
}
