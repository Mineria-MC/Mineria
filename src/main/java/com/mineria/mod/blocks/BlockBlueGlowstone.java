package com.mineria.mod.blocks;

import com.mineria.mod.Mineria;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBlueGlowstone extends Block
{
	public BlockBlueGlowstone(String name, int harvestlevel, Material materialIn, float hardness, float resistance, SoundType sound, float lightValue)
	{
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Mineria.mineriaTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setSoundType(sound);
		this.lightValue = (int)lightValue;
		this.translucent = true;
	}
}
