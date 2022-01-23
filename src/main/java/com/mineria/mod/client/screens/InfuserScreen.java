package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.blocks.infuser.InfuserBlock;
import com.mineria.mod.common.containers.InfuserContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InfuserScreen extends ContainerScreen<InfuserContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/infuser_gui.png");

    public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);

        this.titleLabelX = 40;
        this.titleLabelY = 5;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURES);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.menu.getTileEntity().getBlockState().getValue(InfuserBlock.LIT))
        {
            int k = this.menu.getBurnTimeScaled(13);
            this.blit(matrixStack, this.leftPos + 149, this.topPos + 36 + 12 - k, 202, 12 - k, 14, k + 1);
        }

        this.blit(matrixStack, this.leftPos + 64, this.topPos + 36, 176, 0, this.menu.getInfuseTimeScaled(26) + 1, 13);
    }
}
