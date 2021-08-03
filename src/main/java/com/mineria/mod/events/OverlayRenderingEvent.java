package com.mineria.mod.events;

import com.mineria.mod.References;
import com.mineria.mod.effects.PoisonEffectInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OverlayRenderingEvent
{
    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        IngameGui inGameGui = mc.ingameGUI;

        if(inGameGui instanceof ForgeIngameGui && player != null)
        {
            if(player.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
                {
                    renderPoisonText(mc, (ForgeIngameGui) inGameGui, event.getMatrixStack(), (PoisonEffectInstance) player.getActivePotionEffect(Effects.POISON));
                }
                else if(event.getType() == RenderGameOverlayEvent.ElementType.VIGNETTE)
                {
                    renderPoisonVignette(mc, (ForgeIngameGui) inGameGui, event.getMatrixStack(), (PoisonEffectInstance) player.getActivePotionEffect(Effects.POISON));
                }
            }
        }
    }

    private static void renderPoisonText(Minecraft mc, ForgeIngameGui gui, MatrixStack stack, PoisonEffectInstance instance)
    {
        mc.getProfiler().startSection("poisonEffectText");

        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        instance.drawPotionName(mc.fontRenderer, stack, (float) mc.getMainWindow().getScaledWidth() / 2, 12);
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }

    private static void renderPoisonVignette(Minecraft mc, ForgeIngameGui gui, MatrixStack stack, PoisonEffectInstance instance)
    {
        mc.getProfiler().startSection("poisonEffectVignette");

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 0F, 0F, 1.0F);

        mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/vignette.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int scaledHeight = ObfuscationReflectionHelper.getPrivateValue(IngameGui.class, gui, "field_194812_I");
        int scaledWidth = ObfuscationReflectionHelper.getPrivateValue(IngameGui.class, gui, "field_194811_H");
        bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(0.0F, 1.0F).endVertex();
        bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex(1.0F, 1.0F).endVertex();
        bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex(1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
