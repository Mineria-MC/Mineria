package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import com.mineria.mod.blocks.MineriaBlock;
import com.mineria.mod.util.GuiHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockXp extends MineriaBlock implements ITileEntityProvider
{
	public BlockXp()
	{
		super(Material.IRON, 2.5F, 5, 1, SoundType.METAL);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
            playerIn.openGui(Mineria.INSTANCE, GuiHandler.GUI_XP_BLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityXpBlock();
	}
}
