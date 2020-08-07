package com.mineria.mod.util.compat.jei.titane_extractor;

import com.mineria.mod.References;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.compat.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class TitaneExtractorRecipeCategory extends AbstractTitaneExtractorRecipeCategory<TitaneExtractorRecipe>
{
	private static IDrawable icon;
	private final IDrawable backround;
	private final String name;
	
	public TitaneExtractorRecipeCategory(IGuiHelper helper)
	{
		super(helper);
		backround = helper.createDrawable(TEXTURES, 8, 4, 160, 90);
		icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.titane_extractor));
		name = "Titane Extractor";
	}
	
	@Override
	public IDrawable getBackground()
	{
		return backround;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft)
	{
		animation.draw(minecraft, 15 - 8, 24 - 4);
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public String getTitle()
	{
		return name;
	}
	
	@Override
	public String getModName()
	{
		return References.NAME;
	}
	
	@Override
	public String getUid()
	{
		return RecipeCategories.TITANE_EXTRACTOR;
	}
	
	public void setRecipe(IRecipeLayout recipeLayout, TitaneExtractorRecipe recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(input1, true, 9 - 8, 6 - 4);
		stacks.init(input2, true, 40 - 8, 6 - 4);
		stacks.init(filter, true, 23 - 8, 77 - 4);
		stacks.init(output, false, 94 - 8, 46 - 4);
		stacks.set(ingredients);
	};
}
