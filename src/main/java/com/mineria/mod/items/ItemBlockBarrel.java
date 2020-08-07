package com.mineria.mod.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBarrel extends ItemBlock
{
    public ItemBlockBarrel(Block block)
    {
        super(block);
        setRegistryName(block.getRegistryName());
        setMaxStackSize(1);
    }
}
