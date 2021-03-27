package com.mineria.mod.blocks;

import com.mineria.mod.util.RenderHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SpikeBlock extends MineriaBlock
{
	protected static final VoxelShape SPIKE_COLLISION_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.7375D * 16.0D, 16.0D);
	protected static final VoxelShape SPIKE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.8D * 16.0D, 16.0D);
	private final float attackDamage;
	
	public SpikeBlock(float hardness, float resistance, float attackDamage)
	{
		super(Material.IRON, hardness, resistance, SoundType.METAL, 2);
		this.attackDamage = attackDamage;
		RenderHandler.registerCutout(this);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		entityIn.attackEntityFrom(new DamageSource("onSpike"), this.attackDamage);
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
	public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context)
	{
		return SPIKE_SHAPE.withOffset(pos.getX(), pos.getY(), pos.getZ());
	}
}
