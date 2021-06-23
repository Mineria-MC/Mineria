package com.mineria.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.function.Function;

@SuppressWarnings("deprecation")
public class TNTBarrelBlock extends MineriaBlock
{
    public static final IntegerProperty GUNPOWDER = IntegerProperty.create("gunpowder", 0, 7);
    private static final VoxelShape INSIDE = makeCuboidShape(1, 1, 1, 15, 16, 15);
    private static final Function<Integer, VoxelShape> SHAPE = (gunpowder) -> VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), makeCuboidShape(1, 1 + gunpowder * 2, 1, 15, 16, 15), IBooleanFunction.ONLY_FIRST);

    public TNTBarrelBlock()
    {
        super(Material.WOOD, 4.0F, 0.0F, SoundType.WOOD, 0, ToolType.AXE);
        setDefaultState(this.stateContainer.getBaseState().with(GUNPOWDER, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE.apply(state.get(GUNPOWDER));
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return INSIDE;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (!oldState.matchesBlock(state.getBlock()))
        {
            if (worldIn.isBlockPowered(pos) && state.get(GUNPOWDER) != 0)
            {
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
                worldIn.removeBlock(pos, isMoving);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (worldIn.isBlockPowered(pos) && state.get(GUNPOWDER) != 0)
        {
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
            worldIn.removeBlock(pos, isMoving);
        }
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        if(!worldIn.isAirBlock(pos) && !worldIn.isRemote)
        {
            BlockState previousState = worldIn.getBlockState(pos);
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), previousState);
            worldIn.removeBlock(pos, false);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack heldItem = player.getHeldItem(hand);

        if(heldItem.getItem() == Items.FLINT_AND_STEEL || heldItem.getItem() == Items.FIRE_CHARGE)
        {
            if(state.get(GUNPOWDER) != 0)
            {
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

                if (heldItem.getItem() == Items.FLINT_AND_STEEL)
                {
                    heldItem.damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(hand));
                }
                else if (!player.isCreative())
                {
                    heldItem.shrink(1);
                }
            }

            return ActionResultType.func_233537_a_(worldIn.isRemote);
        }
        else if(heldItem.getItem().equals(Items.GUNPOWDER))
        {
            if(!player.getPosition().equals(pos))
            {
                int count = state.get(GUNPOWDER);
                if(count < 7)
                {
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                    worldIn.setBlockState(pos, state.with(GUNPOWDER, count + 1));
                }
            }

            return ActionResultType.func_233537_a_(worldIn.isRemote);
        }

        return super.onBlockActivated(state, worldIn, pos, player, hand, hit);
    }

    @Override
    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile)
    {
        if (!worldIn.isRemote)
        {
            if (projectile.isBurning())
            {
                BlockPos blockpos = hit.getPos();
                this.explode(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ(), state);
                worldIn.removeBlock(blockpos, false);
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if(!worldIn.isRemote)
        {
            int count = state.get(GUNPOWDER);
            if(count != 0)
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GUNPOWDER, count)));
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return player.isSneaking() ? new ItemStack(Items.GUNPOWDER) : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    @Override
    public PushReaction getPushReaction(BlockState state)
    {
        return PushReaction.BLOCK;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(GUNPOWDER);
    }

    private void explode(World world, double x, double y, double z, BlockState state)
    {
        if(!world.isRemote)
        {
            switch(state.get(GUNPOWDER))
            {
                case 1:
                    world.createExplosion(null, x, y, z, 0.5F, false, Explosion.Mode.DESTROY);
                    break;
                case 2:
                    world.createExplosion(null, x, y, z, 1.0F, false, Explosion.Mode.DESTROY);
                    break;
                case 3:
                    world.createExplosion(null, x, y, z, 2.0F, false, Explosion.Mode.DESTROY);
                    break;
                case 4:
                    world.createExplosion(null, x, y, z, 3.0F, false, Explosion.Mode.DESTROY);
                    break;
                case 5:
                    world.createExplosion(null, x, y, z, 4.0F, false, Explosion.Mode.DESTROY);
                    break;
                case 6:
                    world.createExplosion(null, x, y, z, 5.0F, true, Explosion.Mode.DESTROY);
                    break;
                case 7:
                    world.createExplosion(null, x, y, z, 6.0F, true, Explosion.Mode.DESTROY);
                    break;
            }
        }
    }
}
