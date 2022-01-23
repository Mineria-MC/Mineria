package com.mineria.mod.common.blocks.apothecary_table;

import com.mineria.mod.common.effects.PoisonSource;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class ApothecaryTableInventoryWrapper extends RecipeWrapper
{
    @Nullable
    protected final PoisonSource poisonSource;

    public ApothecaryTableInventoryWrapper(IItemHandlerModifiable inv, @Nullable PoisonSource poisonSource)
    {
        super(inv);
        this.poisonSource = poisonSource;
    }

    @Nullable
    public PoisonSource getPoisonSource()
    {
        return poisonSource;
    }
}
