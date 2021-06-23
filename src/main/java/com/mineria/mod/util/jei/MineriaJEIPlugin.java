package com.mineria.mod.util.jei;

import com.google.common.collect.Lists;
import com.mineria.mod.References;
import com.mineria.mod.blocks.extractor.ExtractorContainer;
import com.mineria.mod.blocks.extractor.ExtractorScreen;
import com.mineria.mod.blocks.infuser.InfuserContainer;
import com.mineria.mod.blocks.infuser.InfuserScreen;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorContainer;
import com.mineria.mod.blocks.titane_extractor.TitaneExtractorScreen;
import com.mineria.mod.init.RecipeSerializerInit;
import com.mineria.mod.recipe.InfuserRecipe;
import com.mineria.mod.util.MineriaUtils;
import com.mineria.mod.util.jei.extractor.ExtractorRecipe;
import com.mineria.mod.util.jei.extractor.ExtractorRecipeCategory;
import com.mineria.mod.util.jei.infuser.InfuserRecipeCategory;
import com.mineria.mod.util.jei.titane_extractor.TitaneExtractorRecipe;
import com.mineria.mod.util.jei.titane_extractor.TitaneExtractorRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

@JeiPlugin
public class MineriaJEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(References.MODID, "mineria_jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        final IGuiHelper helpers = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new TitaneExtractorRecipeCategory(helpers),
                new InfuserRecipeCategory(helpers),
                new ExtractorRecipeCategory(helpers)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        registration.addRecipes(Lists.newArrayList(TitaneExtractorRecipe.DEFAULT_RECIPE), RecipeCategories.TITANE_EXTRACTOR);
        registration.addRecipes(Objects.requireNonNull(MineriaUtils.<InfuserRecipe>findRecipesByType(RecipeSerializerInit.INFUSER_TYPE)), RecipeCategories.INFUSER);
        registration.addRecipes(Lists.newArrayList(ExtractorRecipe.DEFAULT_RECIPE), RecipeCategories.EXTRACTOR);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(TitaneExtractorContainer.class, RecipeCategories.TITANE_EXTRACTOR, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(InfuserContainer.class, RecipeCategories.INFUSER, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(ExtractorContainer.class, RecipeCategories.EXTRACTOR, 0, 3, 10, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(TitaneExtractorScreen.class, 77, 6, 85, 8, RecipeCategories.TITANE_EXTRACTOR);
        registration.addRecipeClickArea(InfuserScreen.class, 88, 5, 40, 8, RecipeCategories.INFUSER);
        registration.addRecipeClickArea(ExtractorScreen.class, 6, 5, 35, 8, RecipeCategories.EXTRACTOR);
    }
}
