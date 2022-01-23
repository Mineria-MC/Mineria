package com.mineria.mod.common.recipe;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Util;

import java.util.*;

public class ExtractorRecipe
{
    public static final ExtractorRecipe DEFAULT_RECIPE = new ExtractorRecipe(Lists.newArrayList(
            new ItemStack(MineriaBlocks.MINERAL_SAND),
            new ItemStack(MineriaBlocks.WATER_BARREL),
            new ItemStack(MineriaItems.FILTER)
    ), Util.make(new HashMap<>(), map -> {
        map.put(800, new ItemStack(Items.IRON_INGOT));
        map.put(600, new ItemStack(MineriaItems.LEAD_INGOT));
        map.put(300, new ItemStack(MineriaItems.COPPER_INGOT));
        map.put(120, new ItemStack(MineriaItems.SILVER_INGOT));
        map.put(100, new ItemStack(Items.GOLD_INGOT));
        map.put(10, new ItemStack(Items.DIAMOND));
        map.put(1, new ItemStack(MineriaItems.LONSDALEITE));
    }));

    private final List<ItemStack> inputs;
    private final Map<Integer, ItemStack> outputs;

    public ExtractorRecipe(List<ItemStack> inputs, Map<Integer, ItemStack> outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Map<Integer, ItemStack> getOutputsMap()
    {
        return this.outputs;
    }

    public List<ItemStack> getOutputs()
    {
        List<Integer> ints = new ArrayList<>();
        outputs.forEach((key, value) -> ints.add(key));
        ints.sort(Comparator.reverseOrder());
        List<ItemStack> stacks = new ArrayList<>();

        ints.forEach(integer -> {
            stacks.add(ints.indexOf(integer), outputs.get(integer));
        });

        return stacks;
    }

    public float getChance(ItemStack stack)
    {
        for(Map.Entry<Integer, ItemStack> entry : outputs.entrySet())
        {
            if(entry.getValue().sameItem(stack))
            {
                return (float)entry.getKey() / 10;
            }
        }
        return 100.0F;
    }

    public List<ItemStack> getInputs()
    {
        return inputs;
    }
}
