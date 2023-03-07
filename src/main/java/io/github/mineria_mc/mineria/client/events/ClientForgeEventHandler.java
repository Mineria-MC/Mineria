package io.github.mineria_mc.mineria.client.events;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.enchantments.FourElementsEnchantment;
import io.github.mineria_mc.mineria.util.MineriaCreativeModeTabs;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Optional;

import static net.minecraft.util.FastColor.ARGB32.*;

/**
 * Hooks for every Forge event on client-side.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventHandler {
    private static final ResourceLocation BRANDING = new ResourceLocation(Mineria.MODID, "textures/branding/branding_mineria_apothecary_update.png");

    @SubscribeEvent
    public static void onRenderBackground(ScreenEvent.BackgroundRendered event) {
        if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
            Minecraft mc = screen.getMinecraft();
            Window window = mc.getWindow();

            if (MineriaCreativeModeTabs.getApothecaryTab().equals(getSelectedTab())) {
                RenderSystem.setShaderTexture(0, BRANDING);
                int startX = 0;
                int startY = 0;
                int drawWidth = window.getWidth() / 4;
                int drawHeight = window.getHeight() / 6;
                int width = 3000;
                int height = 1080;
                int imageWidth = 3072;
                int imageHeight = 3072;
                GuiComponent.blit(event.getPoseStack(), startX, startY, drawWidth, drawHeight, 0, 0, width, height, imageWidth, imageHeight);
            }
        }
    }

    private static Field SELECTED_TAB;

    @Nullable
    private static CreativeModeTab getSelectedTab() {
        if(SELECTED_TAB == null) {
            SELECTED_TAB = ObfuscationReflectionHelper.findField(CreativeModeInventoryScreen.class, "f_98507_");
        }
        try {
            return (CreativeModeTab) SELECTED_TAB.get(null);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    private static final ResourceLocation ELEMENTAL_ORB_TEXTURE = new ResourceLocation(Mineria.MODID, "textures/entity/elemental_orb.png");

    private static float time;

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        time += event.renderTickTime;
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity living = event.getEntity();
        if(!living.isAlive()) {
            return;
        }
        Optional<FourElementsEnchantment> opt = FourElementsEnchantment.getFromEntity(living);
        if(opt.isEmpty()) {
            return;
        }
        renderOrbs(event.getPoseStack(), Minecraft.getInstance().getEntityRenderDispatcher(), event.getMultiBufferSource(), opt.get().getElementType().getColor(), event.getPackedLight());
    }

    @SubscribeEvent
    public static void afterParticlesRender(RenderLevelStageEvent event) {
        if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if(!mc.options.getCameraType().isFirstPerson()) {
            return;
        }
        if(!(event.getCamera().getEntity() instanceof LivingEntity living)) {
            return;
        }
        if(!living.isAlive()) {
            return;
        }
        Optional<FourElementsEnchantment> opt = FourElementsEnchantment.getFromEntity(living);
        if(opt.isEmpty()) {
            return;
        }
        RenderBuffers buffers = mc.renderBuffers();
        EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
        PoseStack stack = event.getPoseStack();

        double relativeX = Mth.lerp(event.getPartialTick(), living.xOld, living.getX());
        double relativeY = Mth.lerp(event.getPartialTick(), living.yOld, living.getY());
        double relativeZ = Mth.lerp(event.getPartialTick(), living.zOld, living.getZ());
        Vec3 cameraPos = event.getCamera().getPosition();

        stack.pushPose();
        stack.translate(relativeX - cameraPos.x, relativeY - cameraPos.y, relativeZ - cameraPos.z);
        renderOrbs(stack, entityRenderDispatcher, buffers.bufferSource(), opt.get().getElementType().getColor(), entityRenderDispatcher.getPackedLightCoords(living, event.getPartialTick()));
        stack.popPose();
    }

    private static void renderOrbs(PoseStack stack, EntityRenderDispatcher entityRenderDispatcher, MultiBufferSource multiBufferSource, int elementColor, int packedLight) {
        int speed = 20;
        float deltaTick = (time / 2) % (speed * 3);

        for (int i = 0; i < 3; i++) {
            stack.pushPose();
            double angle = -(Math.PI / (speed * 1.5)) * (deltaTick + speed * i);
            double deltaX = (1.25 * Math.cos(angle));
            double deltaZ = (1.25 * Math.sin(angle));
            stack.translate(deltaX, 0.47, deltaZ);

            stack.mulPose(entityRenderDispatcher.cameraOrientation());
            stack.mulPose(Axis.YP.rotationDegrees(180));
            stack.scale(0.6F, 0.6F, 0.6F);
            VertexConsumer builder = multiBufferSource.getBuffer(RenderType.itemEntityTranslucentCull(ELEMENTAL_ORB_TEXTURE));
            PoseStack.Pose last = stack.last();
            vertex(builder, last, -0.5F, -0.25F, elementColor, 0, 1, packedLight);
            vertex(builder, last, 0.5F, -0.25F, elementColor, 1, 1, packedLight);
            vertex(builder, last, 0.5F, 0.75F, elementColor, 1, 0, packedLight);
            vertex(builder, last, -0.5F, 0.75F, elementColor, 0, 0, packedLight);
            stack.popPose();
        }
    }

    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, float x, float y, int color, float u, float v, int packedLight) {
        builder.vertex(pose.pose(), x, y, 0.0F).color(red(color), green(color), blue(color), 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(pose.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    // Cool way to disable client input

    /*@SubscribeEvent
    public static void modifyMovementInputs(MovementInputUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();

        boolean lockInputs = false;

        if (!PoisonEffect.isImmune(living)) {
            if (living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonEffectInstance poison) {
                if (poison.doConvulsions()) {
                    lockInputs = true;
                }
            }
        }

        if (lockInputs) {
            event.getMovementInput().forwardImpulse = 0;
            event.getMovementInput().leftImpulse = 0;
        }
    }*/
}
