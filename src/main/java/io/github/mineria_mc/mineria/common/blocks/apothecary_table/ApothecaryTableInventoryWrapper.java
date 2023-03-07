package io.github.mineria_mc.mineria.common.blocks.apothecary_table;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;

public class ApothecaryTableInventoryWrapper extends RecipeWrapper {
    @Nullable
    protected final PoisonSource poisonSource;

    public ApothecaryTableInventoryWrapper(IItemHandlerModifiable inv, @Nullable PoisonSource poisonSource) {
        super(inv);
        this.poisonSource = poisonSource;
    }

    @Nullable
    public PoisonSource getPoisonSource() {
        return poisonSource;
    }
}
