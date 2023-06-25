package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DistillerRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final Ingredient ingredient3;
    private final ItemStack output;

    public DistillerRecipe(ResourceLocation id, Ingredient input, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, @Nonnull ItemStack output) {
        this.id = id;
        this.input = input;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.ingredient3 = ingredient3;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeWrapper inv, @Nullable Level world) {
        return input.test(inv.getItem(0)) && ingredient1.test(inv.getItem(1)) && ingredient2.test(inv.getItem(2)) && ingredient3.test(inv.getItem(3));
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull RecipeWrapper inv, @Nonnull RegistryAccess access) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull RegistryAccess access) {
        return this.output;
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getOutputStack() {
        return output;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.DISTILLER.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MineriaRecipeTypes.DISTILLER.get();
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, input, ingredient1, ingredient2, ingredient3);
    }

    public static class Serializer implements RecipeSerializer<DistillerRecipe> {
        @Nonnull
        @Override
        public DistillerRecipe fromJson(@Nonnull ResourceLocation location, @Nonnull JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            Ingredient ingredient1 = fromJson(json, "ingredient1");
            Ingredient ingredient2 = fromJson(json, "ingredient2");
            Ingredient ingredient3 = fromJson(json, "ingredient3");
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);

            return new DistillerRecipe(location, input, ingredient1, ingredient2, ingredient3, output);
        }

        @Nullable
        @Override
        public DistillerRecipe fromNetwork(@Nonnull ResourceLocation location, @Nonnull FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            Ingredient ingredient1 = fromNetwork(buf);
            Ingredient ingredient2 = fromNetwork(buf);
            Ingredient ingredient3 = fromNetwork(buf);
            ItemStack output = buf.readItem();

            return new DistillerRecipe(location, input, ingredient1, ingredient2, ingredient3, output);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buf, DistillerRecipe recipe) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.get(0).toNetwork(buf);
            ingredients.get(1).toNetwork(buf);
            ingredients.get(2).toNetwork(buf);
            ingredients.get(3).toNetwork(buf);
            buf.writeItemStack(recipe.output, false);
        }

        private static Ingredient fromJson(JsonObject json, String type) {
            Ingredient result = Ingredient.EMPTY;
            try {
                result = Ingredient.fromJson(json.get(type));
            } catch (JsonSyntaxException ignored) {
            }
            return result;
        }

        private static Ingredient fromNetwork(FriendlyByteBuf buffer) {
            int count = buffer.readVarInt();
            if (count == -1) {
                return CraftingHelper.getIngredient(buffer.readResourceLocation(), buffer);
            }
            if (count == 0)
                return Ingredient.EMPTY;
            return Ingredient.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(count));
        }
    }
}
