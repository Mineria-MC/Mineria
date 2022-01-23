package com.mineria.mod.client.jei;

import com.google.common.collect.Lists;
import com.mineria.mod.Mineria;
import com.mineria.mod.client.jei.recipe_categories.*;
import com.mineria.mod.client.screens.*;
import com.mineria.mod.common.containers.*;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import com.mineria.mod.common.recipe.AbstractApothecaryTableRecipe;
import com.mineria.mod.common.recipe.ExtractorRecipe;
import com.mineria.mod.common.recipe.TitaneExtractorRecipe;
import com.mineria.mod.util.MineriaUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * Our {@link JeiPlugin}, from JEI's API.
 */
@SuppressWarnings("unused")
@JeiPlugin
public class MineriaJEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Mineria.MODID, "mineria_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        final IGuiHelper helpers = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new TitaneExtractorRecipeCategory(helpers),
                new InfuserRecipeCategory(helpers),
                new ExtractorRecipeCategory(helpers),
                new DistillerRecipeCategory(helpers),
                new ApothecaryTableRecipeCategory(helpers)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(Lists.newArrayList(TitaneExtractorRecipe.DEFAULT_RECIPE), TitaneExtractorRecipeCategory.ID);
        registration.addRecipes(MineriaUtils.findRecipesByType(MineriaRecipeSerializers.INFUSER_TYPE), InfuserRecipeCategory.ID);
        registration.addRecipes(Lists.newArrayList(ExtractorRecipe.DEFAULT_RECIPE), ExtractorRecipeCategory.ID);
        registration.addRecipes(MineriaUtils.findRecipesByType(MineriaRecipeSerializers.DISTILLER_TYPE), DistillerRecipeCategory.ID);
        registration.addRecipes(MineriaUtils.findRecipesByType(MineriaRecipeSerializers.APOTHECARY_TABLE_TYPE, AbstractApothecaryTableRecipe::renderInJEI), ApothecaryTableRecipeCategory.ID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(TitaneExtractorContainer.class, TitaneExtractorRecipeCategory.ID, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(InfuserContainer.class, InfuserRecipeCategory.ID, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ExtractorContainer.class, ExtractorRecipeCategory.ID, 0, 3, 10, 36);
        registration.addRecipeTransferHandler(DistillerContainer.class, DistillerRecipeCategory.ID, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ApothecaryTableContainer.class, ApothecaryTableRecipeCategory.ID, 1, 1, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(TitaneExtractorScreen.class, 74, 8, getFontRenderer(font -> font.width(I18n.get("tile_entity.mineria.titane_extractor")), 30), 8, TitaneExtractorRecipeCategory.ID);
        registration.addRecipeClickArea(InfuserScreen.class, 40, 5, getFontRenderer(font -> font.width(I18n.get("tile_entity.mineria.infuser")), 40), 8, InfuserRecipeCategory.ID);
        registration.addRecipeClickArea(ExtractorScreen.class, getFontRenderer(font -> 50 - font.width(I18n.get("tile_entity.mineria.extractor")) / 2, 28), 6, getFontRenderer(font -> font.width(I18n.get("tile_entity.mineria.extractor")), 30), 8, ExtractorRecipeCategory.ID);
        registration.addRecipeClickArea(DistillerScreen.class, 34, 5, getFontRenderer(font -> font.width(I18n.get("tile_entity.mineria.distiller")), 40), 8, DistillerRecipeCategory.ID);
        registration.addRecipeClickArea(ApothecaryTableScreen.class, getFontRenderer(font -> 88 - font.width(I18n.get("tile_entity.mineria.apothecary_table")) / 2, 58), 5, getFontRenderer(font -> font.width(I18n.get("tile_entity.mineria.apothecary_table")), 60), 8, ApothecaryTableRecipeCategory.ID);
    }

    private static int getFontRenderer(Function<FontRenderer, Integer> getter, int defaultValue)
    {
        FontRenderer font = Minecraft.getInstance().font;
        return font == null ? defaultValue : getter.apply(font);
    }
}
