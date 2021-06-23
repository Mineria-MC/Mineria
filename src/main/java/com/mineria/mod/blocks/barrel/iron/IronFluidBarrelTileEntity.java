package com.mineria.mod.blocks.barrel.iron;

import com.mineria.mod.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.init.TileEntitiesInit;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class IronFluidBarrelTileEntity extends AbstractWaterBarrelTileEntity implements FluidBarrel
{
    private Fluid storedFluid = Fluids.EMPTY;

    public IronFluidBarrelTileEntity()
    {
        super(TileEntitiesInit.IRON_FLUID_BARREL.get(), 24);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.putString("StoredFluid", storedFluid.getRegistryName().toString());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        super.read(state, nbt);
        this.storedFluid = ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryCreate(nbt.getString("StoredFluid")));
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
