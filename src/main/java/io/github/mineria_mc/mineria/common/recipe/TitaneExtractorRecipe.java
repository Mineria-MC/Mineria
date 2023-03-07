package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public record TitaneExtractorRecipe(ExtractorRecipe.Inputs inputs, ItemStack output) {
    public static final Lazy<TitaneExtractorRecipe> DEFAULT_RECIPE = Lazy.of(() -> new TitaneExtractorRecipe(
            new ExtractorRecipe.Inputs(new ItemStack(MineriaBlocks.MINERAL_SAND.get()), new ItemStack(MineriaBlocks.WATER_BARREL.get()), new ItemStack(MineriaItems.FILTER.get())),
            new ItemStack(MineriaItems.TITANE_NUGGET.get()))
    );
}
