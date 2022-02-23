package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.GoldenWaterBarrelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GoldenWaterBarrelScreen extends AbstractContainerScreen<GoldenWaterBarrelContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/golden_water_barrel.png");

    public GoldenWaterBarrelScreen(GoldenWaterBarrelContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init()
    {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 180;
        this.titleLabelX = (this.imageWidth / 2 - this.font.width(title) / 2);
        this.titleLabelY = 5;
        this.inventoryLabelX = 112;
        this.inventoryLabelY = 88;
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
    }
}
