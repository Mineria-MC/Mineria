package io.github.mineria_mc.mineria.client.compat.jei.recipes.categories;

import io.github.mineria_mc.mineria.common.recipe.ExtractorRecipe;
import io.github.mineria_mc.mineria.common.registries.MineriaBlockRegistry;
import io.github.mineria_mc.mineria.util.Constants;
import io.github.mineria_mc.mineria.util.MineriaUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ExtractorRecipeCategory implements IRecipeCategory<ExtractorRecipe> {

    public static final RecipeType<ExtractorRecipe> TYPE = RecipeType.create(Constants.MODID, "extractor", ExtractorRecipe.class);
    private static final ResourceLocation TEXTURES = new ResourceLocation(Constants.MODID, "textures/gui/extractor.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animation;

    public ExtractorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURES, 4, 5, 158, 108);
        this.icon = helper.createDrawableItemStack(new ItemStack(MineriaBlockRegistry.EXTRACTOR));

        IDrawableStatic staticAnimation = helper.createDrawable(TEXTURES, 177, 0, 40, 53);
        this.animation = helper.createAnimatedDrawable(staticAnimation, 200, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public RecipeType<ExtractorRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return MineriaUtil.translatable("recipe_category", "extractor");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExtractorRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotTooltipCallback tooltipCallback = (recipeSlotView, tooltip) -> recipeSlotView.getDisplayedItemStack().ifPresent(stack -> {
            float chance = recipe.chance(stack);
            String chanceStr = chance == Math.ceil(chance) ? Integer.toString((int) chance) : Float.toString(chance);
            tooltip.add(Component.translatable("recipe_category.mineria.extractor.chance", chanceStr).withStyle(ChatFormatting.GOLD));
        });
        builder.addSlot(RecipeIngredientRole.INPUT, 8 - 4, 20 - 5).addItemStack(recipe.inputs().input1());
        builder.addSlot(RecipeIngredientRole.INPUT, 43 - 4, 20 - 5).addItemStack(recipe.inputs().input2());
        builder.addSlot(RecipeIngredientRole.INPUT, 25 - 4, 92 - 5).addItemStack(recipe.inputs().filter());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 137 - 4, 92 - 5).addItemStack(recipe.outputs().get(0)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 137 - 4, 70 - 5).addItemStack(recipe.outputs().get(1)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 137 - 4, 48 - 5).addItemStack(recipe.outputs().get(2)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 137 - 4, 27 - 5).addItemStack(recipe.outputs().get(3)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102 - 4, 8 - 5).addItemStack(recipe.outputs().get(4)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70 - 4, 27 - 5).addItemStack(recipe.outputs().get(5)).addTooltipCallback(tooltipCallback);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70 - 4, 48 - 5).addItemStack(recipe.outputs().get(6)).addTooltipCallback(tooltipCallback);
    }
    
    @Override
    public void draw(ExtractorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
            double mouseY) {
        animation.draw(guiGraphics, 13 - 4, 37 - 5);
    }
}
