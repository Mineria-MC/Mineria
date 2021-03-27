package com.mineria.mod.blocks.infuser;

import com.mineria.mod.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InfuserScreen extends ContainerScreen<InfuserContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID, "textures/gui/infuser/infuser_gui.png");

    public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.titleX = 40;
        this.titleY = 5;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.ySize - 96 + 2;
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
        this.minecraft.getTextureManager().bindTexture(TEXTURES);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if(this.container.getTileEntity().getBlockState().get(InfuserBlock.LIT))
        {
            int k = this.container.getBurnTimeScaled(13);
            this.blit(matrixStack, this.guiLeft + 149, this.guiTop + 36 + 12 - k, 202, 12 - k, 14, k + 1);
        }

        this.blit(matrixStack, this.guiLeft + 64, this.guiTop + 36, 176, 0, this.container.getInfuseTimeScaled(26) + 1, 13);
    }
}
