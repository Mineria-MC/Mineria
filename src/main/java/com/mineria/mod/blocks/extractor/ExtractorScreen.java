package com.mineria.mod.blocks.extractor;

import com.mineria.mod.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ExtractorScreen extends ContainerScreen<ExtractorContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/extractor.png");

    public ExtractorScreen(ExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init()
    {
        super.init();
        this.xSize = 213;
        this.ySize = 196;
        this.titleX = (this.xSize / 2 - this.font.getStringPropertyWidth(title) / 2) - 74;
        this.titleY = 6;
        this.playerInventoryTitleX = 116;
        this.playerInventoryTitleY = 104;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.blit(matrixStack, this.guiLeft + 11, this.guiTop + 35, 214, 0, 40, this.container.getExtractTimeScaled() + 1);
    }
}
