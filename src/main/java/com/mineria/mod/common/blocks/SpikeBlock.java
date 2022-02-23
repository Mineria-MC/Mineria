package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpikeBlock extends Block
{
	protected static final VoxelShape SPIKE_COLLISION_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.7375D * 16.0D, 16.0D);
	protected static final VoxelShape SPIKE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.8D * 16.0D, 16.0D);
	private final float attackDamage;
	
	public SpikeBlock(float hardness, float resistance, float attackDamage)
	{
		super(BlockBehaviour.Properties.of(Material.METAL).strength(hardness, resistance).sound(SoundType.METAL).requiresCorrectToolForDrops());
		this.attackDamage = attackDamage;
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn)
	{
		boolean damaged = entityIn.hurt(new DamageSource("onSpike"), this.attackDamage);
		if(damaged && entityIn instanceof ServerPlayer)
		{
			MineriaCriteriaTriggers.DAMAGE_FROM_SPIKE.trigger((ServerPlayer) entityIn, this, this.attackDamage);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SPIKE_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SPIKE_COLLISION_SHAPE;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context)
	{
		return SPIKE_SHAPE.move(pos.getX(), pos.getY(), pos.getZ());
	}
}
