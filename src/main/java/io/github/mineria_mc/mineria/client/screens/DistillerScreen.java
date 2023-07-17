package io.github.mineria_mc.mineria.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.DistillerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class DistillerScreen extends AbstractContainerScreen<DistillerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/distiller.png");

    public DistillerScreen(DistillerMenu distillerContainer, Inventory playerInv, Component title) {
        super(distillerContainer, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 34;
        this.titleLabelY = 5;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = 74;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (this.menu.getTileEntity().isBurning()) {
            int k = this.menu.getBurnTimeScaled(13);
            graphics.blit(TEXTURE, this.leftPos + 87, this.topPos + 42 - k, 202, 42 - k, 14, k + 1);
        }

        renderParts(graphics);
    }

    private void renderParts(GuiGraphics graphics) {
        int distillationTime = this.menu.getDistillationTime();
        int totalDistillationTime = this.menu.getTotalDistillationTime();

        if (distillationTime <= totalDistillationTime * 5 / 20) {
            renderPart(graphics, 0, distillationTime, totalDistillationTime * 5 / 20);
        }
        else if (distillationTime <= totalDistillationTime * 6 / 20) {
            renderPart(graphics, 1, distillationTime - totalDistillationTime * 5 / 20, totalDistillationTime / 20);
        }
        else if (distillationTime <= totalDistillationTime * 12 / 20) {
            renderPart(graphics, 2, distillationTime - totalDistillationTime * 6 / 20, totalDistillationTime * 6 / 20);
        }
        else if (distillationTime <= totalDistillationTime * 15 / 20) {
            renderPart(graphics, 3, distillationTime - totalDistillationTime * 12 / 20, totalDistillationTime * 3 / 20);
        }
        else if (distillationTime <= totalDistillationTime) {
            renderPart(graphics, 4, distillationTime - totalDistillationTime * 15 / 20, totalDistillationTime * 5 / 20);
        }
    }

    private void renderPart(GuiGraphics graphics, int index, int currentDistillationTime, int totalDistillationTime) {
        int distillationTime = Math.min(currentDistillationTime, totalDistillationTime);
        int time;

        switch (index) {
            case 0 -> {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 26);
                graphics.blit(TEXTURE, this.leftPos + 28, this.topPos + 25 + Math.max(0, time - 3), 176, Math.max(0, time - 3), 12, Math.min(3, time));
            }
            case 1 -> {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 15);
                graphics.blit(TEXTURE, this.leftPos + 28 + Math.max(0, time - 2), this.topPos + 60, 188 + Math.max(0, time - 2), 26, Math.min(2, time), 2);
            }
            case 2 -> {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 29);
                graphics.blit(TEXTURE, this.leftPos + 42, this.topPos + 63 - time, 203, 29 - time, 12, Math.min(3, time));
            }
            case 3 -> {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 20);
                graphics.blit(TEXTURE, this.leftPos + 72, this.topPos + 34 - time, 215, 20 - time, 14, Math.min(3, time));
            }
            case 4 -> {
                time = getDistillationTimeScaled(distillationTime, totalDistillationTime, 26);
                graphics.blit(TEXTURE, this.leftPos + 105 + Math.max(0, time - 3), this.topPos + 11, 176 + Math.max(0, time - 3), 29, Math.min(3, time), 13);
            }
        }
    }

    private static int getDistillationTimeScaled(int distillationTime, int totalDistillationTime, int pixels) {
        return distillationTime * pixels / totalDistillationTime;
    }
}
