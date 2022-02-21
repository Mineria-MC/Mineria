package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DistillerRecipe implements IRecipe<RecipeWrapper>
{
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Mineria.MODID, "distiller");

    private final ResourceLocation id;
    private final Ingredient input;
    private final Ingredient ingredient1;
    private final Ingredient ingredient2;
    private final Ingredient ingredient3;
    private final ItemStack output;

    public DistillerRecipe(ResourceLocation id, Ingredient input, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, @Nonnull ItemStack output)
    {
        this.id = id;
        this.input = input;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.ingredient3 = ingredient3;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeWrapper inv, @Nullable World world)
    {
        return input.test(inv.getItem(0)) && ingredient1.test(inv.getItem(1)) && ingredient2.test(inv.getItem(2)) && ingredient3.test(inv.getItem(3));
    }

    public boolean matches(IItemHandler inv)
    {
        /*Mineria.LOGGER.debug(String.format("Comparing :\n at slot 0 %s and %s\n at slot 1 %s and %s\n at slot 2 %s and %s\n at slot 3 %s and %s\n",
                getSafeItemStack(input).getItem().getRegistryName(), inv.getStackInSlot(0),
                getSafeItemStack(ingredient1).getItem().getRegistryName(), inv.getStackInSlot(1),
                getSafeItemStack(ingredient2).getItem().getRegistryName(), inv.getStackInSlot(2),
                getSafeItemStack(ingredient3).getItem().getRegistryName(), inv.getStackInSlot(3)));*/
        return input.test(inv.getStackInSlot(0)) && ingredient1.test(inv.getStackInSlot(1)) && ingredient2.test(inv.getStackInSlot(2)) && ingredient3.test(inv.getStackInSlot(3));
    }

    private static ItemStack getSafeItemStack(Ingredient ingredient)
    {
        if(ingredient.isEmpty()) return ItemStack.EMPTY;
        return ingredient.getItems()[0];
    }

    @Override
    public ItemStack assemble(RecipeWrapper inv)
    {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int gridWidth, int gridHeight)
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
        return MineriaRecipeSerializers.DISTILLER.get();
    }

    @Override
    public IRecipeType<?> getType()
    {
        return MineriaRecipeSerializers.DISTILLER_TYPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(Ingredient.EMPTY, input, ingredient1, ingredient2, ingredient3);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DistillerRecipe>
    {
        @Override
        public DistillerRecipe fromJson(ResourceLocation location, JsonObject json)
        {
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            Ingredient ingredient1 = fromJson(json, "ingredient1");
            Ingredient ingredient2 = fromJson(json, "ingredient2");
            Ingredient ingredient3 = fromJson(json, "ingredient3");
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);

            return new DistillerRecipe(location, input, ingredient1, ingredient2, ingredient3, output);
        }

        @Nullable
        @Override
        public DistillerRecipe fromNetwork(ResourceLocation location, PacketBuffer buf)
        {
            Ingredient input = Ingredient.fromNetwork(buf);
            Ingredient ingredient1 = fromNetwork(buf);
            Ingredient ingredient2 = fromNetwork(buf);
            Ingredient ingredient3 = fromNetwork(buf);
            ItemStack output = buf.readItem();

            return new DistillerRecipe(location, input, ingredient1, ingredient2, ingredient3, output);
        }

        @Override
        public void toNetwork(PacketBuffer buf, DistillerRecipe recipe)
        {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.get(0).toNetwork(buf);
            ingredients.get(1).toNetwork(buf);
            ingredients.get(2).toNetwork(buf);
            ingredients.get(3).toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        private static Ingredient fromJson(JsonObject json, String type)
        {
            Ingredient result = Ingredient.EMPTY;
            try
            {
                result = Ingredient.fromJson(json.get(type));
            } catch (JsonSyntaxException ignored) {}
            return result;
        }

        private static Ingredient fromNetwork(PacketBuffer buffer)
        {
            int count = buffer.readVarInt();
            if (count == -1)
            {
                return CraftingHelper.getIngredient(buffer.readResourceLocation(), buffer);
            }
            if(count == 0)
                return Ingredient.EMPTY;
            return Ingredient.fromValues(Stream.generate(() -> new Ingredient.SingleItemList(buffer.readItem())).limit(count));
        }
    }
}
