package com.mineria.mod.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.client.gui.overlay.SubtitleOverlayGui;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IngameGui.class)
public abstract class IngameGuiMixin
{
    @Shadow protected int scaledWidth;

    @Shadow protected int scaledHeight;

    @Shadow @Final protected Minecraft mc;

    @Shadow protected abstract void renderVignette(Entity entityIn);

    @Shadow protected abstract void renderPumpkinOverlay();

    @Shadow protected abstract void renderPortal(float timeInPortal);

    @Shadow @Final protected SpectatorGui spectatorGui;

    @Shadow protected abstract void renderHotbar(float partialTicks, MatrixStack matrixStack);

    @Shadow public abstract FontRenderer getFontRenderer();

    @Shadow @Final protected BossOverlayGui overlayBoss;

    @Shadow protected abstract void renderCrosshair(MatrixStack matrixStack);

    @Shadow protected abstract void renderToolbarInfo(MatrixStack matrixStack);

    @Shadow protected abstract void renderMountHealth(MatrixStack matrixStack);

    @Shadow public abstract void renderHorseJumpBar(MatrixStack matrixStack, int xPosition);

    @Shadow public abstract void renderExpBar(MatrixStack matrixStack, int xPos);

    @Shadow public abstract void renderItemName(MatrixStack matrixStack);

    @Shadow public abstract void renderDemoCountdown(MatrixStack matrixStack);

    @Shadow protected abstract void renderPotionIcons(MatrixStack matrixStack);

    @Shadow @Final protected DebugOverlayGui overlayDebug;

    @Shadow @Nullable protected ITextComponent overlayMessage;

    @Shadow protected int overlayMessageTime;

    @Shadow protected boolean animateOverlayMessageColor;

    @Shadow protected abstract void renderChatBackground(MatrixStack matrixStack, FontRenderer renderer, int p_238448_3_, int p_238448_4_, int p_238448_5_);

    @Shadow @Nullable protected ITextComponent displayedTitle;

    @Shadow protected int titlesTimer;

    @Shadow protected int titleFadeOut;

    @Shadow protected int titleDisplayTime;

    @Shadow protected int titleFadeIn;

    @Shadow @Nullable protected ITextComponent displayedSubTitle;

    @Shadow @Final protected SubtitleOverlayGui overlaySubtitle;

    @Shadow protected abstract void renderScoreboard(MatrixStack p_238447_1_, ScoreObjective p_238447_2_);

    @Shadow @Final protected NewChatGui persistantChatGUI;

    @Shadow @Final protected PlayerTabOverlayGui overlayPlayerList;

    @Shadow protected int ticks;

    /**
     * @reason Injections partially works
     * @author LGatodu47
     */
    @Overwrite
    public void renderIngameGui(MatrixStack matrixStack, float partialTicks) {
        this.scaledWidth = this.mc.getMainWindow().getScaledWidth();
        this.scaledHeight = this.mc.getMainWindow().getScaledHeight();
        FontRenderer fontrenderer = this.getFontRenderer();
        RenderSystem.enableBlend();
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(this.mc.getRenderViewEntity());
        } else {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }

        ItemStack itemstack = this.mc.player.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.getPointOfView().func_243192_a() && itemstack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            this.renderPumpkinOverlay();
        }

        float f = MathHelper.lerp(partialTicks, this.mc.player.prevTimeInPortal, this.mc.player.timeInPortal);
        if (f > 0.0F && !(this.mc.player.isPotionActive(Effects.NAUSEA) || this.mc.player.isPotionActive(Effects.POISON))) {
            this.renderPortal(f);
        }

        if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
            this.spectatorGui.func_238528_a_(matrixStack, partialTicks);
        } else if (!this.mc.gameSettings.hideGUI) {
            this.renderHotbar(partialTicks, matrixStack);
        }

        if (!this.mc.gameSettings.hideGUI) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(IngameGui.GUI_ICONS_LOCATION);
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            this.renderCrosshair(matrixStack);
            RenderSystem.defaultBlendFunc();
            this.mc.getProfiler().startSection("bossHealth");
            this.overlayBoss.func_238484_a_(matrixStack);
            this.mc.getProfiler().endSection();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(IngameGui.GUI_ICONS_LOCATION);
            if (this.mc.playerController.shouldDrawHUD()) {
                this.renderToolbarInfo(matrixStack);
            }

            this.renderMountHealth(matrixStack);
            RenderSystem.disableBlend();
            int i = this.scaledWidth / 2 - 91;
            if (this.mc.player.isRidingHorse()) {
                this.renderHorseJumpBar(matrixStack, i);
            } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
                this.renderExpBar(matrixStack, i);
            }

            if (this.mc.gameSettings.heldItemTooltips && this.mc.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                this.renderItemName(matrixStack);
            } else if (this.mc.player.isSpectator()) {
                this.spectatorGui.func_238527_a_(matrixStack);
            }
        }

        if (this.mc.player.getSleepTimer() > 0) {
            this.mc.getProfiler().startSection("sleep");
            RenderSystem.disableDepthTest();
            RenderSystem.disableAlphaTest();
            float f2 = (float)this.mc.player.getSleepTimer();
            float f1 = f2 / 100.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F - (f2 - 100.0F) / 10.0F;
            }

            int j = (int)(220.0F * f1) << 24 | 1052704;
            AbstractGui.fill(matrixStack, 0, 0, this.scaledWidth, this.scaledHeight, j);
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
            this.mc.getProfiler().endSection();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (this.mc.isDemo()) {
            this.renderDemoCountdown(matrixStack);
        }

        this.renderPotionIcons(matrixStack);
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.render(matrixStack);
        }

        if (!this.mc.gameSettings.hideGUI) {
            if (this.overlayMessage != null && this.overlayMessageTime > 0) {
                this.mc.getProfiler().startSection("overlayMessage");
                float f3 = (float)this.overlayMessageTime - partialTicks;
                int i1 = (int)(f3 * 255.0F / 20.0F);
                if (i1 > 255) {
                    i1 = 255;
                }

                if (i1 > 8) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight - 68), 0.0F);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int k1 = 16777215;
                    if (this.animateOverlayMessageColor) {
                        k1 = MathHelper.hsvToRGB(f3 / 50.0F, 0.7F, 0.6F) & 16777215;
                    }

                    int k = i1 << 24 & -16777216;
                    int l = fontrenderer.getStringPropertyWidth(this.overlayMessage);
                    this.renderChatBackground(matrixStack, fontrenderer, -4, l, 16777215 | k);
                    fontrenderer.drawText(matrixStack, this.overlayMessage, (float)(-l / 2), -4.0F, k1 | k);
                    RenderSystem.disableBlend();
                    RenderSystem.popMatrix();
                }

                this.mc.getProfiler().endSection();
            }

            if (this.displayedTitle != null && this.titlesTimer > 0) {
                this.mc.getProfiler().startSection("titleAndSubtitle");
                float f4 = (float)this.titlesTimer - partialTicks;
                int j1 = 255;
                if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                    float f5 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f4;
                    j1 = (int)(f5 * 255.0F / (float)this.titleFadeIn);
                }

                if (this.titlesTimer <= this.titleFadeOut) {
                    j1 = (int)(f4 * 255.0F / (float)this.titleFadeOut);
                }

                j1 = MathHelper.clamp(j1, 0, 255);
                if (j1 > 8) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)(this.scaledWidth / 2), (float)(this.scaledHeight / 2), 0.0F);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.pushMatrix();
                    RenderSystem.scalef(4.0F, 4.0F, 4.0F);
                    int l1 = j1 << 24 & -16777216;
                    int i2 = fontrenderer.getStringPropertyWidth(this.displayedTitle);
                    this.renderChatBackground(matrixStack, fontrenderer, -10, i2, 16777215 | l1);
                    fontrenderer.drawTextWithShadow(matrixStack, this.displayedTitle, (float)(-i2 / 2), -10.0F, 16777215 | l1);
                    RenderSystem.popMatrix();
                    if (this.displayedSubTitle != null) {
                        RenderSystem.pushMatrix();
                        RenderSystem.scalef(2.0F, 2.0F, 2.0F);
                        int k2 = fontrenderer.getStringPropertyWidth(this.displayedSubTitle);
                        this.renderChatBackground(matrixStack, fontrenderer, 5, k2, 16777215 | l1);
                        fontrenderer.drawTextWithShadow(matrixStack, this.displayedSubTitle, (float)(-k2 / 2), 5.0F, 16777215 | l1);
                        RenderSystem.popMatrix();
                    }

                    RenderSystem.disableBlend();
                    RenderSystem.popMatrix();
                }

                this.mc.getProfiler().endSection();
            }

            this.overlaySubtitle.render(matrixStack);
            Scoreboard scoreboard = this.mc.world.getScoreboard();
            ScoreObjective scoreobjective = null;
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.player.getScoreboardName());
            if (scoreplayerteam != null) {
                int j2 = scoreplayerteam.getColor().getColorIndex();
                if (j2 >= 0) {
                    scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j2);
                }
            }

            ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
            if (scoreobjective1 != null) {
                this.renderScoreboard(matrixStack, scoreobjective1);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableAlphaTest();
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, (float)(this.scaledHeight - 48), 0.0F);
            this.mc.getProfiler().startSection("chat");
            this.persistantChatGUI.func_238492_a_(matrixStack, this.ticks);
            this.mc.getProfiler().endSection();
            RenderSystem.popMatrix();
            scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
            if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning() && this.mc.player.connection.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
                this.overlayPlayerList.setVisible(false);
            } else {
                this.overlayPlayerList.setVisible(true);
                this.overlayPlayerList.func_238523_a_(matrixStack, this.scaledWidth, scoreboard, scoreobjective1);
            }
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableAlphaTest();
    }
}
