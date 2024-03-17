package io.github.mineria_mc.mineria.client.compat.rei.displays;

import java.util.List;

import io.github.mineria_mc.mineria.client.compat.rei.categories.MineriaREITitaneExtractorCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

public class MineriaREITitaneExtractorDisplay extends BasicDisplay {

    public MineriaREITitaneExtractorDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MineriaREITitaneExtractorCategory.TITANE_EXTRACTOR;
    }
}
