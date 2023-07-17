package io.github.mineria_mc.mineria.common.recipe;

import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.Lazy;

import java.util.*;

public class ExtractorRecipe {
    public static final Lazy<ExtractorRecipe> DEFAULT_RECIPE = Lazy.of(() -> new ExtractorRecipe(new Inputs(
            new ItemStack(MineriaBlocks.MINERAL_SAND.get()),
            new ItemStack(MineriaBlocks.WATER_BARREL.get()),
            new ItemStack(MineriaItems.FILTER.get())
    ), Util.make(new HashMap<>(), map -> {
        map.put(800, new ItemStack(Items.IRON_INGOT));
        map.put(600, new ItemStack(MineriaItems.LEAD_INGOT.get()));
        map.put(300, new ItemStack(Items.COPPER_INGOT));
        map.put(120, new ItemStack(MineriaItems.SILVER_INGOT.get()));
        map.put(100, new ItemStack(Items.GOLD_INGOT));
        map.put(10, new ItemStack(Items.DIAMOND));
        map.put(1, new ItemStack(MineriaItems.LONSDALEITE.get()));
    })));

    private final Inputs inputs;
    private final Map<Integer, ItemStack> outputs;

    public ExtractorRecipe(Inputs inputs, Map<Integer, ItemStack> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Inputs inputs() {
        return inputs;
    }

    public Map<Integer, ItemStack> getOutputsMap() {
        return this.outputs;
    }

    public List<ItemStack> outputs() {
        List<Integer> ints = new ArrayList<>();
        outputs.forEach((key, value) -> ints.add(key));
        ints.sort(Comparator.reverseOrder());
        List<ItemStack> stacks = new ArrayList<>();

        ints.forEach(integer -> stacks.add(ints.indexOf(integer), outputs.get(integer)));

        return stacks;
    }

    public float chance(ItemStack stack) {
        for (Map.Entry<Integer, ItemStack> entry : outputs.entrySet()) {
            if (ItemStack.isSameItem(entry.getValue(), stack)) {
                return (float) entry.getKey() / 10;
            }
        }
        return 100.0F;
    }

    public record Inputs(ItemStack input1, ItemStack input2, ItemStack filter) {
    }
}
