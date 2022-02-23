package com.mineria.mod.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

@SuppressWarnings("deprecation")
public class TNTBarrelBlock extends Block
{
    public static final IntegerProperty GUNPOWDER = IntegerProperty.create("gunpowder", 0, 7);
    private static final VoxelShape INSIDE = box(1, 1, 1, 15, 16, 15);
    private static final Function<Integer, VoxelShape> SHAPE = (gunpowder) -> Shapes.join(Shapes.block(), box(1, 1 + gunpowder * 2, 1, 15, 16, 15), BooleanOp.ONLY_FIRST);

    public TNTBarrelBlock()
    {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(4f, 0f).sound(SoundType.WOOD));
        registerDefaultState(this.stateDefinition.any().setValue(GUNPOWDER, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return SHAPE.apply(state.getValue(GUNPOWDER));
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos)
    {
        return INSIDE;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (!oldState.is(state.getBlock()))
        {
            if (worldIn.hasNeighborSignal(pos) && state.getValue(GUNPOWDER) != 0)
            {
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
                worldIn.removeBlock(pos, isMoving);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (worldIn.hasNeighborSignal(pos) && state.getValue(GUNPOWDER) != 0)
        {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
            worldIn.removeBlock(pos, isMoving);
        }
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn)
    {
        if(!worldIn.isEmptyBlock(pos) && !worldIn.isClientSide)
        {
            BlockState previousState = worldIn.getBlockState(pos);
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), previousState);
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        ItemStack heldItem = player.getItemInHand(hand);

        if(heldItem.getItem() == Items.FLINT_AND_STEEL || heldItem.getItem() == Items.FIRE_CHARGE)
        {
            if(state.getValue(GUNPOWDER) != 0)
            {
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
                worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);

                if (heldItem.getItem() == Items.FLINT_AND_STEEL)
                {
                    heldItem.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(hand));
                }
                else if (!player.isCreative())
                {
                    heldItem.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        else if(heldItem.getItem().equals(Items.GUNPOWDER))
        {
            if(!player.blockPosition().equals(pos))
            {
                int count = state.getValue(GUNPOWDER);
                if(count < 7)
                {
                    worldIn.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                    worldIn.setBlockAndUpdate(pos, state.setValue(GUNPOWDER, count + 1));
                }
            }

            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        return super.use(state, worldIn, pos, player, hand, hit);
    }

    @Override
    public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile)
    {
        if (!worldIn.isClientSide)
        {
            if (projectile.isOnFire())
            {
                BlockPos blockpos = hit.getBlockPos();
                this.explode(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ(), state);
                worldIn.removeBlock(blockpos, false);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player)
    {
        if(!worldIn.isClientSide)
        {
            int count = state.getValue(GUNPOWDER);
            if(count != 0)
                worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GUNPOWDER, count)));
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player)
    {
        return player.isShiftKeyDown() ? new ItemStack(Items.GUNPOWDER) : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean dropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(GUNPOWDER);
    }

    private void explode(Level world, double x, double y, double z, BlockState state)
    {
        if(!world.isClientSide)
        {
            int gunPowder = state.getValue(GUNPOWDER);
            switch (gunPowder)
            {
                case 1 -> world.explode(null, x, y, z, 0.5F, false, Explosion.BlockInteraction.DESTROY);
                case 2, 3, 4, 5, 6, 7 -> world.explode(null, x, y, z, gunPowder - 1, gunPowder >= 6, Explosion.BlockInteraction.DESTROY);
            }
        }
    }
}
