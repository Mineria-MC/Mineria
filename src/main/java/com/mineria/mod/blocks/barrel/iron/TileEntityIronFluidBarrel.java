package com.mineria.mod.blocks.barrel.iron;

import com.mineria.mod.blocks.barrel.AbstractTileEntityWaterBarrel;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.Objects;

public class TileEntityIronFluidBarrel extends AbstractTileEntityWaterBarrel implements FluidBarrel
{
    @Nullable
    private Fluid storedFluid;

    public TileEntityIronFluidBarrel()
    {
        super(24);
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation("container.iron_fluid_barrel");
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

    @Override
    public boolean storeFluid(Fluid fluidToStore)
    {
        return (MineriaUtils.doIf(this.storedFluid, Objects::isNull, fluid -> this.storedFluid = fluidToStore) || this.storedFluid == fluidToStore) && super.increaseFluidBuckets();
    }

    @Nullable
    @Override
    public Fluid getFluid()
    {
        super.decreaseFluidBuckets();
        Fluid result = storedFluid;
        if(this.isEmpty())
            storedFluid = null;
        return result;
    }
}
