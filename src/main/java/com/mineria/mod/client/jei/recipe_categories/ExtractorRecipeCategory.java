package com.mineria.mod.client.jei.recipe_categories;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.recipe.ExtractorRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;

public class ExtractorRecipeCategory implements IRecipeCategory<ExtractorRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "extractor");
    private static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/extractor.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animation;

    public ExtractorRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURES, 4, 5, 158, 108);
        this.icon = helper.createDrawableIngredient(new ItemStack(MineriaBlocks.EXTRACTOR));

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 177, 0, 40, 53);
        this.animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public ResourceLocation getUid()
    {
        return ID;
    }

    @Override
    public Class<ExtractorRecipe> getRecipeClass()
    {
        return ExtractorRecipe.class;
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("recipe_category.mineria.extractor");
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
        stacks.init(0, true, 8 - 5, 20 - 6);
        stacks.init(1, true, 43 - 5, 20 - 6);
        stacks.init(2, true, 25 - 5, 92 - 6);
        stacks.init(3, false, 137 - 5, 92 - 6);
        stacks.init(4, false, 137 - 5, 70 - 6);
        stacks.init(5, false, 137 - 5, 48 - 6);
        stacks.init(6, false, 137 - 5, 27 - 6);
        stacks.init(7, false, 102 - 5, 8 - 6);
        stacks.init(8, false, 70 - 5, 27 - 6);
        stacks.init(9, false, 70 - 5, 48 - 6);
        stacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if(!input)
            {
                float chance = recipe.getChance(ingredient);
                String chanceStr = chance == Math.ceil(chance) ? Integer.toString((int) chance) : Float.toString(chance);
                tooltip.add(new TextComponent(chanceStr.concat("% of Chance")).withStyle(ChatFormatting.GOLD));
            }
        });
        stacks.set(ingredients);
    }

    @Override
    public void draw(ExtractorRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY)
    {
        animation.draw(matrixStack, 13 - 4, 37 - 5);
    }
}
