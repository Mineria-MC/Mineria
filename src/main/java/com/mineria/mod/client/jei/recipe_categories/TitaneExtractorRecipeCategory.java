package com.mineria.mod.client.jei.recipe_categories;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaBlocks;
import com.mineria.mod.common.recipe.TitaneExtractorRecipe;
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

public class TitaneExtractorRecipeCategory implements IRecipeCategory<TitaneExtractorRecipe>
{
	public static final ResourceLocation ID = new ResourceLocation(Mineria.MODID, "titane_extractor");
	private static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/titane_extractor.png");

	private final IDrawable icon;
	private final IDrawable background;

	private final IDrawableAnimated animation;

	public TitaneExtractorRecipeCategory(IGuiHelper helper)
	{
		this.background = helper.createDrawable(TEXTURES, 8, 4, 160, 90);
		this.icon = helper.createDrawableIngredient(new ItemStack(MineriaBlocks.TITANE_EXTRACTOR));
		IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 177, 0, 36, 53);
		this.animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
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
    public void setIngredients(TitaneExtractorRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, TitaneExtractorRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        stacks.init(0, true, 9 - 8, 6 - 4);
        stacks.init(1, true, 40 - 8, 6 - 4);
        stacks.init(2, true, 23 - 8, 77 - 4);
        stacks.init(3, false, 94 - 8, 46 - 4);
        stacks.set(ingredients);
    }

    @Override
    public void draw(TitaneExtractorRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY)
    {
        animation.draw(matrixStack, 15 - 8, 24 - 4);
    }

    @Override
	public Component getTitle()
	{
		return new TranslatableComponent("recipe_category.mineria.titane_extractor");
	}
	
	@Override
	public ResourceLocation getUid()
	{
		return ID;
	}

    @Override
    public Class<TitaneExtractorRecipe> getRecipeClass()
    {
        return TitaneExtractorRecipe.class;
    }
}
