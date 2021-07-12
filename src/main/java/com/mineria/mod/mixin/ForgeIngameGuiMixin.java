package com.mineria.mod.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ALL;

@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin extends IngameGui
{
    @Shadow private RenderGameOverlayEvent eventParent;

    @Shadow public static boolean renderHealthMount;

    @Shadow public static boolean renderFood;

    @Shadow public static boolean renderJumpBar;

    @Shadow public static int right_height;

    @Shadow public static int left_height;

    @Shadow protected abstract boolean pre(RenderGameOverlayEvent.ElementType type, MatrixStack mStack);

    @Shadow private FontRenderer fontrenderer;

    @Shadow public static boolean renderVignette;

    @Shadow public static boolean renderHealth;

    @Shadow protected abstract void renderHelmet(float partialTicks, MatrixStack mStack);

    @Shadow public static boolean renderPortal;

    @Shadow public static boolean renderSpectatorTooltip;

    @Shadow public static boolean renderHotbar;

    @Shadow public static boolean renderCrosshairs;

    @Shadow public static boolean renderBossHealth;

    @Shadow protected abstract void renderBossHealth(MatrixStack mStack);

    @Shadow public static boolean renderArmor;

    @Shadow protected abstract void renderArmor(MatrixStack mStack, int width, int height);

    @Shadow public abstract void renderHealth(int width, int height, MatrixStack mStack);

    @Shadow public abstract void renderFood(int width, int height, MatrixStack mStack);

    @Shadow protected abstract void renderHealthMount(int width, int height, MatrixStack mStack);

    @Shadow public static boolean renderAir;

    @Shadow protected abstract void renderAir(int width, int height, MatrixStack mStack);

    @Shadow public static boolean renderExperiance;

    @Shadow protected abstract void renderExperience(int x, MatrixStack mStack);

    @Shadow protected abstract void renderSleepFade(int width, int height, MatrixStack mStack);

    @Shadow protected abstract void renderHUDText(int width, int height, MatrixStack mStack);

    @Shadow protected abstract void renderFPSGraph(MatrixStack mStack);

    @Shadow protected abstract void renderRecordOverlay(int width, int height, float partialTicks, MatrixStack mStack);

    @Shadow protected abstract void renderSubtitles(MatrixStack mStack);

    @Shadow protected abstract void renderTitle(int width, int height, float partialTicks, MatrixStack mStack);

    @Shadow public static boolean renderObjective;

    @Shadow protected abstract void renderChat(int width, int height, MatrixStack mStack);

    @Shadow protected abstract void renderPlayerList(int width, int height, MatrixStack mStack);

    @Shadow protected abstract void post(RenderGameOverlayEvent.ElementType type, MatrixStack mStack);

    public ForgeIngameGuiMixin(Minecraft mcIn)
    {
        super(mcIn);
    }

    /**
     * @reason Injections partially working
     * @author LGatodu47
     */
    @Overwrite
    public void renderIngameGui(MatrixStack mStack, float partialTicks)
    {
        this.scaledWidth = this.mc.getMainWindow().getScaledWidth();
        this.scaledHeight = this.mc.getMainWindow().getScaledHeight();
        eventParent = new RenderGameOverlayEvent(mStack, partialTicks, this.mc.getMainWindow());
        renderHealthMount = mc.player.getRidingEntity() instanceof LivingEntity;
        renderFood = !renderHealthMount;
        renderJumpBar = mc.player.isRidingHorse();

        right_height = 39;
        left_height = 39;

        if (pre(ALL, mStack)) return;

        fontrenderer = mc.fontRenderer;
        //mc.entityRenderer.setupOverlayRendering();
        RenderSystem.enableBlend();
        if (renderVignette && Minecraft.isFancyGraphicsEnabled())
        {
            renderVignette(mc.getRenderViewEntity());
        }
        else
        {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }

        if (renderHealth) renderHelmet(partialTicks, mStack);

        if (renderPortal && !(this.mc.player.isPotionActive(Effects.NAUSEA) || this.mc.player.isPotionActive(Effects.POISON)))
        {
            renderPortal(partialTicks);
        }

        if (this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR)
        {
            if (renderSpectatorTooltip) spectatorGui.func_238528_a_(mStack, partialTicks);
        }
        else if (!this.mc.gameSettings.hideGUI)
        {
            if (renderHotbar) renderHotbar(partialTicks, mStack);
        }

        if (!this.mc.gameSettings.hideGUI) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            setBlitOffset(-90);
            rand.setSeed((long)(ticks * 312871));

            if (renderCrosshairs) renderCrosshair(mStack);
            if (renderBossHealth) renderBossHealth(mStack);

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (this.mc.playerController.shouldDrawHUD() && this.mc.getRenderViewEntity() instanceof PlayerEntity)
            {
                if (renderHealth) renderHealth(this.scaledWidth, this.scaledHeight, mStack);
                if (renderArmor)  renderArmor(mStack, this.scaledWidth, this.scaledHeight);
                if (renderFood)   renderFood(this.scaledWidth, this.scaledHeight, mStack);
                if (renderHealthMount) renderHealthMount(this.scaledWidth, this.scaledHeight, mStack);
                if (renderAir)    renderAir(this.scaledWidth, this.scaledHeight, mStack);
            }

            if (renderJumpBar)
            {
                renderHorseJumpBar(mStack, this.scaledWidth / 2 - 91);
            }
            else if (renderExperiance)
            {
                renderExperience(this.scaledWidth / 2 - 91, mStack);
            }
            if (this.mc.gameSettings.heldItemTooltips && this.mc.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                this.renderItemName(mStack);
            } else if (this.mc.player.isSpectator()) {
                this.spectatorGui.func_238527_a_(mStack);
            }
        }

        renderSleepFade(this.scaledWidth, this.scaledHeight, mStack);

        renderHUDText(this.scaledWidth, this.scaledHeight, mStack);
        renderFPSGraph(mStack);
        renderPotionIcons(mStack);
        if (!mc.gameSettings.hideGUI) {
            renderRecordOverlay(this.scaledWidth, this.scaledHeight, partialTicks, mStack);
            renderSubtitles(mStack);
            renderTitle(this.scaledWidth, this.scaledHeight, partialTicks, mStack);
        }

        Scoreboard scoreboard = this.mc.world.getScoreboard();
        ScoreObjective objective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.player.getScoreboardName());
        if (scoreplayerteam != null)
        {
            int slot = scoreplayerteam.getColor().getColorIndex();
            if (slot >= 0) objective = scoreboard.getObjectiveInDisplaySlot(3 + slot);
        }
        ScoreObjective scoreobjective1 = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);
        if (renderObjective && scoreobjective1 != null)
        {
            this.renderScoreboard(mStack, scoreobjective1);
        }

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        RenderSystem.disableAlphaTest();

        renderChat(this.scaledWidth, this.scaledHeight, mStack);

        renderPlayerList(this.scaledWidth, this.scaledHeight, mStack);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableAlphaTest();

        post(ALL, mStack);
    }
}
