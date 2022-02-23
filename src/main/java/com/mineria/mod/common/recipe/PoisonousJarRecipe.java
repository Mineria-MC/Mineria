package com.mineria.mod.common.recipe;

import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.items.JarItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PoisonousJarRecipe extends AbstractApothecaryTableRecipe
{
    public PoisonousJarRecipe(ResourceLocation id)
    {
        super(id, Ingredient.of(MineriaItems.JAR));
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, Level world)
    {
        return this.input.test(wrapper.getItem(1)) && wrapper.getPoisonSource() != null;
    }

    @Override
    public ItemStack assemble(ApothecaryTableInventoryWrapper wrapper)
    {
        PoisonSource poisonSource = wrapper.getPoisonSource();
        ItemStack result = wrapper.getItem(1).copy();
        return poisonSource == null ? result : JarItem.addPoisonSourceToStack(result, poisonSource);
    }

    @Override
    public ItemStack getResultItem()
    {
        return new ItemStack(MineriaItems.JAR);
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.POISONOUS_JAR.get();
    }

    @Override
    public boolean renderInJEI()
    {
        return false;
    }
}
