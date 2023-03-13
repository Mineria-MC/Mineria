package io.github.mineria_mc.mineria.util;

import com.google.errorprone.annotations.CompileTimeConstant;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The utility class for Mineria.
 */
@SuppressWarnings("unchecked")
public class MineriaUtils {
    /**
     * This method returns all the recipes corresponding to the given recipe type. (Server-Side)
     *
     * @param typeIn the recipe type
     * @param world  the server world
     * @return a {@link Set} of all the recipes
     */
    @Nonnull
    public static <T extends Recipe<?>> Set<T> findRecipesByType(RecipeType<T> typeIn, Level world) {
        return world != null ? (Set<T>) world.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Set.of();
    }

    /**
     * This method returns all the recipes corresponding to given recipe type. (Client-Side)
     *
     * @param type the recipe type
     * @return a {@link Set} of all the recipes
     */
    @OnlyIn(Dist.CLIENT)
    @Nonnull
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
    @OnlyIn(Dist.CLIENT)
    @Nonnull
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

    public static final Random RANDOM = new Random();

    /**
     * Calculates a random pitch value.
     *
     * @return a float between 0.5 and 1.5.
     */
    public static float randomPitch() {
        return RANDOM.nextFloat() + 0.5F;
    }

    /**
     * Gets a random element from a given collection.
     *
     * @param collection the given collection.
     * @param <E>        the type of the collection.
     * @return a random element of that collection.
     */
    public static <E> E getRandomElement(Collection<E> collection) {
        int num = (int) (RANDOM.nextDouble() * collection.size());
        for (E element : collection)
            if (--num < 0) return element;
        throw new AssertionError();
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
     * Utility method that adds particles to the world in a similar way as the /particle command.
     *
     * @param level The current world.
     * @param particles The particles to add.
     * @param x The x pos.
     * @param y The y pos.
     * @param z The z pos.
     * @param dx The x offset.
     * @param dy The y offset.
     * @param dz The z offset.
     * @param speed The 'speed' of these particles.
     * @param count The amount of particles to display.
     * @param force If particle rendering should be forced.
     */
    public static void addParticles(Level level, ParticleOptions particles, double x, double y, double z, double dx, double dy, double dz, double speed, int count, boolean force) {
        RandomSource random = level.random;
        for (int i = 0; i < count; i++) {
            double deltaX = random.nextGaussian() * dx;
            double deltaY = random.nextGaussian() * dy;
            double deltaZ = random.nextGaussian() * dz;
            double speedX = random.nextGaussian() * speed;
            double speedY = random.nextGaussian() * speed;
            double speedZ = random.nextGaussian() * speed;

            level.addParticle(particles, force, x + deltaX, y + deltaY, z + deltaZ, speedX, speedY, speedZ);
        }
    }

    /**
     * Utility method to get around the nullable 'getKey' method in RegistryObject.
     *
     * @param obj The registry object.
     * @return The resource key associated to this registry object.
     * @param <T> The type of the object.
     */
    @Nonnull
    public static <T> ResourceKey<T> key(RegistryObject<T> obj) {
        ResourceKey<T> key = obj.getKey();
        if(key == null) {
            throw new NullPointerException("Registry object is empty!");
        }
        return key;
    }

    /**
     * Utility to directly convert constant string values into a compound, therefore
     * avoiding to have to manually create the compound tags and to fill them.<br>
     * For example:
     * <pre>
     *     public void foo() {
     *         CompoundTag bar = new CompoundTag()
     *         CompoundTag data = new CompoundTag()
     *         data.putInt("MyInt", 45);
     *         bar.put("Data", data);
     *         giveMeATag(bar);
     *     }
     *
     *     public void giveMeATag(CompoundTag tag) {...}
     * </pre>
     * becomes this:
     * <pre>
     *     public void foo() {
     *         giveMeATag(MineriaUtils.parseTag("{Data:{MyInt:45}}"));
     *     }
     *
     *     public void giveMeATag(CompoundTag tag) {...}
     * </pre>
     *
     * <strong>NOTE:</strong> for non-constant data to parse use TagParser#parse(String).
     *
     * @param data A <strong>CONSTANT</strong> string representation of the tag to be parsed.
     * @param args Additional format arguments for the data string.
     * @return The parsed tag.
     */
    public static CompoundTag parseTag(@CompileTimeConstant String data, Object... args) {
        try {
            return TagParser.parseTag(args.length == 0 ? data : String.format(data, args));
        } catch (CommandSyntaxException e) {
            Mineria.LOGGER.error("Caught an exception when parsing tag with data '{}'. Unaware developers are faulty!", data);
            throw new RuntimeException(e);
        }
    }
}
