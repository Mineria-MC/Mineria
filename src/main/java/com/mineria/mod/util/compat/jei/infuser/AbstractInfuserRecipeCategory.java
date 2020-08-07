package com.mineria.mod.util.compat.jei.infuser;

import com.mineria.mod.References;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractInfuserRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T>
{
    protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/gui/infuser/infuser_gui.png");

    protected static final int input1 = 0;
    protected static final int barrel = 1;
    protected static final int fuel = 2;
    protected static final int output = 3;

    protected final IDrawableAnimated animatedBubles;
    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    public AbstractInfuserRecipeCategory(IGuiHelper helper)
    {
        staticFlame = helper.createDrawable(TEXTURES, 202, 0, 14, 14);
        animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 176, 0, 26, 13);
        animatedBubles = helper.createAnimatedDrawable(staticAnimation, 2400, IDrawableAnimated.StartDirection.LEFT, false);
    }
}
