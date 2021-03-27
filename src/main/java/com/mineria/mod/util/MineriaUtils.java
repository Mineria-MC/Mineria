package com.mineria.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The utility class for Mineria.
 */
@SuppressWarnings({"unchecked", "resource"})
public class MineriaUtils
{
    /**
     * We register here the recipe types because there is no {@link net.minecraftforge.registries.ForgeRegistry} for the recipe type.
     *
     * @param id the id of the recipe type (for example : "example:example_recipe_type")
     * @param <T> the recipe type that is returned
     * @param <V> the recipe
     * @return the registered recipe type ({@link T})
     */
    public static <T extends IRecipeType<V>, V extends IRecipe<?>> T registerRecipeType(ResourceLocation id)
    {
        return (T) Registry.register(Registry.RECIPE_TYPE, id, new IRecipeType<V>() {
            @Override
            public String toString()
            {
                return Registry.RECIPE_TYPE.getKey(this).toString();
            }
        });
    }

    /**
     * This method gets all the recipes with the given recipe type. (Server-Side)
     *
     * @param typeIn the recipe type
     * @param world the server world
     * @return a {@link Set} of all the recipes
     */
    @Nullable
    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world)
    {
        return world != null ? world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : null;
    }

    /**
     * This method gets all the recipes with the given recipe type. (Client-Side)
     *
     * @param typeIn the recipe type
     * @return a {@link Set} of all the recipes
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static <T extends IRecipe<?>> Set<T> findRecipesByType(IRecipeType<?> typeIn)
    {
        ClientWorld world = Minecraft.getInstance().world;
        return world != null ? (Set<T>) world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : null;
    }
}
