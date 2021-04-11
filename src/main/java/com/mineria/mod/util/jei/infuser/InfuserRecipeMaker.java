package com.mineria.mod.util.jei.infuser;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mineria.mod.blocks.infuser.InfuserRecipes;
import com.mineria.mod.init.BlocksInit;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class InfuserRecipeMaker
{
    public static List<InfuserRecipe> getRecipes(IJeiHelpers helpers)
    {
        Map<ItemStack, ItemStack> recipes = InfuserRecipes.getInstance().getDualInfuseList();
        List<InfuserRecipe> jeiRecipes = Lists.newArrayList();

        for(Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet())
        {
            List<ItemStack> inputs = Lists.newArrayList(entry.getKey(), Item.getItemFromBlock(BlocksInit.WATER_BARREL).getDefaultInstance(), new ItemStack(Items.COAL));
            jeiRecipes.add(new InfuserRecipe(inputs, entry.getValue()));
        }

        return jeiRecipes;
    }
}
