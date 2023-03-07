package io.github.mineria_mc.mineria.util;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Utility class used to access fields in {@link ItemCooldowns}
 */
public class ItemCooldownsUtil {
    // These fields are initialized one time to avoid performance issues.
    private static Field COOLDOWNS;
    private static Field TICK_COUNT;
    private static Field START_TIME;
    private static Field END_TIME;

    @SuppressWarnings("unchecked")
    public static <T> Map<Item, T> getCooldowns(ItemCooldowns cooldowns) throws IllegalAccessException {
        if (COOLDOWNS == null) {
            COOLDOWNS = ObfuscationReflectionHelper.findField(ItemCooldowns.class, "f_41515_");
        }

        return (Map<Item, T>) COOLDOWNS.get(cooldowns);
    }

    public static int getTickCount(ItemCooldowns cooldowns) throws IllegalAccessException {
        if (TICK_COUNT == null) {
            TICK_COUNT = ObfuscationReflectionHelper.findField(ItemCooldowns.class, "f_41516_");
        }

        return (int) TICK_COUNT.get(cooldowns);
    }

    public static Field getStartTimeField() throws NoSuchFieldException {
        Class<?> cooldownClass = ItemCooldowns.class.getDeclaredClasses()[0];
        if (START_TIME == null) {
            START_TIME = cooldownClass.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "f_41533_"));
            START_TIME.setAccessible(true);
        }

        return START_TIME;
    }

    public static Field getEndTimeField() throws NoSuchFieldException {
        Class<?> cooldownClass = ItemCooldowns.class.getDeclaredClasses()[0];
        if (END_TIME == null) {
            END_TIME = cooldownClass.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "f_41534_"));
            END_TIME.setAccessible(true);
        }

        return END_TIME;
    }
}
