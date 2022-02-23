package com.mineria.mod.common.blocks.distiller;

import com.mineria.mod.common.init.MineriaTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class DistillerBlock extends Block implements EntityBlock
{
    private static final VoxelShape SHAPE = makeShape();

    public DistillerBlock()
    {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(7f, 9f).sound(SoundType.METAL).requiresCorrectToolForDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new DistillerTileEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        return level.isClientSide || blockEntityType != MineriaTileEntities.DISTILLER.get() ? null : (pLevel, pPos, pState, pBlockEntity) -> DistillerTileEntity.serverTick(pLevel, pPos, pState, (DistillerTileEntity) pBlockEntity);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult)
    {
        if(!world.isClientSide)
        {
            BlockEntity tile = world.getBlockEntity(pos);
            if(tile instanceof DistillerTileEntity)
                NetworkHooks.openGui((ServerPlayer) player, (DistillerTileEntity) tile, pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean hasFlags)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if(tile instanceof DistillerTileEntity)
            Containers.dropContents(world, pos, ((DistillerTileEntity) tile).getInventory().toNonNullList());

        super.onRemove(state, world, pos, newState, hasFlags);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return super.mirror(state, mirror);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction)
    {
        return super.rotate(state, world, pos, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return super.getStateForPlacement(ctx);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random)
    {
        super.animateTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_)
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
        VoxelShape shape = Shapes.empty();

        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.0625, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.0625, 0.1875, 0.5, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.375, 0.1875, 0.5, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.0625, 0.375, 0.5, 0.5, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.0625, 0.0625, 0.5, 0.5, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.0625, 0.5, 0.5625, 0.4375), BooleanOp.OR);

        return shape;
    }
}
