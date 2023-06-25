package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import io.github.mineria_mc.mineria.common.init.MineriaRecipeSerializers;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Remove when 1.20
@Deprecated
@SuppressWarnings("removal")
public class MineriaLegacySmithingRecipe extends net.minecraft.world.item.crafting.LegacyUpgradeRecipe {
    protected final Ingredient base, addition;
    protected final ItemStack result;
    protected final boolean keepTag;

    public MineriaLegacySmithingRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result, boolean keepTag) {
        super(id, base, addition, result);
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.keepTag = keepTag;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull Container inv, @Nonnull RegistryAccess access) {
        ItemStack result = this.getResultItem(access).copy();

        if (keepTag) {
            CompoundTag nbt = inv.getItem(0).getTag();
            if (nbt != null) {
                result.setTag(nbt.copy());
            }
        }

        return result;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return MineriaRecipeSerializers.LEGACY_SMITHING.get();
    }

    public static class Serializer implements RecipeSerializer<MineriaLegacySmithingRecipe> {
        @Override
        public MineriaLegacySmithingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            boolean keepTag = !json.has("keepTag") || GsonHelper.getAsBoolean(json, "keepTag");
            return new MineriaLegacySmithingRecipe(id, base, addition, result, keepTag);
        }

        @Nullable
        @Override
        public MineriaLegacySmithingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            boolean keepTag = buffer.readBoolean();
            return new MineriaLegacySmithingRecipe(id, base, addition, result, keepTag);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MineriaLegacySmithingRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeBoolean(recipe.keepTag);
        }
    }
}
