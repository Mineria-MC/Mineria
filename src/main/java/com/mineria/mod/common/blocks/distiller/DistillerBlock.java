package com.mineria.mod.common.blocks.distiller;

import com.mineria.mod.common.blocks.MineriaBlock;
import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class DistillerBlock extends MineriaBlock
{
    private static final VoxelShape SHAPE = makeShape();

    public DistillerBlock()
    {
        super(Material.METAL, 7.0F, 9.0F, SoundType.METAL, 2);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return MineriaTileEntities.DISTILLER.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        if(!world.isClientSide)
        {
            TileEntity tile = world.getBlockEntity(pos);
            if(tile instanceof DistillerTileEntity)
                NetworkHooks.openGui((ServerPlayerEntity) player, (DistillerTileEntity) tile, pos);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean hasFlags)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if(tile instanceof DistillerTileEntity)
            InventoryHelper.dropContents(world, pos, ((DistillerTileEntity) tile).getInventory().toNonNullList());

        super.onRemove(state, world, pos, newState, hasFlags);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return super.mirror(state, mirror);
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return super.rotate(state, world, pos, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx)
    {
        return super.getStateForPlacement(ctx);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random)
    {
        super.animateTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return SHAPE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    private static VoxelShape makeShape()
    {
        VoxelShape shape = VoxelShapes.empty();

        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 0.0625, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.125, 0.0625, 0.0625, 0.1875, 0.5, 0.125), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.125, 0.0625, 0.375, 0.1875, 0.5, 0.4375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.0625, 0.375, 0.5, 0.5, 0.4375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.4375, 0.0625, 0.0625, 0.5, 0.5, 0.125), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.125, 0.5, 0.0625, 0.5, 0.5625, 0.4375), IBooleanFunction.OR);

        return shape;
    }
}
