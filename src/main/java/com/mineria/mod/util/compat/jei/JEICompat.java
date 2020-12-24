package com.mineria.mod.util.compat.jei;

import com.mineria.mod.blocks.extractor.ContainerExtractor;
import com.mineria.mod.blocks.extractor.GuiExtractor;
import com.mineria.mod.blocks.infuser.ContainerInfuser;
import com.mineria.mod.blocks.infuser.GuiInfuser;
import com.mineria.mod.blocks.titane_extractor.ContainerTitaneExtractor;
import com.mineria.mod.blocks.titane_extractor.GuiTitaneExtractor;
import com.mineria.mod.util.compat.jei.extractor.ExtractorRecipeCategory;
import com.mineria.mod.util.compat.jei.extractor.ExtractorRecipeMaker;
import com.mineria.mod.util.compat.jei.infuser.InfuserRecipeCategory;
import com.mineria.mod.util.compat.jei.infuser.InfuserRecipeMaker;
import com.mineria.mod.util.compat.jei.titane_extractor.TitaneExtractorRecipeCategory;
import com.mineria.mod.util.compat.jei.titane_extractor.TitaneExtractorRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.util.text.translation.I18n;

import java.util.IllegalFormatException;

@JEIPlugin
public class JEICompat implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		final IJeiHelpers helpers = registry.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		registry.addRecipeCategories(new TitaneExtractorRecipeCategory(gui));
		registry.addRecipeCategories(new InfuserRecipeCategory(gui));
		registry.addRecipeCategories(new ExtractorRecipeCategory(gui));
	}
	
	@Override
	public void register(IModRegistry registry)
	{
		final IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IRecipeTransferRegistry recipeTransfer = registry.getRecipeTransferRegistry();
		
		registry.addRecipes(TitaneExtractorRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.TITANE_EXTRACTOR);
		registry.addRecipeClickArea(GuiTitaneExtractor.class, 77, 6, 85, 8, RecipeCategories.TITANE_EXTRACTOR);
		registry.addRecipes(InfuserRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.INFUSER);
		registry.addRecipeClickArea(GuiInfuser.class, 88, 5, 40, 8, RecipeCategories.INFUSER);
		registry.addRecipes(ExtractorRecipeMaker.getRecipes(jeiHelpers), RecipeCategories.EXTRACTOR);
		registry.addRecipeClickArea(GuiExtractor.class, 6, 5, 35, 8, RecipeCategories.EXTRACTOR);
		recipeTransfer.addRecipeTransferHandler(ContainerTitaneExtractor.class, RecipeCategories.TITANE_EXTRACTOR, 0, 3, 4, 36);
		recipeTransfer.addRecipeTransferHandler(ContainerInfuser.class, RecipeCategories.INFUSER, 0, 3, 4, 36);
		recipeTransfer.addRecipeTransferHandler(ContainerExtractor.class, RecipeCategories.EXTRACTOR, 0, 3, 10, 36);
	}
	
	public static String translateToLocal(String key)
	{
		if(I18n.canTranslate(key)) return I18n.translateToLocal(key);
		else return I18n.translateToFallback(key);
	}
	
	public static String translateToLocalFormatted(String key, Object... format)
	{
		String s = translateToLocal(key);
		try
		{
			return String.format(s, format);
		}
		catch(IllegalFormatException e)
		{
			return "Format error: " + s;
		}
	}
}
