package com.mineria.mod.client.screens;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.containers.XpBlockContainer;
import com.mineria.mod.network.MineriaPacketHandler;
import com.mineria.mod.network.XpBlockMessageHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class XpBlockScreen extends AbstractContainerScreen<XpBlockContainer>
{
    public static final ResourceLocation TEXTURES = new ResourceLocation(Mineria.MODID, "textures/gui/xp_block_new.png");

    private boolean active = false;
    private int sliderValue = 20;

    public XpBlockScreen(XpBlockContainer screenContainer, Inventory inv, Component titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.titleLabelX = 28;
    }

    @Override
    protected void init()
    {
        super.init();
        this.renderables.clear();
        this.children().clear();
        /*this.addButton(new Button(this.leftPos + 21, this.topPos + 19, 78, 20, new TranslationTextComponent("screen.mineria.xp_block.give_xp"), button -> {
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(new GuiButtonPressedMessageHandler.GuiButtonPressedMessage(this.menu.getTileEntityPos(), 0));
        }));*/
        this.addRenderableWidget(new Button(this.leftPos + 8, this.topPos + 54, 50, 20, new TranslatableComponent(this.active ? "screen.mineria.xp_block.active" : "screen.mineria.xp_block.inactive").withStyle(this.active ? ChatFormatting.GREEN : ChatFormatting.RED), btn -> {
//            MineriaPacketHandler.PACKET_HANDLER.sendToServer(new GuiButtonPressedMessageHandler.GuiButtonPressedMessage(this.menu.getTileEntityPos(), 0));
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(XpBlockMessageHandler.XpBlockMessage.state(menu.getTileEntityPos(), active = !active));
            init();
        }));
        this.addRenderableWidget(new Slider(this.leftPos + 66, this.topPos + 54, 102, 20, new TranslatableComponent("screen.mineria.xp_block.delay").append(new TextComponent(": ")), new TextComponent(" ").append(new TranslatableComponent("screen.mineria.xp_block.delay_unit")), 1, 100, sliderValue, false, true, slider -> init(), slider -> {
//            this.menu.setOrbItemDelay(slider.getValueInt())
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(XpBlockMessageHandler.XpBlockMessage.delay(menu.getTileEntityPos(), sliderValue = slider.getValueInt()));
        }));
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY)
    {
        this.font.draw(stack, this.title, (this.imageWidth - font.width(this.title)) / 2F, 6, 4210752);
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
        RenderSystem.setShaderTexture(0, TEXTURES);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        this.menu.onClose();
    }

    public static class Slider extends net.minecraftforge.fmlclient.gui.widget.Slider
    {
        public Slider(int xPos, int yPos, int width, int height, Component prefix, Component suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, OnPress handler, @Nullable ISlider par)
        {
            super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, handler, par);
        }

        @Override
        protected void renderBg(PoseStack mStack, Minecraft par1Minecraft, int mouseX, int mouseY)
        {
            if(!isMouseOver(mouseX, mouseY))
                this.dragging = false;

            super.renderBg(mStack, par1Minecraft, mouseX, mouseY);
        }

        /*@Override
        public int getYImage(boolean hovered)
        {
            return this.active ? (hovered ? 2 : 1) : 0;
        }

        @Override
        protected void renderBg(MatrixStack mStack, Minecraft mc, int par2, int par3)
        {
            if (this.dragging)
            {
                this.sliderValue = (par2 - (this.x + 4)) / (float)(this.width - 8);
                updateSlider();
            }

            mc.getTextureManager().bind(WIDGETS_LOCATION);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.isHovered() ? 2 : 1) * 20;
            this.blit(mStack, this.x + (int)(this.sliderValue * (double)(this.width - 8)), this.y, 0, 46 + i, 4, 20);
            this.blit(mStack, this.x + (int)(this.sliderValue * (double)(this.width - 8)) + 4, this.y, 196, 46 + i, 4, 20);
        }*/
    }
}
