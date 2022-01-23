package com.mineria.mod.client.jei.recipe_categories;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.recipe.DistillerRecipe;
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

public class DistillerRecipeCategory implements IRecipeCategory<DistillerRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "distiller");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/distiller.png");

    private final String name;
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedBubbles;

    public DistillerRecipeCategory(IGuiHelper helper)
    {
        this.name = I18n.get("recipe_category.mineria.distiller");
        this.background = helper.createDrawable(TEXTURE, 5, 6, 150, 64);
        this.icon = helper.createDrawableIngredient(new ItemStack(MineriaBlocks.DISTILLER));

        IDrawableStatic staticFlame = helper.createDrawable(TEXTURE, 202, 42, 14, 14);
        this.animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticBubbles = helper.createDrawable(TEXTURE, 176, 29, 26, 13);
        this.animatedBubbles = helper.createAnimatedDrawable(staticBubbles, 60, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid()
    {
        return ID;
    }

    @Override
    public Class<DistillerRecipe> getRecipeClass()
    {
        return DistillerRecipe.class;
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
    public void setIngredients(DistillerRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DistillerRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 10 - 5, 11 - 6);
        stacks.init(1, true, 10 - 5, 46 - 6);
        stacks.init(2, true, 54 - 5, 21 - 6);
        stacks.init(3, true, 86 - 5, 9 - 6);
        stacks.init(4, true, 86 - 5, 43 - 6);
        stacks.init(5, false, 132 - 5, 9 - 6);
        stacks.set(ingredients);
    }

    @Override
    public void draw(DistillerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        this.animatedFlame.draw(matrixStack, 87 - 5, 42 - 6);
        this.animatedBubbles.draw(matrixStack, 105 - 5, 11 - 6);
    }
}
