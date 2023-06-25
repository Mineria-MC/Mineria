package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import io.github.mineria_mc.mineria.common.items.JarItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class PoisonousJarRecipe extends AbstractApothecaryTableRecipe {
    public PoisonousJarRecipe(ResourceLocation id) {
        super(id, Ingredient.of(MineriaItems.JAR.get()));
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, Level world) {
        return this.input.test(wrapper.getItem(1)) && wrapper.getPoisonSource() != null;
    }

    @Nonnull
    @Override
    public ItemStack assemble(ApothecaryTableInventoryWrapper wrapper, @Nonnull RegistryAccess access) {
        PoisonSource poisonSource = wrapper.getPoisonSource();
        ItemStack result = wrapper.getItem(1).copy();
        return poisonSource == null ? result : JarItem.addPoisonSourceToStack(result, poisonSource);
    }

    @Nonnull
    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(MineriaItems.JAR.get());
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.POISONOUS_JAR.get();
    }

    @Override
    public boolean renderInJEI() {
        return false;
    }
}
