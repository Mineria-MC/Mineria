package io.github.mineria_mc.mineria.client.compat.rei;

import io.github.mineria_mc.mineria.client.compat.rei.categories.MineriaREITitaneExtractorCategory;
import io.github.mineria_mc.mineria.client.screens.TitaneExtractorScreen;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class MineriaREIClientPlugin implements REIClientPlugin {
    
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MineriaREITitaneExtractorCategory());
        registry.addWorkstations(MineriaREITitaneExtractorCategory.TITANE_EXTRACTOR, EntryStacks.of(MineriaBlockRegistry.TITANE_EXTRACTOR));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(75, 30, 20, 30), TitaneExtractorScreen.class, MineriaREITitaneExtractorCategory.TITANE_EXTRACTOR);
    }
}
