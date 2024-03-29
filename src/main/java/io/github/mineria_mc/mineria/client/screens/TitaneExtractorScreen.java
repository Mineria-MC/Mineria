package io.github.mineria_mc.mineria.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.TitaneExtractorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TitaneExtractorScreen extends AbstractContainerScreen<TitaneExtractorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/titane_extractor.png");

    public TitaneExtractorScreen(TitaneExtractorMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 183;
        this.titleLabelX = 74;
        this.titleLabelY = 8;
        this.inventoryLabelX = 169 - font.width(playerInventoryTitle);
        this.inventoryLabelY = 90;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        graphics.blit(TEXTURE, this.leftPos + 15, this.topPos + 24, 177, 0, 36, this.menu.getExtractTimeScaled() + 1);
    }
}
