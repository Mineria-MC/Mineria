package com.mineria.mod.util.jei.titane_extractor;

import com.mineria.mod.References;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTitaneExtractorRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
	protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/titane_extractor.png");
	protected final IDrawableAnimated animation;
	
	public AbstractTitaneExtractorRecipeCategory(IGuiHelper helper)
	{
		IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 201, 0, 36, 53);
		animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
	}
}
