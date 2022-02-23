package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.DistillerContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class DistillerScreen extends AbstractContainerScreen<DistillerContainer>
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/distiller.png");

    public DistillerScreen(DistillerContainer distillerContainer, Inventory playerInv, Component title)
    {
        super(distillerContainer, playerInv, title);

        this.titleLabelX = 34;
        this.titleLabelY = 5;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 74;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int x, int y)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURES);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if(this.menu.getTileEntity().isBurning())
        {
            int k = this.menu.getBurnTimeScaled(13);
            this.blit(stack, this.leftPos + 87, this.topPos + 42 - k, 202, 42 - k, 14, k + 1);
        }

        renderParts(stack);
    }

    private void renderParts(PoseStack stack)
    {
        int distillationTime = this.menu.getDistillationTime();
        int totalDistillationTime = this.menu.getTotalDistillationTime();

        if(distillationTime <= totalDistillationTime * 5 / 20)
            renderPart(stack, 0, distillationTime, totalDistillationTime * 5 / 20);
        else if(distillationTime <= totalDistillationTime * 6 / 20)
            renderPart(stack, 1, distillationTime - totalDistillationTime * 5 / 20, totalDistillationTime / 20);
        else if(distillationTime <= totalDistillationTime * 12 / 20)
            renderPart(stack, 2, distillationTime - totalDistillationTime * 6 / 20, totalDistillationTime * 6 / 20);
        else if(distillationTime <= totalDistillationTime * 15 / 20)
            renderPart(stack, 3, distillationTime - totalDistillationTime * 12 / 20, totalDistillationTime * 3 / 20);
        else if(distillationTime <= totalDistillationTime)
            renderPart(stack, 4, distillationTime - totalDistillationTime * 15 / 20, totalDistillationTime * 5 / 20);

        /*int totalDistillationTime = this.menu.getTotalDistillationTime();
        int distillationTime = this.menu.getDistillationTime();
        int k = totalDistillationTime - distillationTime;

        int l = getDistillationTimeScaled(Math.min(distillationTime, totalDistillationTime * 5 / 20), totalDistillationTime * 5 / 20, 26);
        renderPart(stack, 28, 25 + l, 176, l, 12, l);

        if(k > totalDistillationTime * 5 / 20);
            //renderPart2();
        if(k > totalDistillationTime * 6 / 20);
            //renderPart3();
        if(k > totalDistillationTime * 12 / 20);
            //renderPart4();
        if(k > totalDistillationTime * 15 / 20);
            //renderPart5();*/
    }

    private void renderPart(PoseStack stack, int index, int currentDistillationTime, int totalDistillationTime)
    {
        int distillationTime = Math.min(currentDistillationTime, totalDistillationTime);
        int time;

        switch (index)
        {
            case 0:
            {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 26);
                this.blit(stack, this.leftPos + 28, this.topPos + 25 + Math.max(0, time - 3), 176, Math.max(0, time - 3), 12, Math.min(3, time));
                break;
            }
            case 1:
            {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 15);
                this.blit(stack, this.leftPos + 28 + Math.max(0, time - 2), this.topPos + 60, 188 + Math.max(0, time - 2), 26, Math.min(2, time), 2);
                break;
            }
            case 2:
            {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 29);
                this.blit(stack, this.leftPos + 42, this.topPos + 63 - time, 203, 29 - time, 12, Math.min(3, time));
                break;
            }
            case 3:
            {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 20);
                this.blit(stack, this.leftPos + 72, this.topPos + 34 - time, 215, 20 - time, 14, Math.min(3, time));
                break;
            }
            case 4:
            {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 26);
                this.blit(stack, this.leftPos + 105 + Math.max(0, time - 3), this.topPos + 11, 176 + Math.max(0, time - 3), 29, Math.min(3, time), 13);
                break;
            }
            default:
                break;
        }
    }

    private void renderPart(PoseStack stack, int x, int y, int textureX, int textureY, int width, int height)
    {
        this.blit(stack, this.leftPos + x, this.topPos + y, textureX, textureY, width, height);
    }

    private static int getDistillationTimeScaled(int distillationTime, int totalDistillationTime, int pixels)
    {
        return distillationTime * pixels / totalDistillationTime;
    }

    /*
    if(k > 5 / 20)
        renderPart1();
    if(k > 1 / 20)
        renderPart2();
    if(k > 6 / 20)
        renderPart3();
    if(k > 3 / 20)
        renderPart4();
    if(k > 5 / 20)
        renderPart5();
     */
}
