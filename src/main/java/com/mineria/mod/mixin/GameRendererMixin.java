package com.mineria.mod.mixin;

import com.mineria.mod.effects.PoisonEffectInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Locale;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin
{
    @Shadow @Final private Minecraft mc;

    @Shadow private long prevFrameTime;

    @Shadow private long timeWorldIcon;

    @Shadow protected abstract void createWorldIcon();

    @Shadow @Nullable private ShaderGroup shaderGroup;

    @Shadow private boolean useShader;

    @Shadow protected abstract void func_243497_c(float p_243497_1_);

    @Shadow protected abstract void renderItemActivation(int widthsp, int heightScaled, float partialTicks);

    @Shadow @Final private LightTexture lightmapTexture;

    @Shadow public abstract void getMouseOver(float partialTicks);

    @Shadow protected abstract boolean isDrawBlockOutline();

    @Shadow @Final private ActiveRenderInfo activeRender;

    @Shadow private float farPlaneDistance;

    @Shadow public abstract Matrix4f getProjectionMatrix(ActiveRenderInfo activeRenderInfoIn, float partialTicks, boolean useFovSetting);

    @Shadow protected abstract void hurtCameraEffect(MatrixStack matrixStackIn, float partialTicks);

    @Shadow protected abstract void applyBobbing(MatrixStack matrixStackIn, float partialTicks);

    @Shadow private int rendererUpdateCount;

    @Shadow public abstract void resetProjectionMatrix(Matrix4f matrixIn);

    @Shadow private boolean renderHand;

    @Shadow protected abstract void renderHand(MatrixStack matrixStackIn, ActiveRenderInfo activeRenderInfoIn, float partialTicks);

    /**
     * @reason Injections partially working
     * @author LGatodu47
     */
    @Overwrite
    public void updateCameraAndRender(float partialTicks, long nanoTime, boolean renderWorldIn)
    {
        if (!this.mc.isGameFocused() && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !this.mc.mouseHelper.isRightDown()))
        {
            if (Util.milliTime() - this.prevFrameTime > 500L)
            {
                this.mc.displayInGameMenu(false);
            }
        } else
        {
            this.prevFrameTime = Util.milliTime();
        }

        if (!this.mc.skipRenderWorld)
        {
            int i = (int) (this.mc.mouseHelper.getMouseX() * (double) this.mc.getMainWindow().getScaledWidth() / (double) this.mc.getMainWindow().getWidth());
            int j = (int) (this.mc.mouseHelper.getMouseY() * (double) this.mc.getMainWindow().getScaledHeight() / (double) this.mc.getMainWindow().getHeight());
            RenderSystem.viewport(0, 0, this.mc.getMainWindow().getFramebufferWidth(), this.mc.getMainWindow().getFramebufferHeight());
            if (renderWorldIn && this.mc.world != null)
            {
                this.mc.getProfiler().startSection("level");
                this.renderWorld(partialTicks, nanoTime, new MatrixStack());
                if (this.mc.isSingleplayer() && this.timeWorldIcon < Util.milliTime() - 1000L)
                {
                    this.timeWorldIcon = Util.milliTime();
                    if (!this.mc.getIntegratedServer().isWorldIconSet())
                    {
                        this.createWorldIcon();
                    }
                }

                this.mc.worldRenderer.renderEntityOutlineFramebuffer();
                if (this.shaderGroup != null && this.useShader)
                {
                    RenderSystem.disableBlend();
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableAlphaTest();
                    RenderSystem.enableTexture();
                    RenderSystem.matrixMode(5890);
                    RenderSystem.pushMatrix();
                    RenderSystem.loadIdentity();
                    this.shaderGroup.render(partialTicks);
                    RenderSystem.popMatrix();
                    RenderSystem.enableTexture(); //FORGE: Fix MC-194675
                }

                this.mc.getFramebuffer().bindFramebuffer(true);
            }

            MainWindow mainwindow = this.mc.getMainWindow();
            RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);
            RenderSystem.matrixMode(5889);
            RenderSystem.loadIdentity();
            RenderSystem.ortho(0.0D, (double) mainwindow.getFramebufferWidth() / mainwindow.getGuiScaleFactor(), (double) mainwindow.getFramebufferHeight() / mainwindow.getGuiScaleFactor(), 0.0D, 1000.0D, 3000.0D);
            RenderSystem.matrixMode(5888);
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
            RenderHelper.setupGui3DDiffuseLighting();
            MatrixStack matrixstack = new MatrixStack();
            if (renderWorldIn && this.mc.world != null)
            {
                this.mc.getProfiler().endStartSection("gui");
                if (this.mc.player != null)
                {
                    float f = MathHelper.lerp(partialTicks, this.mc.player.prevTimeInPortal, this.mc.player.timeInPortal);
                    if (f > 0.0F && (this.mc.player.isPotionActive(Effects.NAUSEA) || /*(this.mc.player.isPotionActive(Effects.POISON) && this.mc.player.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)*/ this.mc.player.isPotionActive(Effects.POISON)) && this.mc.gameSettings.screenEffectScale < 1.0F)
                    {
                        this.func_243497_c(f * (1.0F - this.mc.gameSettings.screenEffectScale));
                    }
                }

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null)
                {
                    RenderSystem.defaultAlphaFunc();
                    this.renderItemActivation(this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight(), partialTicks);
                    this.mc.ingameGUI.renderIngameGui(matrixstack, partialTicks);
                    RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);
                }

                this.mc.getProfiler().endSection();
            }

            if (this.mc.loadingGui != null)
            {
                try
                {
                    this.mc.loadingGui.render(matrixstack, i, j, this.mc.getTickLength());
                } catch (Throwable throwable1)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Rendering overlay");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Overlay render details");
                    crashreportcategory.addDetail("Overlay name", () ->
                    {
                        return this.mc.loadingGui.getClass().getCanonicalName();
                    });
                    throw new ReportedException(crashreport);
                }
            } else if (this.mc.currentScreen != null)
            {
                try
                {
                    net.minecraftforge.client.ForgeHooksClient.drawScreen(this.mc.currentScreen, matrixstack, i, j, this.mc.getTickLength());
                } catch (Throwable throwable)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Screen render details");
                    crashreportcategory1.addDetail("Screen name", () ->
                    {
                        return this.mc.currentScreen.getClass().getCanonicalName();
                    });
                    crashreportcategory1.addDetail("Mouse location", () ->
                    {
                        return String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%f, %f)", i, j, this.mc.mouseHelper.getMouseX(), this.mc.mouseHelper.getMouseY());
                    });
                    crashreportcategory1.addDetail("Screen size", () ->
                    {
                        return String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f", this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight(), this.mc.getMainWindow().getFramebufferWidth(), this.mc.getMainWindow().getFramebufferHeight(), this.mc.getMainWindow().getGuiScaleFactor());
                    });
                    throw new ReportedException(crashreport1);
                }
            }
        }
    }

    /**
     * @reason Injections partially working
     * @author LGatodu47
     */
    @Overwrite
    public void renderWorld(float partialTicks, long finishTimeNano, MatrixStack matrixStackIn) {
        this.lightmapTexture.updateLightmap(partialTicks);
        if (this.mc.getRenderViewEntity() == null) {
            this.mc.setRenderViewEntity(this.mc.player);
        }

        this.getMouseOver(partialTicks);
        this.mc.getProfiler().startSection("center");
        boolean flag = this.isDrawBlockOutline();
        this.mc.getProfiler().endStartSection("camera");
        ActiveRenderInfo activerenderinfo = this.activeRender;
        this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.getLast().getMatrix().mul(this.getProjectionMatrix(activerenderinfo, partialTicks, true));
        this.hurtCameraEffect(matrixstack, partialTicks);
        if (this.mc.gameSettings.viewBobbing) {
            this.applyBobbing(matrixstack, partialTicks);
        }

        float f = MathHelper.lerp(partialTicks, this.mc.player.prevTimeInPortal, this.mc.player.timeInPortal) * this.mc.gameSettings.screenEffectScale * this.mc.gameSettings.screenEffectScale;
        if (f > 0.0F) {
            int i = this.mc.player.isPotionActive(Effects.NAUSEA) || /*(this.mc.player.isPotionActive(Effects.POISON) && this.mc.player.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)*/ this.mc.player.isPotionActive(Effects.POISON) ? 7 : 20;
            float f1 = 5.0F / (f * f + 5.0F) - f * 0.04F;
            f1 = f1 * f1;
            Vector3f vector3f = new Vector3f(0.0F, MathHelper.SQRT_2 / 2.0F, MathHelper.SQRT_2 / 2.0F);
            matrixstack.rotate(vector3f.rotationDegrees(((float)this.rendererUpdateCount + partialTicks) * (float)i));
            matrixstack.scale(1.0F / f1, 1.0F, 1.0F);
            float f2 = -((float)this.rendererUpdateCount + partialTicks) * (float)i;
            matrixstack.rotate(vector3f.rotationDegrees(f2));
        }

        Matrix4f matrix4f = matrixstack.getLast().getMatrix();
        this.resetProjectionMatrix(matrix4f);
        activerenderinfo.update(this.mc.world, (Entity)(this.mc.getRenderViewEntity() == null ? this.mc.player : this.mc.getRenderViewEntity()), !this.mc.gameSettings.getPointOfView().func_243192_a(), this.mc.gameSettings.getPointOfView().func_243193_b(), partialTicks);

        net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup((GameRenderer) (Object) this, activerenderinfo, partialTicks);
        activerenderinfo.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(cameraSetup.getRoll()));

        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(activerenderinfo.getPitch()));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(activerenderinfo.getYaw() + 180.0F));
        this.mc.worldRenderer.updateCameraAndRender(matrixStackIn, partialTicks, finishTimeNano, flag, activerenderinfo, (GameRenderer) (Object) this, this.lightmapTexture, matrix4f);
        this.mc.getProfiler().endStartSection("forge_render_last");
        net.minecraftforge.client.ForgeHooksClient.dispatchRenderLast(this.mc.worldRenderer, matrixStackIn, partialTicks, matrix4f, finishTimeNano);
        this.mc.getProfiler().endStartSection("hand");
        if (this.renderHand) {
            RenderSystem.clear(256, Minecraft.IS_RUNNING_ON_MAC);
            this.renderHand(matrixStackIn, activerenderinfo, partialTicks);
        }

        this.mc.getProfiler().endSection();
    }
}
