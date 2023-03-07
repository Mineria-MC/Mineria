package io.github.mineria_mc.mineria.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class MineriaRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    private final Factory<T> factory;

    public MineriaRecipeSerializer(Factory<T> factory) {
        this.factory = factory;
    }

    @Nonnull
    @Override
    public T fromJson(@Nonnull ResourceLocation id, @Nonnull JsonObject recipe) {
        return this.factory.create(id);
    }

    @Override
    public @Nullable T fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buffer) {
        return this.factory.create(id);
    }

    @Override
    public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull T recipe) {
    }

    public interface Factory<T> {
        T create(ResourceLocation id);
    }
}
