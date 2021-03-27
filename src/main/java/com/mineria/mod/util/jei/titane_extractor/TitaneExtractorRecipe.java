package com.mineria.mod.util.jei.titane_extractor;

import com.google.common.collect.Lists;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TitaneExtractorRecipe
{
    public static final TitaneExtractorRecipe DEFAULT_RECIPE = new TitaneExtractorRecipe(Lists.newArrayList(new ItemStack(BlocksInit.MINERAL_SAND), new ItemStack(BlocksInit.WATER_BARREL), new ItemStack(ItemsInit.FILTER)), new ItemStack(ItemsInit.TITANE_NUGGET));

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
