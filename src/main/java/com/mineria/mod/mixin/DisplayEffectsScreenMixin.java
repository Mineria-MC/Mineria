package com.mineria.mod.mixin;

import com.mineria.mod.effects.CustomEffectInstance;
import com.mineria.mod.effects.PoisonEffectInstance;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.extensions.IForgeEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.stream.Collectors;

@Mixin(DisplayEffectsScreen.class)
public abstract class DisplayEffectsScreenMixin<T extends Container> extends ContainerScreen<T>
{
    @Shadow public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

    public DisplayEffectsScreenMixin(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    /**
     * @reason Could not do it with @Inject
     * @author LGatodu47
     */
    @Overwrite
    public void renderEffects(MatrixStack matrixStack)
    {
        int x = this.guiLeft - 124;
        Collection<EffectInstance> activeEffects = this.minecraft.player.getActivePotionEffects();
        if (!activeEffects.isEmpty())
        {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int y = 33;
            if (activeEffects.size() > 5)
            {
                y = 132 / (activeEffects.size() - 1);
            }

            Iterable<EffectInstance> effects = activeEffects.stream().filter(IForgeEffectInstance::shouldRender).sorted().collect(Collectors.toList());
            this.renderEffectBackground(matrixStack, x, y, effects);
            this.renderEffectSprites(matrixStack, x, y, effects);
            this.renderEffectText(matrixStack, x, y, effects);
        }
    }

    private void renderEffectBackground(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects)
    {
        this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        int y = this.guiTop;

        for (EffectInstance effect : effects)
        {
            int x;
            int width;
            int height;

            if(effect instanceof PoisonEffectInstance)
            {
                x = renderX - 40;
                width = 180;
                height = 32;
            } else
            {
                x = renderX;
                width = 140;
                height = 32;
            }

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            //this.blit(matrixStack, x, y, 0, 166, width, height);
            blit(matrixStack, x, y, width, height, 0, 166, 140, 32, 255, 255);
            y += yOffset;
        }
    }

    private void renderEffectSprites(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects)
    {
        PotionSpriteUploader spriteUploader = this.minecraft.getPotionSpriteUploader();
        int y = this.guiTop;

        for (EffectInstance instance : effects)
        {
            int x;

            if(instance instanceof PoisonEffectInstance)
                x = renderX - 40;
            else
                x = renderX;

            Effect effect = instance.getPotion();
            TextureAtlasSprite sprite = spriteUploader.getSprite(effect);
            this.minecraft.getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
            blit(matrixStack, x + 6, y + 7, this.getBlitOffset(), 18, 18, sprite);
            y += yOffset;
        }

    }

    private void renderEffectText(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects)
    {
        int y = this.guiTop;

        for (EffectInstance effect : effects)
        {
            if(effect instanceof CustomEffectInstance)
            {
                int x;
                int durationX;

                String duration = EffectUtils.getPotionDurationString(effect, 1.0F);

                if(effect instanceof PoisonEffectInstance)
                {
                    x = renderX - 40;
                    durationX = x + 180 - 20 - 18 - this.font.getStringWidth(duration);
                } else
                {
                    x = renderX;
                    durationX = x + 10 + 18;
                }

                ((CustomEffectInstance) effect).drawPotionName(this.font, matrixStack, (x + 10 + 18), (y + 6));
                this.font.drawStringWithShadow(matrixStack, duration, durationX, (y + 6 + 10), 8355711);
            }
            else
            {
                effect.renderInventoryEffect((DisplayEffectsScreen<T>) (Object) this, matrixStack, renderX, y, this.getBlitOffset());
                if (!effect.shouldRenderInvText())
                {
                    y += yOffset;
                    continue;
                }

                String name = I18n.format(effect.getPotion().getName());
                if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9)
                {
                    name = name + ' ' + I18n.format("enchantment.level." + (effect.getAmplifier() + 1));
                }

                this.font.drawStringWithShadow(matrixStack, name, (renderX + 10 + 18), (y + 6), 16777215);
                String duration = EffectUtils.getPotionDurationString(effect, 1.0F);
                this.font.drawStringWithShadow(matrixStack, duration, (renderX + 10 + 18), (y + 6 + 10), 8355711);
                y += yOffset;
            }
        }
    }

    /*
    @Overwrite
    public void renderEffectText(MatrixStack matrixStack, int renderX, int yOffset, Iterable<EffectInstance> effects)
    {
        //this.minecraft.player.getActivePotionEffects().stream().map(Object::getClass).map(Class::getSimpleName).forEach(System.out::println);

        int startY = this.guiTop;

        for(EffectInstance effect : effects)
        {
            if(effect instanceof CustomEffectInstance)
            {
                ((CustomEffectInstance) effect).drawPotionName(this.font, matrixStack, (renderX + 10 + 18), (startY + 6));
                String duration = EffectUtils.getPotionDurationString(effect, 1.0F);
                this.font.drawStringWithShadow(matrixStack, duration, (float)(renderX + 10 + 18), (float)(startY + 6 + 10), 8355711);
            }
            else
            {
                effect.renderInventoryEffect((DisplayEffectsScreen<T>) (Object) this, matrixStack, renderX, startY, this.getBlitOffset());

                if (!effect.shouldRenderInvText())
                {
                    startY += yOffset;
                    continue;
                }

                String name = I18n.format(effect.getPotion().getName());
                if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9)
                {
                    name = name + ' ' + I18n.format("enchantment.level." + (effect.getAmplifier() + 1));
                }

                this.font.drawStringWithShadow(matrixStack, name, (float)(renderX + 10 + 18), (float)(startY + 6), 16777215);
                String duration = EffectUtils.getPotionDurationString(effect, 1.0F);
                this.font.drawStringWithShadow(matrixStack, duration, (float)(renderX + 10 + 18), (float)(startY + 6 + 10), 8355711);
            }
            startY += yOffset;
        }
    }
    */

    /*private int renderCustomEffectText(MatrixStack matrixStack, int renderX, int yOffset, CustomEffectInstance effect, int startY)
    {
        effect.drawPotionName(this.font, matrixStack, (renderX + 10 + 18), (startY + 6));
        String s1 = EffectUtils.getPotionDurationString(effect, 1.0F);
        this.font.drawStringWithShadow(matrixStack, s1, (float)(renderX + 10 + 18), (float)(startY + 6 + 10), 8355711);
        return startY + yOffset;
    }*/
}
