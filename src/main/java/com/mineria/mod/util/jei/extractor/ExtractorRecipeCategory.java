package com.mineria.mod.util.jei.extractor;

import com.mineria.mod.References;
import com.mineria.mod.init.BlocksInit;
import com.mineria.mod.util.jei.RecipeCategories;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ExtractorRecipeCategory implements IRecipeCategory<ExtractorRecipe>
{
    protected static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/extractor.png");

    private final String name;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animation;

    public ExtractorRecipeCategory(IGuiHelper helper)
    {
        this.name = I18n.format("recipe_category.mineria.extractor");
        this.background = helper.createDrawable(TEXTURES, 3, 4, 206, 105);
        this.icon = helper.createDrawableIngredient(new ItemStack(BlocksInit.EXTRACTOR));

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 214, 0, 40, 53);
        this.animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public ResourceLocation getUid()
    {
        return RecipeCategories.EXTRACTOR;
    }

    @Override
    public Class<ExtractorRecipe> getRecipeClass()
    {
        return ExtractorRecipe.class;
    }

    @Override
    public String getTitle()
    {
        return name;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setIngredients(ExtractorRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtractorRecipe recipe, IIngredients ingredients)
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
                tooltip.add(new StringTextComponent(recipe.getChance(ingredient) + "% of Chance").mergeStyle(TextFormatting.GOLD));
            }
        });
        stacks.set(ingredients);
    }

    @Override
    public void draw(ExtractorRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        animation.draw(matrixStack, 11 - 3, 35 - 4);
    }
}
