package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.TitaneExtractorContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class TitaneExtractorScreen extends AbstractContainerScreen<TitaneExtractorContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/titane_extractor.png");

    public TitaneExtractorScreen(TitaneExtractorContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 183;
        this.titleLabelX = 74;
        this.titleLabelY = 8;
        this.inventoryLabelX = 97;
        this.inventoryLabelY = 90;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(matrixStack, this.leftPos + 15, this.topPos + 24, 177, 0, 36, this.menu.getExtractTimeScaled() + 1);
    }
}
