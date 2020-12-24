package com.mineria.mod.util.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class CustomItemStackHandler extends ItemStackHandler
{
    public CustomItemStackHandler()
    {
        super(1);
    }

    public CustomItemStackHandler(int size)
    {
       super(size);
    }

    public CustomItemStackHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
    }

    public NonNullList<ItemStack> toNonNullList()
    {
        return this.stacks;
    }
}
