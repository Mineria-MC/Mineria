package io.github.mineria_mc.mineria.client.jei.recipe_categories;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.recipe.DistillerRecipe;
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

public class DistillerRecipeCategory implements IRecipeCategory<DistillerRecipe> {
    public static final RecipeType<DistillerRecipe> TYPE = RecipeType.create(Mineria.MODID, "distiller", DistillerRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/distiller.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedBubbles;

    public DistillerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 5, 6, 150, 64);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlocks.DISTILLER.get()));

        IDrawableStatic staticFlame = helper.createDrawable(TEXTURE, 202, 42, 14, 14);
        this.animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticBubbles = helper.createDrawable(TEXTURE, 176, 29, 26, 13);
        this.animatedBubbles = helper.createAnimatedDrawable(staticBubbles, 60, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public RecipeType<DistillerRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("recipe_category.mineria.distiller");
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
    public void setRecipe(IRecipeLayoutBuilder builder, DistillerRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10 - 4, 11 - 5).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 10 - 4, 46 - 5).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 54 - 4, 21 - 5).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 86 - 4, 9 - 5).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 86 - 4, 43 - 5);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 132 - 4, 9 - 5).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(@Nonnull DistillerRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack stack, double mouseX, double mouseY) {
        this.animatedFlame.draw(stack, 87 - 5, 42 - 6);
        this.animatedBubbles.draw(stack, 105 - 5, 11 - 6);
    }
}
