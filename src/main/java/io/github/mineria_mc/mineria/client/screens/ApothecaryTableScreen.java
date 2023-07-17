package io.github.mineria_mc.mineria.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.ApothecaryTableMenu;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.util.FastColor.ARGB32.*;

public class ApothecaryTableScreen extends AbstractContainerScreen<ApothecaryTableMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/apothecary_table.png");

    public ApothecaryTableScreen(ApothecaryTableMenu container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = this.imageWidth / 2 - this.font.width(this.title) / 2;
        this.inventoryLabelX = 169 - font.width(playerInventoryTitle);
        this.inventoryLabelY = 72;
        this.titleLabelY = 5;
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty()) {
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                graphics.renderTooltip(font, this.hoveredSlot.getItem(), mouseX, mouseY);
            } else if (mouseX >= this.leftPos + 12 && mouseY >= this.topPos + 8 && mouseX <= this.leftPos + 29 && mouseY <= this.topPos + 79) {
                PoisonSource poisonSource = this.getPoisonSource();
                if (poisonSource != null) {
                    graphics.renderTooltip(font, Component.translatable(poisonSource.getTranslationKey()), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics graphics, float mouseX, int mouseY, int partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int liquidScaled = getLiquidScaled();
        if (getPoisonSource() != null) {
            int color = getPoisonSource().getColor();
            RenderSystem.setShaderColor(red(color) / 255.0F, green(color) / 255.0F, blue(color) / 255.0F, 1);
            graphics.blit(TEXTURE, this.leftPos + 13, this.topPos + 9 + 69 - liquidScaled, 177, 18 + 69 - liquidScaled, 15, liquidScaled);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
        graphics.blit(TEXTURE, this.leftPos + 12, this.topPos + 8, 192, 17, 17, 71);
        graphics.blit(TEXTURE, this.leftPos + 107, this.topPos + 35, 176, 0, getApplicationTimeScaled() + 1, 17);
    }

    @Nullable
    private PoisonSource getPoisonSource() {
        return this.menu.getPoisonSource();
    }

    private int getLiquidScaled() {
        return getPoisonSource() == null || this.menu.getLiquidAmount() == 0 ? 0 : this.menu.getLiquidAmount() * 69 / 5;
    }

    private int getApplicationTimeScaled() {
        return this.menu.getApplicationTime() == 0 ? 0 : this.menu.getApplicationTime() * 24 / this.menu.getTotalApplicationTime();
    }
}
