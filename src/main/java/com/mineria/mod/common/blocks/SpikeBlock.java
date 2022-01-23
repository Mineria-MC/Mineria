package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SpikeBlock extends MineriaBlock
{
	protected static final VoxelShape SPIKE_COLLISION_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.7375D * 16.0D, 16.0D);
	protected static final VoxelShape SPIKE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.8D * 16.0D, 16.0D);
	private final float attackDamage;
	
	public SpikeBlock(float hardness, float resistance, float attackDamage)
	{
		super(Material.METAL, hardness, resistance, SoundType.METAL, 2);
		this.attackDamage = attackDamage;
	}

	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		boolean damaged = entityIn.hurt(new DamageSource("onSpike"), this.attackDamage);
		if(damaged && entityIn instanceof ServerPlayerEntity)
		{
			MineriaCriteriaTriggers.DAMAGE_FROM_SPIKE.trigger((ServerPlayerEntity) entityIn, this, this.attackDamage);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SPIKE_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SPIKE_COLLISION_SHAPE;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
	{
		return SPIKE_SHAPE.move(pos.getX(), pos.getY(), pos.getZ());
	}
}
