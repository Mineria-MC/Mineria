package com.mineria.mod.util.jei;

import com.google.common.collect.Lists;
import com.mineria.mod.blocks.extractor.ContainerExtractor;
import com.mineria.mod.blocks.extractor.GuiExtractor;
import com.mineria.mod.blocks.infuser.ContainerInfuser;
import com.mineria.mod.blocks.infuser.GuiInfuser;
import com.mineria.mod.blocks.titane_extractor.ContainerTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.GuiTitaneExtractor;
import com.mineria.mod.util.jei.extractor.ExtractorRecipe;
import com.mineria.mod.util.jei.extractor.ExtractorRecipeCategory;
import com.mineria.mod.util.jei.infuser.InfuserRecipeCategory;
import com.mineria.mod.util.jei.infuser.InfuserRecipeMaker;
import com.mineria.mod.util.jei.titane_extractor.TitaneExtractorRecipe;
import com.mineria.mod.util.jei.titane_extractor.TitaneExtractorRecipeCategory;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class JEICompat implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		final IGuiHelper helpers = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new TitaneExtractorRecipeCategory(helpers));
		registry.addRecipeCategories(new InfuserRecipeCategory(helpers));
		registry.addRecipeCategories(new ExtractorRecipeCategory(helpers));
	}
	
	@Override
	public void register(IModRegistry registry)
	{
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();
		
		registry.addRecipes(Lists.newArrayList(TitaneExtractorRecipe.DEFAULT_RECIPE), RecipeCategories.TITANE_EXTRACTOR);
		registry.addRecipeClickArea(GuiTitaneExtractor.class, 77, 6, 85, 8, RecipeCategories.TITANE_EXTRACTOR);
		registry.addRecipes(InfuserRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.INFUSER);
		registry.addRecipeClickArea(GuiInfuser.class, 88, 5, 40, 8, RecipeCategories.INFUSER);
		registry.addRecipes(Lists.newArrayList(ExtractorRecipe.DEFAULT_RECIPE), RecipeCategories.EXTRACTOR);
		registry.addRecipeClickArea(GuiExtractor.class, 6, 5, 35, 8, RecipeCategories.EXTRACTOR);
		recipeTransfer.addRecipeTransferHandler(ContainerTitaneExtractor.class, RecipeCategories.TITANE_EXTRACTOR, 0, 3, 4, 36);
		recipeTransfer.addRecipeTransferHandler(ContainerInfuser.class, RecipeCategories.INFUSER, 0, 3, 4, 36);
		recipeTransfer.addRecipeTransferHandler(ContainerExtractor.class, RecipeCategories.EXTRACTOR, 0, 3, 10, 36);
	}
}
