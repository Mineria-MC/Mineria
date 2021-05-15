package com.mineria.mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNTBarrel extends MineriaBlock
{
    public static final PropertyInteger GUNPOWDER = PropertyInteger.create("gunpowder", 0, 7);

    public BlockTNTBarrel()
    {
        super(Material.WOOD, 4.0F, 0.0F, 0, "axe", SoundType.WOOD);
        setDefaultState(this.blockState.getBaseState().withProperty(GUNPOWDER, 0));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.isBlockPowered(pos) && state.getValue(GUNPOWDER) != 0)
        {
            worldIn.setBlockToAir(pos);
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (worldIn.isBlockPowered(pos) && state.getValue(GUNPOWDER) != 0)
        {
            worldIn.setBlockToAir(pos);
            this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
        }
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        IBlockState previousState = world.getBlockState(pos);
        world.setBlockToAir(pos);
        this.explode(world, pos.getX(), pos.getY(), pos.getZ(), previousState);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if(!heldItem.isEmpty())
        {
            if(heldItem.getItem() == Items.FLINT_AND_STEEL || heldItem.getItem() == Items.FIRE_CHARGE)
            {
                if(state.getValue(GUNPOWDER) != 0)
                {
                    this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

                    if (heldItem.getItem() == Items.FLINT_AND_STEEL)
                    {
                        heldItem.damageItem(1, playerIn);
                    }
                    else if (!playerIn.capabilities.isCreativeMode)
                    {
                        heldItem.shrink(1);
                    }
                }

                return true;
            }
            else if(heldItem.getItem().equals(Items.GUNPOWDER))
            {
                int count = state.getValue(GUNPOWDER);
                if(count < 7)
                {
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                    worldIn.setBlockState(pos, state.withProperty(GUNPOWDER, count + 1));
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow)
        {
            EntityArrow entityarrow = (EntityArrow)entityIn;

            if (entityarrow.isBurning() && state.getValue(GUNPOWDER) != 0)
            {
                worldIn.setBlockToAir(pos);
                this.explode(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if(!worldIn.isRemote)
        {
            int count = state.getValue(GUNPOWDER);
            if(count != 0)
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GUNPOWDER, count)));
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return player.isSneaking() ? new ItemStack(Items.GUNPOWDER) : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(GUNPOWDER, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(GUNPOWDER);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, GUNPOWDER);
    }

    private void explode(World world, double x, double y, double z, IBlockState state)
    {
        switch(state.getValue(GUNPOWDER))
        {
            case 1:
                world.createExplosion(null, x, y, z, 0.5F, true);
                break;
            case 2:
                world.createExplosion(null, x, y, z, 1.0F, true);
                break;
            case 3:
                world.createExplosion(null, x, y, z, 2.0F, true);
                break;
            case 4:
                world.createExplosion(null, x, y, z, 3.0F, true);
                break;
            case 5:
                world.createExplosion(null, x, y, z, 4.0F, true);
                break;
            case 6:
                world.newExplosion(null, x, y, z, 5.0F, true, true);
                break;
            case 7:
                world.newExplosion(null, x, y, z, 6.0F, true, true);
                break;
        }
    }
}
