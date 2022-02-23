package com.mineria.mod.client.events;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.instances.PoisonEffectInstance;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.util.MineriaConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class handling {@link RenderGameOverlayEvent} fired when {@link Gui} is rendering.
 */
// TODO
//@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OverlayRenderingEvent
{
    /*private static boolean usingHallucinationShader = false;

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Gui inGameGui = mc.gui;

        if(inGameGui instanceof ForgeIngameGui && player != null)
        {
            MobEffectInstance poison = player.getEffect(MobEffects.POISON);
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
                    PostChain currentGroup = mc.gameRenderer.currentEffect();
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
                if(!player.hasEffect(MobEffects.POISON)) renderFastFreezingVignette(mc, (ForgeIngameGui) inGameGui, event.getWindow(), event.getMatrixStack(), player.getEffect(MineriaEffects.FAST_FREEZING.get()));
            }
        }
    }

    private static void renderPoisonText(Minecraft mc, ForgeIngameGui gui, PoseStack stack, PoisonEffectInstance instance)
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

    private static void renderPoisonVignette(Minecraft mc, ForgeIngameGui gui, Window window, PoseStack stack, PoisonEffectInstance instance)
    {
        mc.getProfiler().push("poisonEffectVignette");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 0F, 0F, 1.0F);

        mc.getTextureManager().bind(VIGNETTE_TEXTURE);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
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

    private static void renderHallucinations(Minecraft mc, ForgeIngameGui gui, Window window, PoseStack stack, MobEffectInstance instance)
    {
        mc.getProfiler().push("hallucinationsOverlay");

        float alpha = Math.abs(((gui.getGuiTicks() % 100) - 50) / 50.0F);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.disableAlphaTest();

        mc.getTextureManager().bind(HALLUCINATIONS_TEXTURE);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
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

    private static void renderFastFreezing(Minecraft mc, ForgeIngameGui gui, Window window, PoseStack stack, MobEffectInstance instance)
    {
        mc.getProfiler().push("fastFreezingOverlay");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();

        mc.getTextureManager().bind(FAST_FREEZING_TEXTURE);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
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

    private static void renderFastFreezingVignette(Minecraft mc, ForgeIngameGui gui, Window window, PoseStack stack, MobEffectInstance instance)
    {
        mc.getProfiler().push("fastFreezingVignette");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(87 / 255F, 8 / 255F, 0, 1);

        mc.getTextureManager().bind(VIGNETTE_TEXTURE);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
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
    }*/
}