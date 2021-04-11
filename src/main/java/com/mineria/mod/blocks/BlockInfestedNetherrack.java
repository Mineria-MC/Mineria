package com.mineria.mod.blocks;

import com.mineria.mod.entity.EntityGoldenSilverfish;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockInfestedNetherrack extends MineriaBlock
{
	public BlockInfestedNetherrack()
	{
		super(Material.CLAY, 0, 0, -1, SoundType.STONE);
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
			EntityGoldenSilverfish goldenSilverfish = new EntityGoldenSilverfish(worldIn);

			goldenSilverfish.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			worldIn.spawnEntity(goldenSilverfish);
			goldenSilverfish.spawnExplosionParticle();
		}
	}

	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Blocks.NETHERRACK);
	}

	public static boolean canContainSilverfish(IBlockState blockState)
    {
        return blockState == Blocks.NETHERRACK.getDefaultState();
    }
}