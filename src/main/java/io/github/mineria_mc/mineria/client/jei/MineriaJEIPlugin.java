package io.github.mineria_mc.mineria.client.jei;

import com.google.common.collect.Lists;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.jei.recipe_categories.*;
import io.github.mineria_mc.mineria.client.screens.*;
import io.github.mineria_mc.mineria.common.containers.*;
import io.github.mineria_mc.mineria.common.init.MineriaMenuTypes;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.common.recipe.AbstractApothecaryTableRecipe;
import io.github.mineria_mc.mineria.common.recipe.ExtractorRecipe;
import io.github.mineria_mc.mineria.common.recipe.TitaneExtractorRecipe;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Our {@link JeiPlugin}, from JEI's API.
 */
@SuppressWarnings("unused")
@JeiPlugin
public class MineriaJEIPlugin implements IModPlugin {
    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Mineria.MODID, "mineria_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
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
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(TitaneExtractorRecipeCategory.TYPE, Lists.newArrayList(TitaneExtractorRecipe.DEFAULT_RECIPE.get()));
        registration.addRecipes(InfuserRecipeCategory.TYPE, MineriaUtils.findRecipesByType(MineriaRecipeTypes.INFUSER_TYPE.get()));
        registration.addRecipes(ExtractorRecipeCategory.TYPE, Lists.newArrayList(ExtractorRecipe.DEFAULT_RECIPE.get()));
        registration.addRecipes(DistillerRecipeCategory.TYPE, MineriaUtils.findRecipesByType(MineriaRecipeTypes.DISTILLER_TYPE.get()));
        registration.addRecipes(ApothecaryTableRecipeCategory.TYPE, MineriaUtils.findRecipesByType(MineriaRecipeTypes.APOTHECARY_TABLE_TYPE.get(), AbstractApothecaryTableRecipe::renderInJEI));
    }

    // TODO: fix recipe transfer handler for infuser
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(TitaneExtractorMenu.class, MineriaMenuTypes.TITANE_EXTRACTOR.get(), TitaneExtractorRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(InfuserMenu.class, MineriaMenuTypes.INFUSER.get(), InfuserRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ExtractorMenu.class, MineriaMenuTypes.EXTRACTOR.get(), ExtractorRecipeCategory.TYPE, 0, 3, 10, 36);
        registration.addRecipeTransferHandler(DistillerMenu.class, MineriaMenuTypes.DISTILLER.get(), DistillerRecipeCategory.TYPE, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ApothecaryTableMenu.class, MineriaMenuTypes.APOTHECARY_TABLE.get(), ApothecaryTableRecipeCategory.TYPE, 1, 1, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TitaneExtractorScreen.class, 74, 8, widthOf(Component.translatable("tile_entity.mineria.titane_extractor")), 8, TitaneExtractorRecipeCategory.TYPE);
        registration.addRecipeClickArea(InfuserScreen.class, (176 - widthOf(Component.translatable("tile_entity.mineria.infuser"))) / 2, 5, widthOf(Component.translatable("tile_entity.mineria.infuser")), 8, InfuserRecipeCategory.TYPE);
        registration.addRecipeClickArea(ExtractorScreen.class, 50 - widthOf(Component.translatable("tile_entity.mineria.extractor")) / 2, 6, widthOf(Component.translatable("tile_entity.mineria.extractor")), 8, ExtractorRecipeCategory.TYPE);
        registration.addRecipeClickArea(DistillerScreen.class, 34, 5, widthOf(Component.translatable("tile_entity.mineria.distiller")), 8, DistillerRecipeCategory.TYPE);
        registration.addRecipeClickArea(ApothecaryTableScreen.class, 88 - widthOf(Component.translatable("tile_entity.mineria.apothecary_table")) / 2, 5, widthOf(Component.translatable("tile_entity.mineria.apothecary_table")), 8, ApothecaryTableRecipeCategory.TYPE);
    }

    private static int widthOf(FormattedText text) {
        return Minecraft.getInstance().font.width(text);
    }
}
