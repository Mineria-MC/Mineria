package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.UpgradeRecipe;

import javax.annotation.Nullable;

public class MineriaSmithingRecipe extends UpgradeRecipe {
    protected final Ingredient base;
    protected final Ingredient addition;
    protected final boolean keepTag;

    public MineriaSmithingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result, boolean keepTag) {
        super(id, base, addition, result);
        this.base = base;
        this.addition = addition;
        this.keepTag = keepTag;
    }

    @Override
    public ItemStack assemble(Container inv) {
        ItemStack result = this.getResultItem().copy();

        if (keepTag) {
            CompoundTag nbt = inv.getItem(0).getTag();
            if (nbt != null) {
                result.setTag(nbt.copy());
            }
        }

        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.SMITHING.get();
    }

    public static class Serializer implements RecipeSerializer<MineriaSmithingRecipe> {
        @Override
        public MineriaSmithingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            boolean keepTag = !json.has("keepTag") || GsonHelper.getAsBoolean(json, "keepTag");
            return new MineriaSmithingRecipe(id, base, addition, result, keepTag);
        }

        @Nullable
        @Override
        public MineriaSmithingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            boolean keepTag = buffer.readBoolean();
            return new MineriaSmithingRecipe(id, base, addition, result, keepTag);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MineriaSmithingRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeBoolean(recipe.keepTag);
        }
    }
}
