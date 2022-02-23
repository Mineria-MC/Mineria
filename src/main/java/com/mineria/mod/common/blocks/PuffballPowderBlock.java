package com.mineria.mod.common.blocks;

import com.mineria.mod.common.init.MineriaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class PuffballPowderBlock extends Block
{
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public PuffballPowderBlock()
    {
        super(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).strength(1.2F).randomTicks().sound(SoundType.WEEPING_VINES));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random rand)
    {
        BlockState underneath = world.getBlockState(pos.below());
        if(underneath.is(Blocks.GRASS_BLOCK))
        {
            world.setBlock(pos.below(), Blocks.MYCELIUM.defaultBlockState(), 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx)
    {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos newPos)
    {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, newState, world, pos, newPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos)
    {
        return !world.isEmptyBlock(pos.below());
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        return new ItemStack(MineriaBlocks.PUFFBALL);
    }
}
