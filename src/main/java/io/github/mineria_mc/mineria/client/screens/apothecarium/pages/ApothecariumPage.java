package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumBookmarkInfo;
import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumFontWrapper;
import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumScreen;
import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;

import javax.annotation.Nullable;

public abstract class ApothecariumPage {
    protected final Minecraft client;
    protected final ApothecariumFontWrapper font;
    protected final int y, width, height;
    protected final ApothecariumScreen parentScreen;

    public ApothecariumPage(PageCreationContext ctx) {
        this.client = ctx.client();
        this.font = ctx.font();
        this.y = ctx.y();
        this.width = ctx.width();
        this.height = ctx.height();
        this.parentScreen = ctx.parentScreen();
    }

    public abstract void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int x);

    @Nullable
    public ApothecariumBookmarkInfo bookmarkInfo() {
        return null;
    }

    protected float findFittingScale(FormattedText text, float maxScale, float margin) {
        return Math.min(font.width(text) * maxScale, width - 2 * margin) / font.width(text);
    }
}
