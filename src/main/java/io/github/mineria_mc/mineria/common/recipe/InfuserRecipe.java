package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.blocks.barrel.AbstractWaterBarrelBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaBlocks;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeTypes;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

public class InfuserRecipe implements Recipe<RecipeWrapper> {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Mineria.MODID, "infuser");

    private final ResourceLocation id;
    private final Ingredient input, container;
    private final Either<Ingredient, Fluid> secondaryInput;
    private final ItemStack output;

    public InfuserRecipe(ResourceLocation id, Ingredient input, Either<Ingredient, Fluid> secondaryInput, Ingredient container, @Nonnull ItemStack output) {
        this.id = id;
        this.input = input;
        this.secondaryInput = secondaryInput;
        this.container = container;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeWrapper inv, @Nonnull Level worldIn) {
        return input.test(inv.getItem(0)) && secondaryInputTest().test(inv.getItem(1)) && container.test(inv.getItem(3));
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull RecipeWrapper inv) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.INFUSER.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return MineriaRecipeTypes.INFUSER_TYPE.get();
    }

    public Ingredient getInput() {
        return input;
    }

    public Either<Ingredient, Fluid> getSecondaryInput() {
        return secondaryInput;
    }

    public Ingredient getSecondaryInputExamples() {
        return secondaryInput.map(Function.identity(), fluid -> Ingredient.of(fluid == Fluids.WATER ? MineriaBlocks.WATER_BARREL.get() : MineriaBlocks.IRON_FLUID_BARREL.get(), fluid.getBucket()));
    }

    public Predicate<ItemStack> secondaryInputTest() {
        return stack -> secondaryInput.map(ingredient -> ingredient.test(stack), fluid -> AbstractWaterBarrelBlockEntity.checkFluidFromStack(stack, fluid));
    }

    public Ingredient getContainer() {
        return container;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.input, this.secondaryInput.left().orElse(Ingredient.EMPTY), Ingredient.EMPTY, this.container);
    }

    public static class Serializer implements RecipeSerializer<InfuserRecipe> {
        @Nonnull
        @Override
        public InfuserRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            Either<Ingredient, Fluid> secondaryInput;
            if(json.has("secondary_input") && json.get("secondary_input").isJsonObject()) {
                JsonObject obj = json.getAsJsonObject("secondary_input");
                if(obj.has("fluid_id")) {
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(obj.get("fluid_id").getAsString()));
                    if(fluid == null) {
                        fluid = Fluids.EMPTY;
                    }
                    secondaryInput = Either.right(fluid);
                } else {
                    if(obj.has("empty") && obj.get("empty").getAsBoolean()) {
                        secondaryInput = Either.left(Ingredient.EMPTY);
                    } else {
                        secondaryInput = Either.left(Ingredient.fromJson(obj));
                    }
                }
            } else {
                secondaryInput = Either.right(Fluids.WATER);
            }
            Ingredient container = json.has("container") && json.get("container").isJsonObject() ? Ingredient.fromJson(json.get("container")) : Ingredient.of(MineriaItems.CUP.get());
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);

            return new InfuserRecipe(recipeId, input, secondaryInput, container, output);
        }

        @Nullable
        @Override
        public InfuserRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Either<Ingredient, Fluid> secondaryIngredient = buffer.readEither(Ingredient::fromNetwork, buf -> {
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(buf.readResourceLocation());
                return fluid == null ? Fluids.EMPTY : fluid;
            });
            Ingredient container = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();

            return new InfuserRecipe(recipeId, input, secondaryIngredient, container, output);
        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, InfuserRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeEither(recipe.secondaryInput,
                    (buf, ingredient) -> ingredient.toNetwork(buf),
                    (buf, fluid) -> {
                ResourceLocation id = ForgeRegistries.FLUIDS.getKey(fluid);
                buf.writeResourceLocation(id == null ? new ResourceLocation("empty") : id);
            });
            recipe.container.toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
