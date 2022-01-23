package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.common.blocks.apothecary_table.ApothecaryTableInventoryWrapper;
import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
    public boolean matches(ApothecaryTableInventoryWrapper wrapper, @Nullable World world)
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
    public IRecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.APOTHECARY_TABLE.get();
    }

    public PoisonSource getPoisonSource()
    {
        return poisonSource;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ApothecaryTableRecipe>
    {
        @Override
        public ApothecaryTableRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            Ingredient input = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            PoisonSource poisonSource = PoisonSource.byName(ResourceLocation.tryParse(JSONUtils.getAsString(json, "poison_source")));
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);

            return new ApothecaryTableRecipe(id, input, poisonSource, output);
        }

        @Nullable
        @Override
        public ApothecaryTableRecipe fromNetwork(ResourceLocation id, PacketBuffer buf)
        {
            return new ApothecaryTableRecipe(id, Ingredient.fromNetwork(buf), PoisonSource.byName(buf.readResourceLocation()), buf.readItem());
        }

        @Override
        public void toNetwork(PacketBuffer buf, ApothecaryTableRecipe recipe)
        {
            recipe.input.toNetwork(buf);
            buf.writeResourceLocation(recipe.poisonSource.getId());
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
