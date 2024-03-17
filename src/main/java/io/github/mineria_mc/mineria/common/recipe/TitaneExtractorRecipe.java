package io.github.mineria_mc.mineria.common.recipe;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.common.registries.MineriaItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record TitaneExtractorRecipe(ExtractorRecipe.Inputs inputs, ItemStack output) {
    public static final Supplier<TitaneExtractorRecipe> DEFAULT_RECIPE = Suppliers.memoize(() -> new TitaneExtractorRecipe(
            new ExtractorRecipe.Inputs(new ItemStack(MineriaBlockRegistry.MINERAL_SAND), new ItemStack(Items.WATER_BUCKET), new ItemStack(MineriaItemRegistry.FILTER)),
            new ItemStack(MineriaItemRegistry.TITANE_NUGGET))
    );
}
