package com.mineria.mod.blocks.barrel.iron;

import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public interface FluidBarrel
{
    boolean storeFluid(Fluid fluidToStore);

    @Nullable
    Fluid getFluid();
}
