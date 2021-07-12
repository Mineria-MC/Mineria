package com.mineria.mod.effects;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class PoisonEffectInstance extends CustomEffectInstance
{
    private int potionClass;
    private final IPoisonEffect potion;
    private final PoisonSource poisonSource;

    public PoisonEffectInstance(int potionClass, int duration, int amplifier, PoisonSource source)
    {
        this(potionClass, duration, duration, amplifier, source);
    }

    public PoisonEffectInstance(int potionClass, int duration, int maxDuration, int amplifier, PoisonSource source)
    {
        super(Effects.POISON, duration, maxDuration, amplifier, false, true, true);
        this.potionClass = potionClass;
        this.potion = (IPoisonEffect) Effects.POISON;
        this.poisonSource = source;
    }

    @Override
    public boolean combine(EffectInstance effectInstance)
    {
        if (effectInstance instanceof PoisonEffectInstance)
        {
            PoisonEffectInstance other = (PoisonEffectInstance) effectInstance;

            boolean combined = false;
            if(other.potionClass > this.potionClass)
            {
                this.potionClass = other.potionClass;
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            }
            else if (other.amplifier > this.amplifier)
            {
                if(other.potionClass == this.potionClass)
                {
                    this.amplifier = other.amplifier;
                    this.maxDuration = other.maxDuration;
                    this.duration = other.duration;
                    combined = true;
                }
            }
            else if(other.maxDuration > this.maxDuration)
            {
                if(other.potionClass == this.potionClass && other.amplifier == this.amplifier)
                {
                    this.maxDuration = other.maxDuration;
                    this.duration = other.duration;
                    combined = true;
                }
            }
            else if (other.duration > this.duration)
            {
                if (other.potionClass == this.potionClass && other.amplifier == this.amplifier && other.maxDuration == this.maxDuration)
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

    public int getPotionClass()
    {
        return potionClass;
    }

    @Override
    public boolean isReady()
    {
        return this.potion.isReady(this.duration, this.amplifier, this.potionClass);
    }

    @Override
    public void performEffect(LivingEntity living)
    {
        if (this.duration > 0)
        {
            this.potion.performEffect(living, this.amplifier, this.duration, this.maxDuration, this.potionClass);
        }
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return this.potion.getCurativeItems(this.potionClass, this.amplifier, this.maxDuration, this.duration, this.poisonSource);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("PotionClass", this.potionClass);
        nbt.putInt("PoisonSource", this.poisonSource.getId());
        return nbt;
    }

    public PoisonSource getPoisonSource()
    {
        return poisonSource;
    }

    @Override
    public void drawPotionName(FontRenderer font, MatrixStack matrixStack, float x, float y)
    {
        font.drawTextWithShadow(matrixStack, this.poisonSource.getTranslationComponent(this.potionClass, this.amplifier), x, y, 16777215);
    }

    public static void applyPoisonEffect(LivingEntity living, int potionClass, int duration, int amplifier, PoisonSource source)
    {
        living.addPotionEffect(new PoisonEffectInstance(potionClass, duration, amplifier, source));
        living.addPotionEffect(new CustomEffectInstance(Effects.NAUSEA, duration, duration, Math.min(potionClass, 2), false, false, false) {
            @Override
            public boolean shouldRender()
            {
                return false;
            }
        });
        if(potionClass > 0)
            living.addPotionEffect(new CustomEffectInstance(Effects.SLOWNESS, duration, duration, Math.min(potionClass - 1, 1), false, false, false) {
                @Override
                public boolean shouldRender()
                {
                    return false;
                }
            });
    }
}
