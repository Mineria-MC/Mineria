package com.mineria.mod.common.blocks.barrel;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class WaterBarrelBlock extends AbstractWaterBarrelBlock
{
    public WaterBarrelBlock(int initialCapacity)
    {
        super(2, 10, 0, initialCapacity);
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new WaterBarrelTileEntity(initialCapacity);
    }
}
