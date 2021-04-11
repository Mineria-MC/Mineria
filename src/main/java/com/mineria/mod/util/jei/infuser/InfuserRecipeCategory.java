package com.mineria.mod.util.jei.infuser;

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

import javax.annotation.Nullable;

public class InfuserRecipeCategory extends AbstractInfuserRecipeCategory<InfuserRecipe>
{
    private final IDrawable icon;
    private final IDrawable background;
    private final String name;

    public InfuserRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        background = helper.createDrawable(TEXTURES, 8, 4, 159, 62);
        icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.INFUSER));
        name = I18n.format("container.infuser");
    }

    @Override
    public String getUid()
    {
        return RecipeCategories.INFUSER;
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
    public IDrawable getBackground()
    {
        return background;
    }

    @Nullable
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        animatedFlame.draw(minecraft, 149 - 8, 36 - 4);
        animatedBubbles.draw(minecraft, 64 - 8, 36 - 4);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfuserRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 13 - 8, 9 - 4);
        stacks.init(1, true, 46 - 8, 34 - 4);
        stacks.init(2, true, 129 - 8, 34 - 4);
        stacks.init(3, false, 90 - 8, 34 - 4);
        stacks.set(ingredients);
    }
}
