package com.mineria.mod.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.References;
import com.mineria.mod.init.RecipeSerializerInit;
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
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(References.MODID, "infuser");

    private final ResourceLocation id;
    private Ingredient input;
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
        return input.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv)
    {
        return this.output.copy();
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput()
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
        return RecipeSerializerInit.INFUSER.get();
    }

    @Override
    public IRecipeType<?> getType()
    {
        return RecipeSerializerInit.INFUSER_TYPE;
    }

    public Ingredient getInput()
    {
        return input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.from(Ingredient.EMPTY, this.input);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe>
    {
        @Override
        public InfuserRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);

            return new InfuserRecipe(recipeId, input, output);
        }

        @Nullable
        @Override
        public InfuserRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            Ingredient input = Ingredient.read(buffer);
            ItemStack output = buffer.readItemStack();

            return new InfuserRecipe(recipeId, input, output);
        }

        @Override
        public void write(PacketBuffer buffer, InfuserRecipe recipe)
        {
            Ingredient input = recipe.getIngredients().get(0);
            input.write(buffer);
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
        }
    }
}
