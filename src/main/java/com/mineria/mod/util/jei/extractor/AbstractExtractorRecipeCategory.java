package com.mineria.mod.util.jei.extractor;

import com.mineria.mod.References;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractExtractorRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/extractor.png");

    protected final IDrawableAnimated animation;

    public AbstractExtractorRecipeCategory(IGuiHelper helper)
    {
        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 214, 0, 40, 53);
        animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
    }
}
