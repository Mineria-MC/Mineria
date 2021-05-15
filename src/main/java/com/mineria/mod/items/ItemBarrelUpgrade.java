package com.mineria.mod.items;

import com.mineria.mod.blocks.barrel.diamond.TileEntityDiamondFluidBarrel;

import java.util.function.Consumer;

public class ItemBarrelUpgrade extends ItemMineria
{
    private final Consumer<TileEntityDiamondFluidBarrel> handler;

    public ItemBarrelUpgrade(Consumer<TileEntityDiamondFluidBarrel> handler)
    {
        super(new Builder().setMaxStackSize(1));
        this.handler = handler;
    }

    public void applyUpgrade(TileEntityDiamondFluidBarrel tile)
    {
        this.handler.accept(tile);
    }
}
