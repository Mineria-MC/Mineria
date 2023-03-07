package io.github.mineria_mc.mineria.client.screens.apothecarium.pages;

import io.github.mineria_mc.mineria.client.screens.apothecarium.PageCreationContext;
import com.mojang.blaze3d.vertex.PoseStack;

public class EmptyPage extends ApothecariumPage {
    public EmptyPage(PageCreationContext ctx) {
        super(ctx);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks, int x) {
    }
}
