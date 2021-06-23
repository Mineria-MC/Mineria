package com.mineria.mod.blocks.titane_extractor;

import com.mineria.mod.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TitaneExtractorScreen extends ContainerScreen<TitaneExtractorContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/titane_extractor.png");

    public TitaneExtractorScreen(TitaneExtractorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 200;
        this.ySize = 200;
        this.titleX = 74;
        this.titleY = 8;
        this.playerInventoryTitleX = 97;
        this.playerInventoryTitleY = 90;
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

        this.blit(matrixStack, this.guiLeft + 15, this.guiTop + 24, 201, 0, 36, this.container.getExtractTimeScaled() + 1);
    }
}
