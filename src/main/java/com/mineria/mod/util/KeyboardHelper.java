package com.mineria.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper
{
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    public static boolean isShiftKeyDown()
    {
        return InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
