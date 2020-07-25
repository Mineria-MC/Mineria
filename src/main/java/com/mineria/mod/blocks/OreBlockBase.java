package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class OreBlockBase extends Block
{
	public OreBlockBase(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		setHarvestLevel("pickaxe", harvestlevel);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
	}
	
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
	{
		return true;
	}
}
