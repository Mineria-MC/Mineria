package com.mineria.mod.client.jei.recipe_categories;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.recipe.AbstractApothecaryTableRecipe;
import com.mineria.mod.common.recipe.ApothecaryTableRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.FastColor.ARGB32.*;

public class ApothecaryTableRecipeCategory implements IRecipeCategory<AbstractApothecaryTableRecipe>
{
    public static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "apothecary_table");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecary_table.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedArrow;

    public ApothecaryTableRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 6, 6, 164, 74);
        this.icon = helper.createDrawableIngredient(new ItemStack(MineriaBlocks.APOTHECARY_TABLE));

        IDrawableStatic staticArrow = helper.createDrawable(TEXTURE, 176, 0, 24, 17);
        this.animatedArrow = helper.createAnimatedDrawable(staticArrow, 80, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid()
    {
        return ID;
    }

    @Override
    public Class<? extends AbstractApothecaryTableRecipe> getRecipeClass()
    {
        return AbstractApothecaryTableRecipe.class;
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("recipe_category.mineria.apothecary_table");
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
    public void setIngredients(AbstractApothecaryTableRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AbstractApothecaryTableRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(1, true, 77, 28);
        stacks.init(2, false, 134, 28);
        stacks.set(ingredients);
    }

    @Override
    public void draw(AbstractApothecaryTableRecipe recipe, PoseStack stack, double mouseX, double mouseY)
    {
        this.animatedArrow.draw(stack, 101, 29);
        if(recipe instanceof ApothecaryTableRecipe) drawPoisonSource(stack, 6, 2, ((ApothecaryTableRecipe) recipe).getPoisonSource());
    }

    @Override
    public List<Component> getTooltipStrings(AbstractApothecaryTableRecipe recipe, double mouseX, double mouseY)
    {
        List<Component> tooltip = new ArrayList<>();

        if(mouseX > 6 && mouseX < 21 && mouseY > 2 && mouseY < 71)
        {
            if(recipe instanceof ApothecaryTableRecipe)
            {
                tooltip.add(new TranslatableComponent(((ApothecaryTableRecipe) recipe).getPoisonSource().getTranslationKey()));
            }
        }

        return tooltip;
    }

    private static void drawPoisonSource(PoseStack stack, int x, int y, PoisonSource poisonSource)
    {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int color = poisonSource.getColor();
        RenderSystem.setShaderColor(red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F, 1.0f);
        GuiComponent.blit(stack, x + 1, y + 1, 1, 177, 18, 15, 69, 256, 256);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiComponent.blit(stack, x, y, 2, 192, 17, 17, 71, 256, 256);
    }
}
