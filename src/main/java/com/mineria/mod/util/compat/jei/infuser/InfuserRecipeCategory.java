package com.mineria.mod.util.compat.jei.infuser;

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

import javax.annotation.Nullable;

public class InfuserRecipeCategory extends AbstractInfuserRecipeCategory<InfuserRecipe>
{
    private static IDrawable icon;
    private final IDrawable backround;
    private final String name;

    public InfuserRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        backround = helper.createDrawable(TEXTURES, 8, 4, 159, 62);
        icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.infuser));
        name = "Infuser";
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
        return backround;
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
        animatedBubles.draw(minecraft, 64 - 8, 36 - 4);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfuserRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(input1, true, 13 - 8, 9 - 4);
        stacks.init(barrel, true, 46 - 8, 34 - 4);
        stacks.init(fuel, true, 129 - 8, 34 - 4);
        stacks.init(output, false, 90 - 8, 34 - 4);
        stacks.set(ingredients);
    }
}
