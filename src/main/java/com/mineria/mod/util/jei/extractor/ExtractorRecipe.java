package com.mineria.mod.util.jei.extractor;

import com.google.common.collect.Lists;
import com.mineria.mod.blocks.extractor.TileEntityExtractor;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExtractorRecipe implements IRecipeWrapper
{
    public static final ExtractorRecipe DEFAULT_RECIPE = new ExtractorRecipe(Lists.newArrayList(
            new ItemStack(BlocksInit.MINERAL_SAND), Item.getItemFromBlock(BlocksInit.WATER_BARREL).getDefaultInstance(), new ItemStack(ItemsInit.FILTER)),
            TileEntityExtractor.RECIPE_OUTPUTS);

    private final List<ItemStack> inputs;
    private final Map<Integer, ItemStack> outputs;

    public ExtractorRecipe(List<ItemStack> inputs, Map<Integer, ItemStack> outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public List<ItemStack> getOutputs()
    {
        List<Integer> ints = new ArrayList<>(outputs.keySet());
        ints.sort(Comparator.reverseOrder());
        List<ItemStack> stacks = new ArrayList<>();

        ints.forEach(integer -> stacks.add(ints.indexOf(integer), outputs.get(integer)));

        return stacks;
    }

    public float getChance(ItemStack stack)
    {
        for(Map.Entry<Integer, ItemStack> entry : outputs.entrySet())
        {
            if(entry.getValue().isItemEqual(stack))
            {
                return (float)entry.getKey() / 10;
            }
        }
        return 100.0F;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }
}
