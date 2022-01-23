package com.mineria.mod.client.events;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.util.MineriaConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class handling {@link RenderGameOverlayEvent} fired when {@link IngameGui} is rendering.
 */
@SuppressWarnings({"deprecation", "unused"})
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OverlayRenderingEvent
{
    private static boolean usingHallucinationShader = false;

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        IngameGui inGameGui = mc.gui;

        if(inGameGui instanceof ForgeIngameGui && player != null)
        {
            EffectInstance poison = player.getEffect(Effects.POISON);
            if(poison instanceof PoisonEffectInstance)
            {
                if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
                {
                    renderPoisonText(mc, (ForgeIngameGui) inGameGui, event.getMatrixStack(), (PoisonEffectInstance) poison);
                }
                else if(event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE)
                {
                    renderPoisonVignette(mc, (ForgeIngameGui) inGameGui, event.getWindow(), event.getMatrixStack(), (PoisonEffectInstance) poison);
                }
            }

            if(player.hasEffect(MineriaEffects.HALLUCINATIONS.get()))
            {
                if(MineriaConfig.CLIENT.useHallucinationsShader.get())
                {
                    ShaderGroup currentGroup = mc.gameRenderer.currentEffect();
                    usingHallucinationShader = currentGroup != null && currentGroup.getName().equals("minecraft:shaders/post/wobble.json");

                    if(!usingHallucinationShader)
                    {
                        mc.gameRenderer.loadEffect(new ResourceLocation("shaders/post/wobble.json"));
                    }
                } else if(event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE)
                {
                    renderHallucinations(mc, (ForgeIngameGui) inGameGui, event.getWindow(), event.getMatrixStack(), player.getEffect(MineriaEffects.HALLUCINATIONS.get()));
                }
            }
            else if(usingHallucinationShader)
            {
                mc.gameRenderer.shutdownEffect();
                usingHallucinationShader = false;
            }

            if(player.hasEffect(MineriaEffects.FAST_FREEZING.get()) && event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE)
            {
                renderFastFreezing(mc, (ForgeIngameGui) inGameGui, event.getWindow(), event.getMatrixStack(), player.getEffect(MineriaEffects.FAST_FREEZING.get()));
                if(!player.hasEffect(Effects.POISON)) renderFastFreezingVignette(mc, (ForgeIngameGui) inGameGui, event.getWindow(), event.getMatrixStack(), player.getEffect(MineriaEffects.FAST_FREEZING.get()));
            }
        }
    }

    private static void renderPoisonText(Minecraft mc, ForgeIngameGui gui, MatrixStack stack, PoisonEffectInstance instance)
    {
        mc.getProfiler().push("poisonEffectText");

        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        instance.drawPotionName(mc.font, stack, (float) mc.getWindow().getGuiScaledWidth() / 2, 12);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();

        mc.getProfiler().pop();
    }

    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");

    private static void renderPoisonVignette(Minecraft mc, ForgeIngameGui gui, MainWindow window, MatrixStack stack, PoisonEffectInstance instance)
    {
        mc.getProfiler().push("poisonEffectVignette");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 0F, 0F, 1.0F);

        mc.getTextureManager().bind(VIGNETTE_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int scaledHeight = window.getGuiScaledHeight();
        int scaledWidth = window.getGuiScaledWidth();
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();

        mc.getProfiler().pop();
    }

    private static final ResourceLocation HALLUCINATIONS_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/misc/pink_sheep.png");

    private static void renderHallucinations(Minecraft mc, ForgeIngameGui gui, MainWindow window, MatrixStack stack, EffectInstance instance)
    {
        mc.getProfiler().push("hallucinationsOverlay");

        float alpha = Math.abs(((gui.getGuiTicks() % 100) - 50) / 50.0F);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.disableAlphaTest();

        mc.getTextureManager().bind(HALLUCINATIONS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int scaledHeight = window.getGuiScaledHeight();
        int scaledWidth = window.getGuiScaledWidth();
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getProfiler().pop();
    }

    // TODOLTR Replace with texture from Minecraft 1.17
    private static final ResourceLocation FAST_FREEZING_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/misc/fast_freezing.png");

    private static void renderFastFreezing(Minecraft mc, ForgeIngameGui gui, MainWindow window, MatrixStack stack, EffectInstance instance)
    {
        mc.getProfiler().push("fastFreezingOverlay");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();

        mc.getTextureManager().bind(FAST_FREEZING_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int scaledHeight = window.getGuiScaledHeight();
        int scaledWidth = window.getGuiScaledWidth();
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.getProfiler().pop();
    }

    private static void renderFastFreezingVignette(Minecraft mc, ForgeIngameGui gui, MainWindow window, MatrixStack stack, EffectInstance instance)
    {
        mc.getProfiler().push("fastFreezingVignette");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(87 / 255F, 8 / 255F, 0, 1);

        mc.getTextureManager().bind(VIGNETTE_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int scaledHeight = window.getGuiScaledHeight();
        int scaledWidth = window.getGuiScaledWidth();
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();

        mc.getProfiler().pop();
    }
}
