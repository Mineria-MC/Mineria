package com.mineria.mod.common.blocks.barrel.iron;

import com.mineria.mod.common.blocks.barrel.AbstractWaterBarrelTileEntity;
import com.mineria.mod.common.init.MineriaTileEntities;
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
        super(MineriaTileEntities.IRON_FLUID_BARREL.get(), 24);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        super.save(compound);
        compound.putString("StoredFluid", storedFluid.getRegistryName().toString());
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt)
    {
        super.load(state, nbt);
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
