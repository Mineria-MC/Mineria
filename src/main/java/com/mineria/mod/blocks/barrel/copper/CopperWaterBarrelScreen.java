package com.mineria.mod.blocks.barrel.copper;

import com.mineria.mod.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CopperWaterBarrelScreen extends ContainerScreen<CopperWaterBarrelContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/copper_water_barrel.png");

    public CopperWaterBarrelScreen(CopperWaterBarrelContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init()
    {
        super.init();
        this.xSize = 176;
        this.ySize = 126;
        this.titleX = (this.xSize / 2 - this.font.getStringPropertyWidth(title) / 2);
        this.titleY = 3;
        this.playerInventoryTitleX = 112;
        this.playerInventoryTitleY = 32;
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
        RenderSystem.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
