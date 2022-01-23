package com.mineria.mod.common.recipe;

import com.google.gson.JsonObject;
import com.mineria.mod.common.init.MineriaRecipeSerializers;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class MineriaSmithingRecipe extends SmithingRecipe
{
    protected final Ingredient base;
    protected final Ingredient addition;
    protected final boolean keepTag;

    public MineriaSmithingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result, boolean keepTag)
    {
        super(id, base, addition, result);
        this.base = base;
        this.addition = addition;
        this.keepTag = keepTag;
    }

    @Override
    public ItemStack assemble(IInventory inv)
    {
        ItemStack result = this.getResultItem().copy();

        if(keepTag)
        {
            CompoundNBT nbt = inv.getItem(0).getTag();
            if(nbt != null)
            {
                result.setTag(nbt.copy());
            }
        }

        return result;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return MineriaRecipeSerializers.SMITHING.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MineriaSmithingRecipe>
    {
        @Override
        public MineriaSmithingRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            Ingredient base = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "base"));
            Ingredient addition = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "addition"));
            ItemStack result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
            boolean keepTag = !json.has("keepTag") || JSONUtils.getAsBoolean(json, "keepTag");
            return new MineriaSmithingRecipe(id, base, addition, result, keepTag);
        }

        @Nullable
        @Override
        public MineriaSmithingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer)
        {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            boolean keepTag = buffer.readBoolean();
            return new MineriaSmithingRecipe(id, base, addition, result, keepTag);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MineriaSmithingRecipe recipe)
        {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeBoolean(recipe.keepTag);
        }
    }
}
