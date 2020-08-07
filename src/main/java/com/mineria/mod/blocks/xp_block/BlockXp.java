package com.mineria.mod.blocks.xp_block;

import com.mineria.mod.Mineria;
import com.mineria.mod.References;
import net.minecraft.block.BlockContainer;
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

public class BlockXp extends BlockContainer
{
	public BlockXp(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHarvestLevel("pickaxe", harvestlevel);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityXpBlock te = (TileEntityXpBlock)worldIn.getTileEntity(pos);
        te.setPlayer(playerIn, true);
        if(!worldIn.isRemote)
		{
            playerIn.openGui(Mineria.instance, References.GUI_XP_BLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
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
