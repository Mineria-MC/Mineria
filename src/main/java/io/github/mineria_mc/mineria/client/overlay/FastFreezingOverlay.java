package io.github.mineria_mc.mineria.client.overlay;

import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FastFreezingOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, PoseStack stack, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        int windowWidth = mc.getWindow().getGuiScaledWidth();
        int windowHeight = mc.getWindow().getGuiScaledHeight();

        if (player != null && player.hasEffect(MineriaEffects.FAST_FREEZING.get())) {
            gui.setupOverlayRenderState(true, false);
            if (!player.hasEffect(MobEffects.POISON)) renderVignette(windowWidth, windowHeight);
            renderFastFreezing(windowWidth, windowHeight);
        }
    }

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("textures/misc/powder_snow_outline.png");

    private void renderFastFreezing(int windowWidth, int windowHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(0.0D, windowHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        builder.vertex(windowWidth, windowHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        builder.vertex(windowWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        builder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static final ResourceLocation VIGNETTE = new ResourceLocation("textures/misc/vignette.png");

    private void renderVignette(int windowWidth, int windowHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(0.34f, 0.03f, 0F, 1.0F);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIGNETTE);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(0.0D, windowHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        builder.vertex(windowWidth, windowHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        builder.vertex(windowWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        builder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
