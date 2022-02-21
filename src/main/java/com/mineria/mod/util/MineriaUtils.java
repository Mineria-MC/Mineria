package com.mineria.mod.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
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
     * We register here our recipe types because there is no {@link net.minecraftforge.registries.ForgeRegistry} for recipe types.
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
     * This method returns all the recipes corresponding to the given recipe type. (Server-Side)
     *
     * @param typeIn the recipe type
     * @param world the server world
     * @return a {@link Set} of all the recipes
     */
    public static <T extends IRecipe<?>> Set<T> findRecipesByType(IRecipeType<?> typeIn, World world)
    {
        return world != null ? (Set<T>) world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : new HashSet<>();
    }

    /**
     * This method returns all the recipes corresponding to given recipe type. (Client-Side)
     *
     * @param type the recipe type
     * @return a {@link Set} of all the recipes
     */
    @OnlyIn(Dist.CLIENT)
    public static <T extends IRecipe<?>> Set<T> findRecipesByType(IRecipeType<T> type)
    {
        return findRecipesByType(type, recipe -> true);
    }

    /**
     * Finds every recipe related to the given recipe type and matching the given predicate. (Client-Side)
     *
     * @param type the given recipe type
     * @param filter the condition the recipe need to match
     * @return a {@link Set} of all the recipes.
     */
    @OnlyIn(Dist.CLIENT)
    public static <T extends IRecipe<?>> Set<T> findRecipesByType(IRecipeType<T> type, Predicate<T> filter)
    {
        ClientWorld world = Minecraft.getInstance().level;
        return world != null ? ImmutableSet.copyOf((Set<T>) world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == type && filter.test((T) recipe)).collect(Collectors.toSet())) : Collections.emptySet();
    }

    /**
     * Runs a consumer of the given object if the predicate returns true.
     *
     * @param obj the object to test and to run the consumer.
     * @param condition the predicate required in order to run the consumer.
     * @param action the action that will be run if the object matches the predicate.
     * @param <T> the type of the given object.
     * @return the result of the predicate.
     */
    public static <T> boolean doIf(T obj, Predicate<T> condition, Consumer<T> action)
    {
        boolean test = condition.test(obj);
        if(test) action.accept(obj);
        return test;
    }

    private static final Random RANDOM = new Random();

    /**
     * Calculates a random pitch value.
     *
     * @return a float between 0.5 and 1.5.
     */
    public static float randomPitch()
    {
        return RANDOM.nextFloat() + 0.5F;
    }

    /**
     * Gets a random element from a given collection.
     *
     * @param collection the given collection.
     * @param <E> the type of the collection.
     * @return a random element of that collection.
     */
    public static <E> E getRandomElement(Collection<E> collection)
    {
        int num = (int) (RANDOM.nextDouble() * collection.size());
        for(E element : collection)
            if (--num < 0) return element;
        throw new AssertionError();
    }

    /**
     * Casts the given class to the specified typed class.
     *
     * @param aClass the given class.
     * @param <T> the type of the result class.
     * @return the given class cast to the specified typed class.
     */
    public static <T> Class<T> castClass(Class<?> aClass)
    {
        return (Class<T>)aClass;
    }

    /**
     * Checks if the month and the day matches to the current date.
     *
     * @param month the month to check
     * @param day the day to check
     * @return true if they match
     */
    public static boolean currentDateMatches(int month, int day)
    {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }
}
