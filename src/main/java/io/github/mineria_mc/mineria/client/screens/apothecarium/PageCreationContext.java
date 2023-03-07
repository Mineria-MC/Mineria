package io.github.mineria_mc.mineria.client.screens.apothecarium;

import net.minecraft.client.Minecraft;

public record PageCreationContext(Minecraft client, ApothecariumFontWrapper font, int y, int width, int height, ApothecariumScreen parentScreen) {
}
