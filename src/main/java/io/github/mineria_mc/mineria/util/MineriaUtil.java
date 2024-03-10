package io.github.mineria_mc.mineria.util;

import java.util.Calendar;

import net.minecraft.network.chat.Component;

public class MineriaUtil {
    
    public static Component translatable(String prefix, String suffix) {
        return Component.translatable(prefix + "." + Constants.MODID + "." + suffix);
    }

    public static boolean currentDateMatches(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }
}
