package com.mineria.mod.util.compat.jei.extractor;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mineria.mod.blocks.extractor.ExtractorRecipes;
import com.mineria.mod.init.ItemsInit;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ExtractorRecipeMaker
{
    public static List<ExtractorRecipe> getRecipes(IJeiHelpers helpers)
    {
        IStackHelper stackHelper = helpers.getStackHelper();
        ExtractorRecipes instance = ExtractorRecipes.getInstance();
        Table<ItemStack, ItemStack, Map<Integer, ItemStack>> recipes = instance.getTripleExtractingList();
        List<ExtractorRecipe> jeiRecipes = Lists.newArrayList();

        for(Map.Entry<ItemStack, Map<ItemStack, Map<Integer, ItemStack>>> entry : recipes.rowMap().entrySet())
        {
            for(Map.Entry<ItemStack, Map<Integer, ItemStack>> ent : entry.getValue().entrySet())
            {
                ItemStack input1 = entry.getKey();
                ItemStack input2 = ent.getKey();
                Map<Integer, ItemStack> outputs = ent.getValue();
                List<ItemStack> inputs = Lists.newArrayList(input1, input2, new ItemStack(ItemsInit.filter));
                ExtractorRecipe recipe = new ExtractorRecipe(inputs, outputs);
                jeiRecipes.add(recipe);
            }
        }
        return jeiRecipes;
    }
}
