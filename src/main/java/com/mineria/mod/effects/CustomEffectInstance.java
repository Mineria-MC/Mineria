package com.mineria.mod.effects;

import com.mineria.mod.References;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class CustomEffectInstance extends EffectInstance
{
    protected Effect potion;
    protected int duration;
    protected int maxDuration;
    protected int amplifier;
    protected boolean ambient;
    protected boolean showParticles;
    protected boolean showIcon;
    protected List<ItemStack> curativeItems;

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
        this.curativeItems = new ArrayList<>();
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

    public ResourceLocation getSerializerName()
    {
        return new ResourceLocation(References.MODID, "custom");
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
        nbt.putInt("MaxDuration", this.maxDuration);
        nbt.putString("Serializer", this.getSerializerName().toString());
        return nbt;
    }

    public void drawPotionName(FontRenderer font, MatrixStack matrixStack, float x, float y)
    {
        font.drawTextWithShadow(matrixStack, new TranslationTextComponent(this.getEffectName()), x, y, 16777215);
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return curativeItems;
    }

    @Override
    public void setCurativeItems(List<ItemStack> curativeItems)
    {
        this.curativeItems = curativeItems;
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

    public static CustomEffectInstance merge(CustomEffectInstance custom, EffectInstance effect)
    {
        return makeCustomEffectInstance(effect.getPotion(), effect.getDuration(), custom.maxDuration, effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles(), effect.isShowIcon(), effect.shouldRender());
    }

    public static class Serializer implements IEffectInstanceSerializer<CustomEffectInstance>
    {
        @Override
        public ResourceLocation getName()
        {
            return new ResourceLocation(References.MODID, "custom");
        }

        @Override
        public void encodePacket(CustomEffectInstance effect, PacketBuffer buf)
        {
            IEffectInstanceSerializer.getSerializer(new ResourceLocation("default")).encodePacket(effect, buf);
            buf.writeInt(effect.getMaxDuration());
        }

        @Override
        public CustomEffectInstance decodePacket(PacketBuffer buf)
        {
            EffectInstance instance = IEffectInstanceSerializer.getSerializer(new ResourceLocation("default")).decodePacket(buf);
            CustomEffectInstance custom = new CustomEffectInstance(Effects.SPEED, 0, buf.readInt(), 0, false, false, false);

            return CustomEffectInstance.merge(custom, instance);
        }

        @Override
        public CustomEffectInstance deserialize(Effect effect, CompoundNBT nbt)
        {
            EffectInstance instance = IEffectInstanceSerializer.getSerializer(new ResourceLocation("default")).deserialize(effect, nbt);
            CustomEffectInstance custom = new CustomEffectInstance(Effects.SPEED, 0, nbt.getInt("MaxDuration"), 0, false, false, false);

            return IEffectInstanceSerializer.readCurativeItems(CustomEffectInstance.merge(custom, instance), nbt);
        }
    }
}
