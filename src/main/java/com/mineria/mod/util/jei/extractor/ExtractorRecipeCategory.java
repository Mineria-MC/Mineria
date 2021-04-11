package com.mineria.mod.util.jei.extractor;

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
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class ExtractorRecipeCategory extends AbstractExtractorRecipeCategory<ExtractorRecipe>
{
    private final IDrawable icon;
    private final IDrawable background;
    private final String name;

    public ExtractorRecipeCategory(IGuiHelper helper)
    {
        super(helper);
        background = helper.createDrawable(TEXTURES, 3, 4, 206, 105);
        icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.EXTRACTOR));
        name = I18n.format("container.extractor");
    }

    @Override
    public String getUid()
    {
        return RecipeCategories.EXTRACTOR;
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
        animation.draw(minecraft, 11 - 3, 35 - 4);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtractorRecipe recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 6 - 4, 18 - 5);
        stacks.init(1, true, 41 - 4, 18 - 5);
        stacks.init(2, true, 23 - 4, 90 - 5);
        stacks.init(3, false, 190 - 4, 90 - 5);
        stacks.init(4, false, 190 - 4, 68 - 5);
        stacks.init(5, false, 190 - 4, 46 - 5);
        stacks.init(6, false, 190 - 4, 25 - 5);
        stacks.init(7, false, 120 - 4, 6 - 5);
        stacks.init(8, false, 68 - 4, 25 - 5);
        stacks.init(9, false, 68 - 4, 46 - 5);
        stacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) ->
        {
            if(!input)
            {
                tooltip.add(TextFormatting.GOLD + Float.toString(recipeWrapper.getChance(ingredient)) + "% of Chance");
            }
        });
        stacks.set(ingredients);
    }
}
