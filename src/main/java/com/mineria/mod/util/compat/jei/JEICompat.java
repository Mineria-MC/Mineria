package com.mineria.mod.util.compat.jei;

//@JEIPlugin
public class JEICompat// implements IModPlugin
{
	/*
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

	 */
}
