package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import com.mineria.mod.entity.EntityGoldenFish;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockInfestedNetherrack extends Block
{
	public BlockInfestedNetherrack(String name, SoundType sound)
	{
		super(Material.CLAY);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHardness(0.0F);
		this.setSoundType(sound);
		this.setDefaultState(this.blockState.getBaseState());
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if(!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops"))
		{
			EntityGoldenFish entitygoldenfish = new EntityGoldenFish(worldIn);
			entitygoldenfish.setLocationAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
			worldIn.spawnEntity(entitygoldenfish);
			entitygoldenfish.spawnExplosionParticle();
		}
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Blocks.NETHERRACK);
	}

	public static boolean canContainSilverfish(IBlockState blockState)
    {
        Block block = blockState.getBlock();
        return blockState == Blocks.NETHERRACK.getDefaultState();
    }
}