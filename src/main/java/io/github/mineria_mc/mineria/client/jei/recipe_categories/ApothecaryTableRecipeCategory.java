package io.github.mineria_mc.mineria.client.jei.recipe_categories;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import io.github.mineria_mc.mineria.common.recipe.AbstractApothecaryTableRecipe;
import io.github.mineria_mc.mineria.common.recipe.ApothecaryFillingRecipe;
import io.github.mineria_mc.mineria.common.recipe.ApothecaryTableRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mineria_mc.mineria.util.MineriaUtils;
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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.FastColor.ARGB32.*;

public class ApothecaryTableRecipeCategory implements IRecipeCategory<AbstractApothecaryTableRecipe> {
    public static final RecipeType<AbstractApothecaryTableRecipe> TYPE = RecipeType.create(Mineria.MODID, "apothecary_table", AbstractApothecaryTableRecipe.class);
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecary_table.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated animatedArrow;

    public ApothecaryTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 6, 6, 164, 74);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlocks.APOTHECARY_TABLE.get()));

        IDrawableStatic staticArrow = helper.createDrawable(TEXTURE, 176, 0, 24, 17);
        this.animatedArrow = helper.createAnimatedDrawable(staticArrow, 80, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public RecipeType<AbstractApothecaryTableRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("recipe_category.mineria.apothecary_table");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AbstractApothecaryTableRecipe recipe, @Nonnull IFocusGroup focuses) {
        if(recipe instanceof ApothecaryTableRecipe r) {
            List<ItemStack> validStacks = MineriaUtils.findRecipesByType(MineriaRecipeTypes.APOTHECARY_TABLE_FILLING.get(), fillingRecipe -> fillingRecipe.getOutputPoison().equals(r.getPoisonSource()))
                    .stream()
                    .map(ApothecaryFillingRecipe::getInput)
                    .flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
                    .toList();
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 36, 29).addItemStacks(validStacks);
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 78, 29).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 29).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(@Nonnull AbstractApothecaryTableRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull PoseStack stack, double mouseX, double mouseY) {
        this.animatedArrow.draw(stack, 101, 29);
        if (recipe instanceof ApothecaryTableRecipe) {
            drawPoisonSource(stack, 6, 2, ((ApothecaryTableRecipe) recipe).getPoisonSource());
        }
    }

    @Nonnull
    @Override
    public List<Component> getTooltipStrings(@Nonnull AbstractApothecaryTableRecipe recipe, @Nonnull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = new ArrayList<>();

        if (recipe instanceof ApothecaryTableRecipe) {
            if (mouseX > 6 && mouseX < 21 && mouseY > 2 && mouseY < 71) {
                tooltip.add(Component.translatable(((ApothecaryTableRecipe) recipe).getPoisonSource().getTranslationKey()));
            }
        }

        return tooltip;
    }

    private static void drawPoisonSource(PoseStack stack, int x, int y, PoisonSource poisonSource) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int color = poisonSource.getColor();
        RenderSystem.setShaderColor(red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F, 1.0f);
        GuiComponent.blit(stack, x + 1, y + 1, 1, 177, 18, 15, 69, 256, 256);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GuiComponent.blit(stack, x, y, 2, 192, 17, 17, 71, 256, 256);
    }
}
