package com.mineria.mod.client.jei.recipe_categories;

import com.google.common.collect.Lists;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.recipe.InfuserRecipe;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;

public class InfuserRecipeCategory implements IRecipeCategory<InfuserRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "infuser");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/infuser_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedBubbles;

    public InfuserRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 8, 4, 159, 62);
        this.icon = helper.createDrawableIngredient(new ItemStack(MineriaBlocks.INFUSER));

        IDrawableStatic staticFlame = helper.createDrawable(TEXTURE, 202, 0, 14, 14);
        this.animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURE, 176, 0, 26, 13);
        this.animatedBubbles = helper.createAnimatedDrawable(staticAnimation, 2400, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid()
    {
        return ID;
    }

    @Override
    public Class<? extends InfuserRecipe> getRecipeClass()
    {
        return InfuserRecipe.class;
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("recipe_category.mineria.infuser");
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
    public void setIngredients(InfuserRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputIngredients(Lists.newArrayList(recipe.getInput(), Ingredient.of(MineriaBlocks.WATER_BARREL, Items.WATER_BUCKET)));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, InfuserRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 13 - 8, 9 - 4);
        stacks.init(1, true, 46 - 8, 34 - 4);
        stacks.init(2, true, 129 - 8, 34 - 4);
        stacks.init(3, false, 90 - 8, 34 - 4);
        stacks.set(ingredients);
    }

    @Override
    public void draw(InfuserRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY)
    {
        animatedFlame.draw(matrixStack, 149 - 8, 36 - 4);
        animatedBubbles.draw(matrixStack, 64 - 8, 36 - 4);
    }
}
