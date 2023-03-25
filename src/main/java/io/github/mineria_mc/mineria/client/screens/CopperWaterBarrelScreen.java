package io.github.mineria_mc.mineria.client.screens;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.CopperWaterBarrelMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CopperWaterBarrelScreen extends AbstractContainerScreen<CopperWaterBarrelMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/copper_water_barrel.png");

    public CopperWaterBarrelScreen(CopperWaterBarrelMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 126;
        this.titleLabelX = (this.imageWidth / 2 - this.font.width(title) / 2);
        this.titleLabelY = 3;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 32;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}