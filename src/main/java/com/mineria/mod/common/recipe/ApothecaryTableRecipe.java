package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ApothecaryTableRecipe extends AbstractApothecaryTableRecipe
{
    private final PoisonSource poisonSource;
    private final ItemStack output;

    public ApothecaryTableRecipe(ResourceLocation id, Ingredient input, PoisonSource poisonSource, @Nonnull ItemStack output)
    {
        super(id, input);
        this.poisonSource = poisonSource;
        this.output = output;
    }

    @Override
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, @Nullable Level world)
    {
        return input.test(wrapper.getItem(1)) && poisonSource.equals(wrapper.getPoisonSource());
    }

    @Override
    public ItemStack assemble(ApothecaryTableInventoryWrapper wrapper)
    {
        return this.output.copy();
    }

    @Override
    public ItemStack getResultItem()
    {
        return this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.APOTHECARY_TABLE.get();
    }

    public PoisonSource getPoisonSource()
    {
        return poisonSource;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ApothecaryTableRecipe>
    {
        @Override
        public ApothecaryTableRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            PoisonSource poisonSource = PoisonSource.byName(ResourceLocation.tryParse(GsonHelper.getAsString(json, "poison_source")));
            ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);

            return new ApothecaryTableRecipe(id, input, poisonSource, output);
        }

        @Nullable
        @Override
        public ApothecaryTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            return new ApothecaryTableRecipe(id, Ingredient.fromNetwork(buf), PoisonSource.byName(buf.readResourceLocation()), buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ApothecaryTableRecipe recipe)
        {
            recipe.input.toNetwork(buf);
            buf.writeResourceLocation(recipe.poisonSource.getId());
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
