package com.mineria.mod.blocks.barrel;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockWaterBarrel extends AbstractBlockWaterBarrel
{
    public BlockWaterBarrel(int maxBuckets)
    {
        super(maxBuckets, 2, 10, 0);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityWaterBarrel(this.capacity);
    }

    @Override
    protected void addInformationOnShift(ItemStack stack, List<String> tooltip, ITooltipFlag advanced)
    {
    }
}
