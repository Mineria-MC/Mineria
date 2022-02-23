package com.mineria.mod.common.blocks.barrel;

import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WaterBarrelTileEntity extends AbstractWaterBarrelTileEntity
{
    public WaterBarrelTileEntity(BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.WATER_BARREL.get(), pos, state);
    }

    public WaterBarrelTileEntity(int capacity, BlockPos pos, BlockState state)
    {
        super(MineriaTileEntities.WATER_BARREL.get(), pos, state, capacity);
    }
}
