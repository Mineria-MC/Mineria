package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.Mineria;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserRecipe implements IRecipe<RecipeWrapper>
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
    public boolean matches(RecipeWrapper inv, World worldIn)
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
    public IRecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.INFUSER.get();
    }

    @Override
    public IRecipeType<?> getType()
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

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe>
    {
        @Override
        public InfuserRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);

            return new InfuserRecipe(recipeId, input, output);
        }

        @Nullable
        @Override
        public InfuserRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
        {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();

            return new InfuserRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, InfuserRecipe recipe)
        {
            Ingredient input = recipe.getIngredients().get(0);
            input.toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
