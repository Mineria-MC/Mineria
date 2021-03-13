package com.mineria.mod.blocks.xp_block;

public class BlockXp// extends BlockContainer
{
	/*
	public BlockXp(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.MINERIA_GROUP);
		setHarvestLevel("pickaxe", harvestlevel);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityXpBlock te = (TileEntityXpBlock)worldIn.getTileEntity(pos);
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

	 */
}
