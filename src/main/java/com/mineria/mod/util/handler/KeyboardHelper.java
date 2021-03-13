package com.mineria.mod.util.handler;

import net.minecraft.client.util.InputMappings;
import org.antlr.v4.runtime.misc.Pair;

public class KeyboardHelper
{
    private static final Pair<Integer, Integer> L_SHIFT = new Pair<>(340, -1);
    private static final Pair<Integer, Integer> R_SHIFT = new Pair<>(344, -1);

    public static boolean isShiftKeyDown()
    {
        return InputMappings.isKeyDown(L_SHIFT.a, L_SHIFT.b) || InputMappings.isKeyDown(R_SHIFT.a, R_SHIFT.b);
    }
}
