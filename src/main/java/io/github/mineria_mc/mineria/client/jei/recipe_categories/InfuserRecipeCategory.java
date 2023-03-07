package io.github.mineria_mc.mineria.client.jei.recipe_categories;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.recipe.InfuserRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class InfuserRecipeCategory implements IRecipeCategory<InfuserRecipe> {
    public static final RecipeType<InfuserRecipe> TYPE = RecipeType.create(Mineria.MODID, "infuser", InfuserRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/infuser_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedFlame;
    private final IDrawableAnimated animatedBubbles;

    public InfuserRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 8, 4, 159, 62);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlocks.INFUSER.get()));

        IDrawableStatic staticFlame = helper.createDrawable(TEXTURE, 202, 0, 14, 14);
        this.animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURE, 176, 0, 26, 13);
        this.animatedBubbles = helper.createAnimatedDrawable(staticAnimation, 2400, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public RecipeType<InfuserRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("recipe_category.mineria.infuser");
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
    public void setRecipe(IRecipeLayoutBuilder builder, InfuserRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 31).addIngredients(recipe.getSecondaryInputExamples());
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 122, 31);
        builder.addSlot(RecipeIngredientRole.INPUT, 83, 31).addIngredients(recipe.getContainer());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 11).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(@Nonnull InfuserRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack stack, double mouseX, double mouseY) {
        Component resultText = Component.translatable("recipe_category.mineria.infuser.result");
        Font font = Minecraft.getInstance().font;
        font.draw(stack, resultText,93 - font.width(resultText) / 2f, 1, 0xFF7C7C7C);
        animatedFlame.draw(stack, 149 - 8, 36 - 4);
        animatedBubbles.draw(stack, 64 - 8, 36 - 4);
    }
}
