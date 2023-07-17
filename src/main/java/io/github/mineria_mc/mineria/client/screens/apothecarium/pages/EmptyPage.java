package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import net.minecraft.client.gui.GuiGraphics;

public class EmptyPage extends ApothecariumPage {
    public EmptyPage(PageCreationContext ctx) {
        super(ctx);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, int x) {
    }
}
