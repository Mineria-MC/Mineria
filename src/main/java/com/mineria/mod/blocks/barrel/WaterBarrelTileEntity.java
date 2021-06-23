package com.mineria.mod.blocks.barrel;

import com.mineria.mod.init.TileEntitiesInit;

public class WaterBarrelTileEntity extends AbstractWaterBarrelTileEntity
{
    public WaterBarrelTileEntity()
    {
        super(TileEntitiesInit.WATER_BARREL.get());
    }

    public WaterBarrelTileEntity(int capacity)
    {
        super(TileEntitiesInit.WATER_BARREL.get(), capacity);
    }
}
