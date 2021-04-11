package com.mineria.mod.blocks.infuser;

import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InfuserRecipes
{
    private static final InfuserRecipes INSTANCE = new InfuserRecipes();

    private final Map<ItemStack, ItemStack> infuseList = new HashMap<>();

    public static InfuserRecipes getInstance()
    {
        return INSTANCE;
    }

    private InfuserRecipes()
    {
        addInfusingRecipe(new ItemStack(BlocksInit.PLANTAIN), new ItemStack(ItemsInit.PLANTAIN_TEA));
        addInfusingRecipe(new ItemStack(BlocksInit.MINT), new ItemStack(ItemsInit.MINT_TEA));
        addInfusingRecipe(new ItemStack(BlocksInit.THYME), new ItemStack(ItemsInit.THYME_TEA));
        addInfusingRecipe(new ItemStack(BlocksInit.NETTLE), new ItemStack(ItemsInit.NETTLE_TEA));
        addInfusingRecipe(new ItemStack(BlocksInit.PULMONARY), new ItemStack(ItemsInit.PULMONARY_TEA));
    }

    public void addInfusingRecipe(ItemStack input, ItemStack result)
    {
        if (getInfusingResult(input) != ItemStack.EMPTY) return;
        this.infuseList.put(input, result);
    }

    public ItemStack getInfusingResult(ItemStack input)
    {
        for(Map.Entry<ItemStack, ItemStack> entry : infuseList.entrySet())
        {
            if(compareItemStacks(input, entry.getKey()))
            {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getDualInfuseList()
    {
        return this.infuseList;
    }
}
