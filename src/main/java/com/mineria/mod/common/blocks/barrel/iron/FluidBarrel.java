package com.mineria.mod.common.blocks.barrel.iron;

import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public interface FluidBarrel
{
    boolean storeFluid(Fluid fluidToStore);

    @Nullable
    Fluid getFluid();
}
