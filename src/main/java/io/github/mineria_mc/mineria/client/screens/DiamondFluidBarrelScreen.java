package io.github.mineria_mc.mineria.client.screens;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.containers.DiamondFluidBarrelMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class DiamondFluidBarrelScreen extends AbstractContainerScreen<DiamondFluidBarrelMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Mineria.MODID, "textures/gui/diamond_fluid_barrel.png");

    public DiamondFluidBarrelScreen(DiamondFluidBarrelMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 199;
        this.imageHeight = 166;
        this.titleLabelX = (this.imageWidth / 2 - this.font.width(title) / 2);
        this.titleLabelY = 6;
        this.inventoryLabelX = 19;
        this.inventoryLabelY = 73;
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        for(int i = 0; i < 3; i++) {
            if(menu.isInventoryActive(i)) {
                this.blit(poseStack, this.leftPos + 40, this.topPos + 17 + 18 * i, 0, 167, 144, 18);
            }
        }
    }
}
