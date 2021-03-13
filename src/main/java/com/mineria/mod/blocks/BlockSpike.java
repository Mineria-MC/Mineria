package com.mineria.mod.blocks;

public class BlockSpike// extends BlockBase
{
	/*
	protected static final AxisAlignedBB SPIKE_COLLISION_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.7375D, 1.0D);
    protected static final AxisAlignedBB SPIKE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8D, 1.0D);
	private final float attackDamage;
	
	public BlockSpike(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound, float attackDamage)
	{
		super(name, harvestlevel, materialIn, hardness, resistance, sound);
		this.attackDamage = attackDamage;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		entityIn.attackEntityFrom(DamageSource.GENERIC, this.attackDamage);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return SPIKE_COLLISION_AABB;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        return SPIKE_AABB.offset(pos);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	 */
}
