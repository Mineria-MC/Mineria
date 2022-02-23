package com.mineria.mod.common.recipe;

import com.google.common.collect.Lists;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.init.MineriaItems;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TitaneExtractorRecipe
{
    public static final TitaneExtractorRecipe DEFAULT_RECIPE = new TitaneExtractorRecipe(Lists.newArrayList(new ItemStack(MineriaBlocks.MINERAL_SAND), new ItemStack(MineriaBlocks.WATER_BARREL), new ItemStack(MineriaItems.FILTER)), new ItemStack(MineriaItems.TITANE_NUGGET));

	private final List<ItemStack> inputs;
	private final ItemStack output;
	
	public TitaneExtractorRecipe(List<ItemStack> inputs, ItemStack output)
	{
		this.inputs = inputs;
		this.output = output;
	}

    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    public ItemStack getOutput()
    {
        return output;
    }
}
