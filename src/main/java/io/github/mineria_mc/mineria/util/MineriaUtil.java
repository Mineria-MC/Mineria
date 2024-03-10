package io.github.mineria_mc.mineria.util;

import net.minecraft.network.chat.Component;

public class MineriaUtil {
    
    public static Component translatable(String prefix, String suffix) {
        return Component.translatable(prefix + "." + Constants.MODID + "." + suffix);
    }
}
