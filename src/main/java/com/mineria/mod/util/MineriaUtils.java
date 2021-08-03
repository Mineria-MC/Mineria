package com.mineria.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
     * @param <V> the recipe
     * @return the registered recipe type
     */
    public static <V extends IRecipe<?>> IRecipeType<V> registerRecipeType(ResourceLocation id)
    {
        return IRecipeType.register(id.toString());
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

    public static <T> boolean doIf(T obj, Predicate<T> condition, Consumer<T> action)
    {
        boolean test = condition.test(obj);
        if(test)
            action.accept(obj);
        return test;
    }

    /**
     * Calculates a random pitch based on the given integer.
     * For example :
     * - randomPitch(5) returns a number between 0.5 and 1.5
     * - randomPitch(146) returns a number between 0.854 and 1.146
     * - randomPitch(0) returns 1
     *
     * @return a float between 1 - maxDifference / 10^[number of digits of maxDifference] and 1 + maxDifference / 10^[number of digits].
     */
    public static float randomPitch()
    {
        Random rand = new Random();

        float floatDif = rand.nextFloat() / 2;

        return rand.nextBoolean() ? 1.0F - floatDif : 1.0F + floatDif;
    }
}
