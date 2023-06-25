package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ApothecaryFillingRecipe implements Recipe<ApothecaryTableInventoryWrapper> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final PoisonSource outputPoison;

    public ApothecaryFillingRecipe(ResourceLocation id, Ingredient input, PoisonSource output) {
        this.id = id;
        this.input = input;
        this.outputPoison = output;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    public Ingredient getInput() {
        return input;
    }

    public PoisonSource getOutputPoison() {
        return outputPoison;
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper inv, @Nonnull Level level) {
        return this.input.test(inv.getItem(0)) && !PoisonSource.UNKNOWN.equals(outputPoison) && (inv.getPoisonSource() == null || outputPoison.equals(inv.getPoisonSource()));
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull ApothecaryTableInventoryWrapper inv, @Nonnull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, input);
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.APOTHECARY_TABLE_FILLING.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MineriaRecipeTypes.APOTHECARY_TABLE_FILLING.get();
    }

    public static class Serializer implements RecipeSerializer<ApothecaryFillingRecipe> {
        @Nonnull
        @Override
        public ApothecaryFillingRecipe fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            PoisonSource outputPoison = PoisonSource.byName(new ResourceLocation(GsonHelper.getAsString(json, "output_poison")));
            return new ApothecaryFillingRecipe(id, input, outputPoison);
        }

        @Override
        public @Nullable ApothecaryFillingRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            PoisonSource outputPoison = PoisonSource.byName(buffer.readResourceLocation());
            return new ApothecaryFillingRecipe(id, input, outputPoison);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull ApothecaryFillingRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeResourceLocation(recipe.outputPoison.getId());
        }
    }
}
