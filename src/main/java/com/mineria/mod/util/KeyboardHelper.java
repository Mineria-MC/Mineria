package com.mineria.mod.util;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class KeyboardHelper
{
    private static final long WINDOW = Minecraft.getInstance().getWindow().getWindow();

    public static boolean isShiftKeyDown()
    {
        return InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(WINDOW, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
}
