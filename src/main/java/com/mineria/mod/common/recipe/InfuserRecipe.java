package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserRecipe implements Recipe<RecipeWrapper>
{
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Mineria.MODID, "infuser");

    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;

    public InfuserRecipe(ResourceLocation id, Ingredient input, @Nonnull ItemStack output)
    {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn)
    {
        return input.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv)
    {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getResultItem()
    {
        return this.output;
    }

    @Override
    public ResourceLocation getId()
    {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.INFUSER.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return MineriaRecipeSerializers.INFUSER_TYPE;
    }

    public Ingredient getInput()
    {
        return input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InfuserRecipe>
    {
        @Override
        public InfuserRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);

            return new InfuserRecipe(recipeId, input, output);
        }

        @Nullable
        @Override
        public InfuserRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();

            return new InfuserRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InfuserRecipe recipe)
        {
            Ingredient input = recipe.getIngredients().get(0);
            input.toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
