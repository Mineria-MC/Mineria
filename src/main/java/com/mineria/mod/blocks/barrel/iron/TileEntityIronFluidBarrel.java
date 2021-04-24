package com.mineria.mod.blocks.barrel.iron;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.Objects;

public class TileEntityIronFluidBarrel extends AbstractTileEntityWaterBarrel
{
    @Nullable
    private Fluid storedFluid;

    public TileEntityIronFluidBarrel()
    {
        super(24);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if(storedFluid != null)
            compound.setString("StoredFluid", storedFluid.getName());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        storedFluid = compound.hasKey("StoredFluid") ? FluidRegistry.getFluid(compound.getString("StoredFluid")) : null;
    }

    protected boolean storeFluid(Fluid fluidToStore)
    {
        return (MineriaUtils.doIf(this.storedFluid, Objects::isNull, fluid -> this.storedFluid = fluidToStore) || this.storedFluid == fluidToStore) && super.increaseFluidBuckets();
    }

    @Nullable
    protected Fluid getFluid()
    {
        super.decreaseFluidBuckets();
        Fluid fluid = storedFluid;
        if(this.isEmpty())
            storedFluid = null;
        return fluid;
    }
}
