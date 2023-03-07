package io.github.mineria_mc.mineria.client.jei.recipe_categories;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.recipe.TitaneExtractorRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class TitaneExtractorRecipeCategory implements IRecipeCategory<TitaneExtractorRecipe> {
    public static final RecipeType<TitaneExtractorRecipe> TYPE = RecipeType.create(Mineria.MODID, "titane_extractor", TitaneExtractorRecipe.class);
    private static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/titane_extractor.png");

    private final IDrawable icon;
    private final IDrawable background;

    private final IDrawableAnimated animation;

    public TitaneExtractorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURES, 8, 4, 160, 90);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlocks.TITANE_EXTRACTOR.get()));
        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 177, 0, 36, 53);
        this.animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Nonnull
    @Override
    public RecipeType<TitaneExtractorRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("recipe_category.mineria.titane_extractor");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, @Nonnull TitaneExtractorRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 9 - 7, 6 - 3).addItemStack(recipe.inputs().input1());
        builder.addSlot(RecipeIngredientRole.INPUT, 40 - 7, 6 - 3).addItemStack(recipe.inputs().input2());
        builder.addSlot(RecipeIngredientRole.INPUT, 23 - 7, 77 - 3).addItemStack(recipe.inputs().filter());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94 - 7, 46 - 3).addItemStack(recipe.output());
    }

    @Override
    public void draw(@Nonnull TitaneExtractorRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack stack, double mouseX, double mouseY) {
        animation.draw(stack, 15 - 8, 24 - 4);
    }
}
