package io.github.mineria_mc.mineria.client.compat.jei;

import com.google.common.collect.Lists;

import io.github.mineria_mc.mineria.client.compat.jei.recipes.categories.ExtractorRecipeCategory;
import io.github.mineria_mc.mineria.client.compat.jei.recipes.categories.TitaneExtractorRecipeCategory;
import io.github.mineria_mc.mineria.client.screens.ExtractorScreen;
import io.github.mineria_mc.mineria.client.screens.TitaneExtractorScreen;
import io.github.mineria_mc.mineria.common.containers.ExtractorMenu;
import io.github.mineria_mc.mineria.common.containers.TitaneExtractorMenu;
import io.github.mineria_mc.mineria.common.recipe.ExtractorRecipe;
import io.github.mineria_mc.mineria.common.recipe.TitaneExtractorRecipe;
import io.github.mineria_mc.mineria.common.registries.MineriaMenuTypesRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

public class MineriaJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Constants.MODID, "mineria_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        final IGuiHelper helpers = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
            new TitaneExtractorRecipeCategory(helpers),
            new ExtractorRecipeCategory(helpers)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(TitaneExtractorRecipeCategory.TYPE, Lists.newArrayList(TitaneExtractorRecipe.DEFAULT_RECIPE.get()));
        registration.addRecipes(ExtractorRecipeCategory.TYPE, Lists.newArrayList(ExtractorRecipe.DEFAULT_RECIPE.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(TitaneExtractorMenu.class, MineriaMenuTypesRegistry.TITANE_EXTRACTOR, TitaneExtractorRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ExtractorMenu.class, MineriaMenuTypesRegistry.EXTRACTOR, ExtractorRecipeCategory.TYPE, 0, 3, 10, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TitaneExtractorScreen.class, 74, 8, widthOf(MineriaUtil.translatable("tile_entity", "titane_extractor")), 8, TitaneExtractorRecipeCategory.TYPE);
        registration.addRecipeClickArea(ExtractorScreen.class, 50 - widthOf(MineriaUtil.translatable("tile_entity", "extractor")) / 2, 6, widthOf(MineriaUtil.translatable("tile_entity", "extractor")), 8, ExtractorRecipeCategory.TYPE);
    }

    @SuppressWarnings("resource")
    private static int widthOf(FormattedText text) {
        return Minecraft.getInstance().font.width(text);
    }
}
