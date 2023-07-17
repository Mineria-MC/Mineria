package io.github.mineria_mc.mineria.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import io.github.mineria_mc.mineria.util.MineriaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class HallucinationsOverlay implements IGuiOverlay {
    private static boolean usingHallucinationShader = false;

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        int windowWidth = mc.getWindow().getGuiScaledWidth();
        int windowHeight = mc.getWindow().getGuiScaledHeight();

        if (player != null && player.hasEffect(MineriaEffects.HALLUCINATIONS.get())) {
            if (MineriaConfig.CLIENT.useHallucinationsShader.get()) {
                PostChain currentGroup = mc.gameRenderer.currentEffect();
                usingHallucinationShader = currentGroup != null && currentGroup.getName().equals("minecraft:shaders/post/wobble.json");
                if (!usingHallucinationShader)
                    mc.gameRenderer.loadEffect(new ResourceLocation("shaders/post/wobble.json"));
            } else {
                gui.setupOverlayRenderState(true, false);
                renderHallucinations(gui.getGuiTicks(), windowWidth, windowHeight);
            }
        } else if (usingHallucinationShader) {
            mc.gameRenderer.shutdownEffect();
            usingHallucinationShader = false;
        }
    }

    private static final ResourceLocation HALLUCINATIONS_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/misc/pink_sheep.png");

    private static void renderHallucinations(int tickCount, int windowWidth, int windowHeight) {
        float alpha = Math.abs(((tickCount % 100) - 50) / 50.0F);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, HALLUCINATIONS_TEXTURE);

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
}
