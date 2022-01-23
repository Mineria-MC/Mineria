package com.mineria.mod.util;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.item.Item;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Utility class used to access fields in {@link CooldownTracker}
 */
public class CooldownTrackerUtil
{
    // These fields are initialized one time to avoid performance issues.
    private static Field COOLDOWNS;
    private static Field TICK_COUNT;
    private static Field START_TIME;
    private static Field END_TIME;

    @SuppressWarnings("unchecked")
    public static <T> Map<Item, T> getCooldowns(CooldownTracker tracker) throws IllegalAccessException
    {
        if(COOLDOWNS == null)
            COOLDOWNS = ObfuscationReflectionHelper.findField(CooldownTracker.class, "field_185147_a");

        return (Map<Item, T>) COOLDOWNS.get(tracker);
    }

    public static int getTickCount(CooldownTracker tracker) throws IllegalAccessException
    {
        if(TICK_COUNT == null)
            TICK_COUNT = ObfuscationReflectionHelper.findField(CooldownTracker.class, "field_185148_b");

        return (int) TICK_COUNT.get(tracker);
    }

    public static Field getStartTimeField() throws NoSuchFieldException
    {
        Class<?> cooldownClass = CooldownTracker.class.getDeclaredClasses()[0];
        if(START_TIME == null)
            START_TIME = cooldownClass.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_185137_a"));

        START_TIME.setAccessible(true);
        return START_TIME;
    }

    public static Field getEndTimeField() throws NoSuchFieldException
    {
        Class<?> cooldownClass = CooldownTracker.class.getDeclaredClasses()[0];
        if(END_TIME == null)
            END_TIME = cooldownClass.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_185138_b"));

        END_TIME.setAccessible(true);
        return END_TIME;
    }
}
