package io.github.mineria_mc.mineria.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.capabilities.ki.KiEffectCategory;
import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KiPerksScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/ki_perks.png");

    private final List<KiEffectWidget> kiEffectWidgets = new ArrayList<>();

    protected int windowWidth;
    protected int windowHeight;
    protected int viewportWidth;
    protected int viewportHeight;

    protected float fade;

    public KiPerksScreen() {
        super(GameNarrator.NO_TITLE);
    }

    private static final float WIDTH_RATIO = 854 / 1.75f;

    private int scaledWidth(float pixels) {
//        float scaleFactor = width / WIDTH_RATIO;
//        return (int) (pixels * scaleFactor);
        return (int) pixels;
    }

    private static final float HEIGHT_RATIO = 480 / 1.75f;

    private int scaledHeight(float pixels) {
//        float scaleFactor = height / HEIGHT_RATIO;
//        return (int) (pixels * scaleFactor);
        return (int) pixels;
    }

    @Override
    protected void init() {
        super.init();
        this.windowWidth = scaledWidth(252);
        this.windowHeight = scaledHeight(140);
        this.viewportWidth = scaledWidth(234);
        this.viewportHeight = scaledHeight(113);

        int x = 16, y = 20;
        for (KiEffectCategory category : KiEffectCategory.values()) {
            kiEffectWidgets.add(new KiEffectWidget(x, y, category));
            x += 40;
            if(x >= 216) {
                x = 16;
                y += 44;
            }
        }
    }

    @Override
    protected void clearWidgets() {
        super.clearWidgets();
        this.kiEffectWidgets.clear();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int x = (this.width - windowWidth) / 2, y = (this.height - windowHeight) / 2;

        renderInside(graphics, x + scaledHeight(9), y + scaledWidth(18), mouseX, mouseY, partialTick);
        renderWindow(graphics, x, y);
        renderTooltip(graphics, x, y, mouseX, mouseY);
    }

    protected void renderInside(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, float partialTick) {
        graphics.enableScissor(x, y, x + viewportWidth, y + viewportHeight);
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0.0F);

        for(int i = -1; i <= 15; ++i) {
            for(int j = -1; j <= 8; ++j) {
                blit(graphics, TEXTURE, scaledWidth(16) * i, scaledHeight(16) * j, scaledWidth(16), scaledHeight(16), 219, 240, 16, 16, 256, 256);
            }
        }

        kiEffectWidgets.forEach(kiEffectWidget -> kiEffectWidget.render(graphics, mouseX, mouseY, partialTick));
        graphics.pose().popPose();
        graphics.disableScissor();
    }

    protected void renderWindow(GuiGraphics graphics, int x, int y) {
        RenderSystem.enableBlend();
//        graphics.blit(TEXTURE, x, y, windowWidth, windowHeight, 0, 0, 252, 140, 256, 256);
        blit(graphics, TEXTURE, (int) (this.width - windowWidth * width / WIDTH_RATIO) / 2, (int) (this.height - windowHeight * height / HEIGHT_RATIO) / 2, windowWidth, windowHeight, 0, 0, 252, 140, 256, 256);
        PoseStack stack = graphics.pose();
        stack.pushPose();
        float fontScale = scaledHeight(font.lineHeight) / (float) font.lineHeight;
        stack.translate(x + scaledWidth(8), y + scaledHeight(6), 0);
        stack.scale(fontScale, fontScale, 1);
        graphics.drawString(this.font, Component.translatable("screen.mineria.ki_perks"), 0, 0, 4210752, false);
        stack.popPose();
    }

    protected void renderTooltip(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        PoseStack stack = graphics.pose();

        stack.pushPose();
        stack.translate(x + scaledWidth(9), y + scaledHeight(18), 400);
        RenderSystem.enableDepthTest();
        stack.pushPose();
        stack.translate(0, 0, -200);
        graphics.fill(0, 0, viewportWidth, viewportHeight, Mth.floor(this.fade * 255.0F) << 24);
        boolean advanceFade = false;
        int relativeMouseX = mouseX - x - scaledWidth(9);
        int relativeMouseY = mouseY - y - scaledHeight(8);
        if (relativeMouseX > 0 && relativeMouseX < scaledWidth(234) && relativeMouseY > 0 && relativeMouseY < scaledHeight(113)) {
            for(KiEffectWidget kiEffectWidget : this.kiEffectWidgets) {
                if (kiEffectWidget.isMouseOver(relativeMouseX, relativeMouseY)) {
                    advanceFade = true;
                    kiEffectWidget.renderTooltip(graphics, x, y);
                    break;
                }
            }
        }

        stack.popPose();
        this.fade = Mth.clamp(advanceFade ? this.fade + 0.02F : this.fade - 0.04f, 0, 0.3f);
        RenderSystem.disableDepthTest();
        stack.popPose();
    }

    private void blit(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        PoseStack stack = graphics.pose();

        stack.pushPose();
        stack.translate(x, y, 0);
        stack.scale(this.width / WIDTH_RATIO, this.height / HEIGHT_RATIO, 1);
        graphics.blit(texture, 0, 0, width, height, u, v, uWidth, vHeight, textureWidth, textureHeight);
        stack.popPose();
    }

    private class KiEffectWidget extends AbstractWidget {
        private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(Mineria.MODID, "textures/gui/ki_effect_categories.png");

        private final KiEffectCategory category;

        public KiEffectWidget(int x, int y, KiEffectCategory category) {
            super(scaledWidth(x), scaledHeight(y), scaledWidth(24), scaledHeight(24), CommonComponents.EMPTY);
            this.category = category;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            if (this.visible) {
                this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
                this.renderWidget(graphics, mouseX, mouseY, partialTick);
            }
        }

        @Override
        protected void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            RenderSystem.enableDepthTest();
            blit(graphics, TEXTURE, getX(), getY(), getWidth(), getHeight(), 0, 141,  24, 24, 256, 256);
            blit(graphics, WIDGETS_LOCATION, getX() + scaledWidth(3), getY() + scaledHeight(3), scaledWidth(18), scaledHeight(18), (category.ordinal() % 5) * 18, Mth.floorDiv(category.ordinal(), 5) * 18, 18, 18, 90, 90);
        }

        protected void renderTooltip(GuiGraphics graphics, int viewportX, int viewportY) {
            Component title = getTitle(), requiredKi = getRequiredKiComponent(), description = getDescription();
            int tooltipWidth = scaledWidth(14) + getWidth() + scaledWidth(font.width(title)) + scaledWidth(font.width(requiredKi));
            List<FormattedCharSequence> descLines = font.split(description, tooltipWidth);
            int tooltipHeight = getHeight() + descLines.size() * scaledHeight(font.lineHeight) + scaledHeight(4);

            boolean overflowsX = getX() + tooltipWidth >= windowWidth;
            boolean overflowsY = viewportHeight - getY() <= tooltipHeight;

            RenderSystem.enableBlend();
            int startX = overflowsX ? getX() - tooltipWidth + getWidth() + scaledWidth(2) : getX() - scaledWidth(2);
            int startY = overflowsY ? getY() - tooltipHeight + getHeight() : getY();

            if (!descLines.isEmpty()) {
                if (overflowsY) {
                    drawBackgroundFromRect(graphics, startX, startY, tooltipWidth, descLines.size() * scaledHeight(font.lineHeight) + scaledHeight(4), 0xFF000000, 0xFF555555, 0xFF212121, KiPerksScreen.this::scaledWidth, KiPerksScreen.this::scaledHeight);
                } else {
                    drawBackgroundFromRect(graphics, startX, startY + getHeight(), tooltipWidth, descLines.size() * scaledHeight(font.lineHeight) + scaledHeight(4), 0xFF000000, 0xFF555555, 0xFF212121, KiPerksScreen.this::scaledWidth, KiPerksScreen.this::scaledHeight);
                }
            }

            /*graphics.blit(TEXTURE, pX + this.x + 3, pY + this.y, this.display.getFrame().getTexture(), 128, 26, 26);

            if (overflowsX) {
                graphics.drawString(this.minecraft.font, this.title, startX + 5, pY + this.y + 9, -1);
            } else {
                graphics.drawString(this.minecraft.font, this.title, pX + this.x + 32, pY + this.y + 9, -1);
            }

            if (overflowsY) {
                for(int k1 = 0; k1 < this.description.size(); ++k1) {
                    graphics.drawString(this.minecraft.font, this.description.get(k1), startX + 5, l + 26 - j1 + 7 + k1 * 9, -5592406, false);
                }
            } else {
                for(int l1 = 0; l1 < this.description.size(); ++l1) {
                    graphics.drawString(this.minecraft.font, this.description.get(l1), startX + 5, pY + this.y + 9 + 17 + l1 * 9, -5592406, false);
                }
            }*/
        }

        private static void drawBackgroundFromRect(GuiGraphics graphics, int x, int y, int width, int height, int borderColor, int innerBorderColor, int backgroundColor, Float2IntFunction scaledWidth, Float2IntFunction scaledHeight) {
            int scaled1Width = scaledWidth.get(1);
            int scaled1Height = scaledHeight.get(1);
            int scaled2Width = scaledWidth.get(2);
            int scaled2Height = scaledHeight.get(2);

            // border
            graphics.fill(x + scaled2Width, y, x + width - scaled2Width, y + scaled1Height, borderColor);
            graphics.fill(x, y + scaled2Height, x + scaled1Width, y + height - scaled2Height, borderColor);
            graphics.fill(x + width - scaled1Width, y + scaled2Height, x + width, y + height - scaled2Height, borderColor);
            graphics.fill(x + scaled2Width, y + height - scaled1Height, x + width - scaled2Width, y + height, borderColor);
            graphics.fill(x + scaled1Width, y + scaled1Height, x + width - scaled1Width, y + height - scaled1Height, borderColor);

            // innerBorder
            graphics.fill(x + scaled2Width, y + scaled1Height, x + width - scaled2Width, y + height - scaled1Height, innerBorderColor);
            graphics.fill(x + scaled1Width, y + scaled2Height, x + width - scaled1Width, y + height - scaled2Height, innerBorderColor);

            // background
            graphics.fill(x + scaled2Width, y + scaled2Height, x + width - scaled2Width, y + height - scaled2Height, backgroundColor);
        }

        protected Component getTitle() {
            return Component.literal("Title");
        }

        protected Component getRequiredKiComponent() {
            return Component.literal("Required Ki: 10");
        }

        protected Component getDescription() {
            return Component.literal("A description");
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        }
    }
}
