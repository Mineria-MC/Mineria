package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaTileEntities;

public class WaterBarrelTileEntity extends AbstractWaterBarrelTileEntity
{
    public WaterBarrelTileEntity()
    {
        super(MineriaTileEntities.WATER_BARREL.get());
    }

    public WaterBarrelTileEntity(int capacity)
    {
        super(MineriaTileEntities.WATER_BARREL.get(), capacity);
    }
}
