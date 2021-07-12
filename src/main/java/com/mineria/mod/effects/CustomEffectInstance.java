package com.mineria.mod.effects;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomEffectInstance extends EffectInstance
{
    protected Effect potion;
    protected int duration;
    protected int maxDuration;
    protected int amplifier;
    protected boolean ambient;
    protected boolean showParticles;
    protected boolean showIcon;

    public CustomEffectInstance(Effect potion, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon)
    {
        super(potion, duration, amplifier, ambient, showParticles, showIcon);
        this.potion = potion;
        this.duration = duration;
        this.maxDuration = maxDuration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
    }

    @Override
    public boolean combine(EffectInstance effectInstance)
    {
        if (effectInstance instanceof CustomEffectInstance)
        {
            CustomEffectInstance other = (CustomEffectInstance) effectInstance;

            boolean combined = false;
            if (other.amplifier > this.amplifier)
            {
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            }
            else if(other.maxDuration > this.maxDuration)
            {
                if(other.amplifier == this.amplifier)
                {
                    this.maxDuration = other.maxDuration;
                    this.duration = other.duration;
                    combined = true;
                }
            }
            else if (other.duration > this.duration)
            {
                if (other.amplifier == this.amplifier && other.maxDuration == this.maxDuration)
                {
                    this.duration = other.duration;
                    combined = true;
                }
            }

            if (!other.ambient && this.ambient || combined)
            {
                this.ambient = other.ambient;
                combined = true;
            }

            if (other.showParticles != this.showParticles)
            {
                this.showParticles = other.showParticles;
                combined = true;
            }

            if (other.showIcon != this.showIcon)
            {
                this.showIcon = other.showIcon;
                combined = true;
            }

            return combined;
        }
        return false;
    }

    public Effect getPotion()
    {
        return this.potion;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public int getMaxDuration()
    {
        return maxDuration;
    }

    public int getAmplifier()
    {
        return this.amplifier;
    }

    public boolean isAmbient()
    {
        return this.ambient;
    }

    public boolean doesShowParticles()
    {
        return this.showParticles;
    }

    public boolean isShowIcon()
    {
        return this.showIcon;
    }

    @Override
    public boolean tick(LivingEntity living, Runnable onChangedPotionEffect)
    {
        if (this.duration > 0)
        {
            if (this.isReady())
                this.performEffect(living);

            --this.duration;
        }

        return this.duration > 0;
    }

    public boolean isReady()
    {
        return this.potion.isReady(this.duration, this.amplifier);
    }

    @Override
    public void performEffect(LivingEntity living)
    {
        if (duration > 0)
        {
            this.potion.performEffect(living, this.amplifier);
        }
    }

    @Override
    public String getEffectName()
    {
        return this.potion.getName();
    }

    public String toString()
    {
        String s;
        if (this.amplifier > 0)
        {
            s = this.getEffectName() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        } else
        {
            s = this.getEffectName() + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        }

        if (!this.showParticles)
        {
            s = s + ", Particles: false";
        }

        if (!this.showIcon)
        {
            s = s + ", Show Icon: false";
        }

        return s;
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        else if (!(obj instanceof EffectInstance))
            return false;
        else
        {
            CustomEffectInstance effect = (CustomEffectInstance) obj;
            return this.duration == effect.duration && this.amplifier == effect.amplifier && this.ambient == effect.ambient && this.potion.equals(effect.potion);
        }
    }

    public int hashCode()
    {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        return 31 * i + (this.ambient ? 1 : 0);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putBoolean("Custom", true);
        nbt.putInt("MaxDuration", this.maxDuration);
        nbt.putBoolean("ShouldRender", this.shouldRender());
        return nbt;
    }

    public void drawPotionName(FontRenderer font, MatrixStack matrixStack, float x, float y)
    {
        font.drawTextWithShadow(matrixStack, new TranslationTextComponent(this.getEffectName()), x, y, 16777215);
    }

    public static EffectInstance makeEffectInstance(Effect effect, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean shouldRender)
    {
        return new EffectInstance(effect, duration, amplifier, ambient, showParticles, showIcon) {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        };
    }

    public static CustomEffectInstance makeCustomEffectInstance(Effect effect, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean shouldRender)
    {
        return new CustomEffectInstance(effect, duration, maxDuration, amplifier, ambient, showParticles, showIcon) {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        };
    }
}
