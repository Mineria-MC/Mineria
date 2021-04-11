package com.mineria.mod.util.jei.titane_extractor;

import com.mineria.mod.References;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.jei.RecipeCategories;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class TitaneExtractorRecipeCategory extends AbstractTitaneExtractorRecipeCategory<TitaneExtractorRecipe>
{
	private final IDrawable icon;
	private final IDrawable backround;
	private final String name;
	
	public TitaneExtractorRecipeCategory(IGuiHelper helper)
	{
		super(helper);
		backround = helper.createDrawable(TEXTURES, 8, 4, 160, 90);
		icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.TITANE_EXTRACTOR));
		name = I18n.format("container.titane_extractor");
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

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, TitaneExtractorRecipe recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, 9 - 8, 6 - 4);
		stacks.init(1, true, 40 - 8, 6 - 4);
		stacks.init(2, true, 23 - 8, 77 - 4);
		stacks.init(3, false, 94 - 8, 46 - 4);
		stacks.set(ingredients);
	};
}
