package io.github.mineria_mc.mineria.client.screens;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.ExtractorMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class ExtractorScreen extends AbstractContainerScreen<ExtractorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/extractor.png");

    public ExtractorScreen(ExtractorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 176;
        this.imageHeight = 200;
        this.titleLabelX = 50 - this.font.width(title) / 2;
        this.titleLabelY = 6;
        this.inventoryLabelX = this.imageWidth / 2 - this.font.width(this.playerInventoryTitle) / 2;
        this.inventoryLabelY = 106;
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

        this.blit(matrixStack, this.leftPos + 13, this.topPos + 37, 177, 0, 40, this.menu.getExtractTimeScaled() + 1);
    }
}
