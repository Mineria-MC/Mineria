package com.mineria.mod.common.blocks.barrel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;
import java.util.List;

public class WaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public WaterBarrelBlock(int initialCapacity)
    {
        super(2, 10, initialCapacity);
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WaterBarrelTileEntity(initialCapacity, pos, state);
    }
}
