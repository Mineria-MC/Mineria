package io.github.mineria_mc.mineria.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

@SuppressWarnings({ "unchecked", "resource" })
public class MineriaUtil {
    
    public static Component translatable(String prefix, String suffix) {
        return Component.translatable(prefix + "." + Constants.MODID + "." + suffix);
    }

    /**
     * Checks if the month and the day matches to the current date.
     *
     * @param month the month to check
     * @param day   the day to check
     * @return true if they match
     */
    public static boolean currentDateMatches(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }

    /**
     * This method returns all the recipes corresponding to the given recipe type. (Server-Side)
     *
     * @param typeIn the recipe type
     * @param world  the server world
     * @return a {@link Set} of all the recipes
     */
    public static <T extends Recipe<?>> Set<T> findRecipesByType(RecipeType<T> typeIn, Level world) {
        return world != null ? (Set<T>) world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Set.of();
    }

    /**
     * This method returns all the recipes corresponding to given recipe type. (Client-Side)
     *
     * @param type the recipe type
     * @return a {@link Set} of all the recipes
     */
    public static <T extends Recipe<?>> List<T> findRecipesByType(RecipeType<T> type) {
        return findRecipesByType(type, recipe -> true);
    }

    /**
     * Finds every recipe related to the given recipe type and matching the given predicate. (Client-Side)
     *
     * @param type   the given recipe type
     * @param filter the condition the recipe need to match
     * @return a {@link Set} of all the recipes.
     */
    public static <T extends Recipe<?>> List<T> findRecipesByType(RecipeType<T> type, Predicate<T> filter) {
        
        ClientLevel world = Minecraft.getInstance().level;
        return world != null ? world.getRecipeManager().getRecipes()
                .stream()
                .filter(recipe -> recipe.getType() == type)
                .map(recipe -> (T) recipe)
                .filter(filter)
                .sorted(Comparator.comparing(Recipe::getId))
                .toList() : List.of();
    }
}
