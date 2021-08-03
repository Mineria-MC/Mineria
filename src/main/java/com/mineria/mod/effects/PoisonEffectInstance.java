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
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class PoisonEffectInstance extends CustomEffectInstance
{
    private int potionClass;
    private final IPoisonEffect potion;
    private PoisonSource poisonSource;

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
                    this.duration = Math.max(this.duration + other.duration, 24000);
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

            if(other.poisonSource != this.poisonSource)
            {
                this.poisonSource = other.poisonSource;
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
    public ResourceLocation getSerializerName()
    {
        return new ResourceLocation(References.MODID, "poison");
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

    public boolean doSpasms()
    {
        return this.potion.doSpasms(this.duration, this.maxDuration, this.potionClass);
    }

    public boolean doConvulsions()
    {
        return this.potion.doConvulsions(this.duration, this.maxDuration, this.potionClass);
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
        ITextComponent txt = this.poisonSource.getTranslationComponent(this.potionClass, this.amplifier).appendString(" - ").appendString(EffectUtils.getPotionDurationString(this, 1.0F));
        int width = font.getStringPropertyWidth(txt);
        font.drawTextWithShadow(matrixStack, txt, x - (int) (width / 2), y, 16727643);
    }

    public void onPotionCured(LivingEntity living)
    {
        this.poisonSource.onPotionCured(living);
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

    public static PoisonEffectInstance merge(PoisonEffectInstance poison, CustomEffectInstance custom)
    {
        return new PoisonEffectInstance(poison.getPotionClass(), custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), poison.getPoisonSource());
    }

    public static class Serializer implements IEffectInstanceSerializer<PoisonEffectInstance>
    {
        @Override
        public ResourceLocation getName()
        {
            return new ResourceLocation(References.MODID, "poison");
        }

        @Override
        public void encodePacket(PoisonEffectInstance effect, PacketBuffer buf)
        {
            IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).encodePacket(effect, buf);
            buf.writeInt(effect.getPotionClass());
            buf.writeInt(effect.getPoisonSource().getId());
        }

        @Override
        public PoisonEffectInstance decodePacket(PacketBuffer buf)
        {
            CustomEffectInstance custom = IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).decodePacket(buf);
            PoisonEffectInstance poison = new PoisonEffectInstance(buf.readInt(), 0, 0, PoisonSource.byId(buf.readInt()));

            return PoisonEffectInstance.merge(poison, custom);
        }

        @Override
        public PoisonEffectInstance deserialize(Effect effect, CompoundNBT nbt)
        {
            CustomEffectInstance custom = IEffectInstanceSerializer.<CustomEffectInstance>getSerializer(new ResourceLocation(References.MODID, "custom")).deserialize(effect, nbt);
            PoisonEffectInstance poison = new PoisonEffectInstance(nbt.getInt("PotionClass"), 0, 0, PoisonSource.byId(nbt.getInt("PoisonSource")));

            return IEffectInstanceSerializer.readCurativeItems(PoisonEffectInstance.merge(poison, custom), nbt);
        }
    }
}
