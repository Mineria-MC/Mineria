package com.mineria.mod.blocks.infuser;

public class InfuserRecipes
{
    /*
    private static final InfuserRecipes INSTANCE = new InfuserRecipes();

    private final Table<ItemStack, ItemStack, ItemStack> infuseList = HashBasedTable.create();

    public static InfuserRecipes getInstance()
    {
        return INSTANCE;
    }

    private InfuserRecipes()
    {
        addInfusingRecipe(new ItemStack(BlocksInit.plantain), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.plantain_tea));
        addInfusingRecipe(new ItemStack(BlocksInit.mint), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.mint_tea));
        addInfusingRecipe(new ItemStack(BlocksInit.thyme), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.thyme_tea));
        addInfusingRecipe(new ItemStack(BlocksInit.nettle), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.nettle_tea));
        addInfusingRecipe(new ItemStack(BlocksInit.pulmonary), new ItemStack(BlocksInit.water_barrel), new ItemStack(ItemsInit.pulmonary_tea));
    }

    public void addInfusingRecipe(ItemStack input, ItemStack input2, ItemStack stack)
    {
        if (getInfusingResult(input, input2) != ItemStack.EMPTY) return;
        this.infuseList.put(input, input2, stack);
    }

    public ItemStack getInfusingResult(ItemStack input1, ItemStack input2)
    {
        for(Map.Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.infuseList.rowMap().entrySet())
        {
            if(this.compareItemStacks(input1, entry.getKey()))
            {
                for(Map.Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet())
                {
                    if(this.compareItemStacks(input2, ent.getKey()))
                    {
                        return ent.getValue();
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

     */
}
