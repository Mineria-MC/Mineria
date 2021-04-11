package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.Mineria;
import com.mineria.mod.blocks.MineriaBlock;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.GuiHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockTitaneExtractor extends MineriaBlock implements ITileEntityProvider
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool EXTRACTING = PropertyBool.create("extracting");
	
	public BlockTitaneExtractor()
	{
		super(Material.IRON, 5, 10, 2, SoundType.METAL);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRACTING, false));
	}

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(EXTRACTING) ? 8 : 0;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            IBlockState northState = worldIn.getBlockState(pos.north());
            IBlockState southState = worldIn.getBlockState(pos.south());
            IBlockState westState = worldIn.getBlockState(pos.west());
            IBlockState eastState = worldIn.getBlockState(pos.east());
            EnumFacing facing = state.getValue(FACING);

            if (facing == EnumFacing.NORTH && northState.isFullBlock() && !southState.isFullBlock())
            {
                facing = EnumFacing.SOUTH;
            }
            else if (facing == EnumFacing.SOUTH && southState.isFullBlock() && !northState.isFullBlock())
            {
                facing = EnumFacing.NORTH;
            }
            else if (facing == EnumFacing.WEST && westState.isFullBlock() && !eastState.isFullBlock())
            {
                facing = EnumFacing.EAST;
            }
            else if (facing == EnumFacing.EAST && eastState.isFullBlock() && !westState.isFullBlock())
            {
                facing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, facing), 2);
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (stateIn.getValue(EXTRACTING))
        {
            EnumFacing facing = stateIn.getValue(FACING);
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double z = pos.getZ() + 0.5D;
            double w = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (facing)
            {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x - 0.52D, y, z + w, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, x - 0.52D, y, z + w, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.52D, y, z + w, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, x + 0.52D, y, z + w, 0.0D, 0.0D, 0.0D);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + w, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, x + w, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + w, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, x + w, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                default:
                    break;
            }
        }
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        	playerIn.openGui(Mineria.INSTANCE, GuiHandler.GUI_TITANE_EXTRACTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
	
	public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tile = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, BlocksInit.TITANE_EXTRACTOR.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(EXTRACTING, active), 3);

        if (tile != null)
        {
            tile.validate();
            worldIn.setTileEntity(pos, tile);
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityTitaneExtractor();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntityTitaneExtractor tile = (TileEntityTitaneExtractor)worldIn.getTileEntity(pos);
        tile.getInventory().toNonNullList().forEach(stack -> {
            EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
            worldIn.spawnEntity(item);
        });

        super.breakBlock(worldIn, pos, state);
    }

    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
	public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.getFront(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y)
        {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
	public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, EXTRACTING);
    }
}
