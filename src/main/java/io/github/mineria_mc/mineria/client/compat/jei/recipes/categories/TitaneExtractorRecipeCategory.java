package io.github.mineria_mc.mineria.client.compat.jei.recipes.categories;

import io.github.mineria_mc.mineria.common.recipe.TitaneExtractorRecipe;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TitaneExtractorRecipeCategory implements IRecipeCategory<TitaneExtractorRecipe> {
    public static final RecipeType<TitaneExtractorRecipe> TYPE = RecipeType.create(Constants.MODID, "titane_extractor", TitaneExtractorRecipe.class);
    private static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MODID, "textures/gui/titane_extractor.png");

    private final IDrawable icon;
    private final IDrawable background;

    private final IDrawableAnimated animation;

    public TitaneExtractorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURES, 8, 4, 160, 90);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlockRegistry.TITANE_EXTRACTOR));
        
        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 177, 0, 36, 53);
        this.animation = helper.createAnimatedDrawable(staticAnimation, 200, StartDirection.TOP, false);
    }

    @Override
    public RecipeType<TitaneExtractorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return MineriaUtil.translatable("recipe_category", "titane_extractor");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TitaneExtractorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 9 - 7, 6 - 3).addItemStack(recipe.inputs().input1());
        builder.addSlot(RecipeIngredientRole.INPUT, 40 - 7, 6 - 3).addItemStack(recipe.inputs().input2());
        builder.addSlot(RecipeIngredientRole.INPUT, 23 - 7, 77 - 3).addItemStack(recipe.inputs().filter());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94 - 7, 46 - 3).addItemStack(recipe.output());
    }

    @Override
    public void draw(TitaneExtractorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
            double mouseX, double mouseY) {
        animation.draw(guiGraphics, 15 - 8, 24 - 4);
    }
}
