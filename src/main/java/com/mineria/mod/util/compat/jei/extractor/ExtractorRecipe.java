package com.mineria.mod.util.compat.jei.extractor;

public class ExtractorRecipe// implements IRecipeWrapper
{
    /*
    private final List<ItemStack> inputs;
    private final Map<Integer, ItemStack> outputs;

    public ExtractorRecipe(List<ItemStack> inputs, Map<Integer, ItemStack> outputs)
    {
        this.inputs = inputs;
        this.outputs = outputs;
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

     */
}
