package com.mineria.mod.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockWoodCut extends BlockBase
{
    public BlockWoodCut(String name)
    {
        super( name + "_woodcut", 1, Material.WOOD, 1.2F, 1.0F, SoundType.WOOD);
    }
}
