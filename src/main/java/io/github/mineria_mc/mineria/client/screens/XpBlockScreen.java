package io.github.mineria_mc.mineria.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.XpBlockMenu;
import io.github.mineria_mc.mineria.network.MineriaPacketHandler;
import io.github.mineria_mc.mineria.network.XpBlockMessageHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class XpBlockScreen extends AbstractContainerScreen<XpBlockMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/xp_block_new.png");

    private boolean active = false;
    private int sliderValue = 20;

    public XpBlockScreen(XpBlockMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.titleLabelX = 28;
    }

    @Override
    protected void init() {
        super.init();
        Component msg = Component.translatable(this.active ? "screen.mineria.xp_block.active" : "screen.mineria.xp_block.inactive").withStyle(this.active ? ChatFormatting.GREEN : ChatFormatting.RED);
        this.addRenderableWidget(Button.builder(msg, btn -> {
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(XpBlockMessageHandler.XpBlockMessage.state(menu.getTileEntityPos(), active = !active));
            this.clearWidgets();
            init();
        }).bounds(this.leftPos + 8, this.topPos + 54, 50, 20).build());
        this.addRenderableWidget(new Slider(this.leftPos + 66, this.topPos + 54, 102, 20, Component.translatable("screen.mineria.xp_block.delay").append(Component.literal(": ")), Component.literal(" ").append(Component.translatable("screen.mineria.xp_block.delay_unit")), 1, 100, sliderValue, true, slider -> {
            MineriaPacketHandler.PACKET_HANDLER.sendToServer(XpBlockMessageHandler.XpBlockMessage.delay(menu.getTileEntityPos(), sliderValue = slider.getValueInt()));
        }));
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, this.title.getVisualOrderText(), (this.imageWidth - font.width(this.title)) / 2F, 6, 4210752, false);
    }

    @Override
    public void render(@Nonnull GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return this.getFocused() != null && this.isDragging() && pButton == 0 && this.getFocused().mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.menu.onClose();
    }

    public static class Slider extends ForgeSlider {
        private final Consumer<Slider> action;

        public Slider(int xPos, int yPos, int width, int height, Component prefix, Component suf, double minVal, double maxVal, double currentVal, boolean drawStr, Consumer<Slider> action) {
            super(xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, drawStr);
            this.action = action;
        }

        @Override
        protected void applyValue() {
            this.action.accept(this);
        }
    }
}
