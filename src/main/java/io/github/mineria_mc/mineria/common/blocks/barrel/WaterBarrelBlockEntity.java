package io.github.mineria_mc.mineria.common.blocks.barrel;

import io.github.mineria_mc.mineria.common.init.MineriaTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WaterBarrelBlockEntity extends AbstractWaterBarrelBlockEntity {
    public WaterBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(MineriaTileEntities.WATER_BARREL.get(), pos, state);
    }

    public WaterBarrelBlockEntity(int capacity, BlockPos pos, BlockState state) {
        super(MineriaTileEntities.WATER_BARREL.get(), pos, state, capacity);
    }
}
