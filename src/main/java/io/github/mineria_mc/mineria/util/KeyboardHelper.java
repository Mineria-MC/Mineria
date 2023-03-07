package io.github.mineria_mc.mineria.util;

import net.minecraft.client.gui.screens.Screen;

public class KeyboardHelper {
    public static boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }
}
