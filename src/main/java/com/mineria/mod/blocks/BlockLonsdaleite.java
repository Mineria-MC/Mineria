package com.mineria.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLonsdaleite extends MineriaBlockMetal
{
	public BlockLonsdaleite()
	{
		super(10, 17.5F, 3);
	}

	@Override
	public boolean isTranslucent(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate)
		{
			return true;
		}

		return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
