package com.mineria.mod.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBlueGlowstone extends MineriaBlock
{
	public BlockBlueGlowstone()
	{
		super(Material.GLASS, 0.3F, 0.3F, -1, SoundType.GLASS);
		setLightLevel(1);
		this.translucent = true;
	}
}
