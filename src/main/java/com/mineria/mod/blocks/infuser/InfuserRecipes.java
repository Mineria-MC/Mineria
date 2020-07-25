package com.mineria.mod.blocks.infuser;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.init.ItemsInit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class InfuserRecipes
{
    private static final InfuserRecipes EXTRACTING_BASE = new InfuserRecipes();

    private final Table<ItemStack, ItemStack, ItemStack> infuseList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();

    public static InfuserRecipes instance()
    {
        return EXTRACTING_BASE;
    }

    private InfuserRecipes()
    {
        addExtractingRecipe(new ItemStack(BlocksInit.plantain), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.plantain_tea));
        addExtractingRecipe(new ItemStack(BlocksInit.mint), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.mint_tea));
        addExtractingRecipe(new ItemStack(BlocksInit.thyme), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.thyme_tea));
        addExtractingRecipe(new ItemStack(BlocksInit.nettle), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.nettle_tea));
        addExtractingRecipe(new ItemStack(BlocksInit.pulmonary), new ItemStack(Items.WATER_BUCKET), new ItemStack(ItemsInit.pulmonary_tea));
    }

    public void addExtractingRecipe(ItemStack input, ItemStack input2, ItemStack stack)
    {
        if (getInfusingResult(input, input2) != ItemStack.EMPTY) return;
        this.infuseList.put(input, input2, stack);
    }

    public ItemStack getInfusingResult(ItemStack input1, ItemStack input2)
    {
        for(Map.Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.infuseList.columnMap().entrySet())
        {
            if(this.compareItemStacks(input2, (ItemStack)entry.getKey()))
            {
                for(Map.Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet())
                {
                    if(this.compareItemStacks(input1, (ItemStack)ent.getKey()))
                    {
                        return (ItemStack)ent.getValue();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Table<ItemStack, ItemStack, ItemStack> getDualInfuseList()
    {
        return this.infuseList;
    }
}
