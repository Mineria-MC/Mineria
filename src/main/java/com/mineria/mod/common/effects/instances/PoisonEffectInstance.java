package com.mineria.mod.common.effects.instances;

import com.mineria.mod.common.capabilities.CapabilityRegistry;
import com.mineria.mod.common.effects.*;
import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mineria.mod.common.effects.CustomEffectInstance.EffectUpdater;

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
        super(MobEffects.POISON, duration, maxDuration, amplifier, false, true, true, null);
        this.potionClass = potionClass;
        this.potion = (IPoisonEffect) MobEffects.POISON;
        this.poisonSource = source;
    }

    @Override
    public boolean update(MobEffectInstance effectInstance)
    {
        if (effectInstance instanceof PoisonEffectInstance other)
        {
            /*if(other.poisonSource != this.poisonSource)
                return false;*/
            return getEffectUpdater().updateEffect(this, other);
            /*boolean combined = false;
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
                    this.duration = Math.min(this.duration + other.duration, 24000);
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

            return combined;*/
        }
        return false;
    }

    private static EffectUpdater<PoisonEffectInstance> EFFECT_UPDATER;

    protected static EffectUpdater<PoisonEffectInstance> getEffectUpdater()
    {
        if(EFFECT_UPDATER == null)
        {
            EffectUpdater<PoisonEffectInstance> updater = new EffectUpdater<>();
            updater.compareOrderedInts(0, PoisonEffectInstance::getPotionClass, (inst, value) -> inst.potionClass = value);
            updater.compareOrderedInts(1, PoisonEffectInstance::getAmplifier, PoisonEffectInstance::setAmplifier);
            updater.compareOrderedInts(2, PoisonEffectInstance::getMaxDuration, (inst, value) -> inst.maxDuration = value);
            updater.compareOrderedInts(3, PoisonEffectInstance::getDuration, PoisonEffectInstance::setDuration);
            updater.compareBooleans(CustomEffectInstance::isAmbient, CustomEffectInstance::setAmbient);
            updater.compareBooleans(CustomEffectInstance::isVisible, CustomEffectInstance::setVisible);
            updater.compareBooleans(CustomEffectInstance::showIcon, CustomEffectInstance::setShowIcon);
            EFFECT_UPDATER = updater;
        }

        return EFFECT_UPDATER;
    }

    public int getPotionClass()
    {
        return potionClass;
    }

    @Override
    public IEffectInstanceSerializer<? extends CustomEffectInstance> getSerializer()
    {
        return MineriaEffectInstanceSerializers.POISON.get();
    }

    @Override
    public boolean isReady()
    {
        return this.potion.isDurationEffectTick(this.duration, this.amplifier, this.potionClass);
    }

    @Override
    public void applyEffect(LivingEntity living)
    {
        if (this.duration > 0)
        {
            this.potion.applyEffectTick(living, this.amplifier, this.duration, this.maxDuration, this.potionClass);
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
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("PotionClass", this.potionClass);
        nbt.putString("PoisonSource", this.poisonSource.getId().toString());
        return nbt;
    }

    public PoisonSource getPoisonSource()
    {
        return poisonSource;
    }

    @Override
    public void drawPotionName(Font font, PoseStack matrixStack, float x, float y)
    {
        Component txt = this.poisonSource.getDescription(this.potionClass, this.amplifier).append(" - ").append(MobEffectUtil.formatDuration(this, 1.0F));
        int width = font.width(txt);
        font.drawShadow(matrixStack, txt, x - (int) (width / 2), y, 16727643);
    }

    @Override
    public int getColor()
    {
        return this.poisonSource.getColor();
    }

    public void onPotionCured(LivingEntity living)
    {
        ((PoisonEffect) this.potion).removeMovementSpeedModifier(living);
        living.getCapability(CapabilityRegistry.POISON_EXPOSURE).ifPresent(cap -> cap.removeFromMap(this.poisonSource));
    }

    @Override
    public PoisonEffectInstance copy()
    {
        return Util.make(new PoisonEffectInstance(this.getPotionClass(), this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.getPoisonSource()), effect -> effect.setCurativeItems(this.getCurativeItems()));
    }

    public static void applyPoisonEffect(LivingEntity living, int potionClass, int duration, int amplifier, PoisonSource source)
    {
        getEffects(potionClass, duration, amplifier, source).forEach(living::addEffect);
    }

    public static Set<MobEffectInstance> getEffects(int potionClass, int duration, int amplifier, PoisonSource source)
    {
        Set<MobEffectInstance> result = new HashSet<>();
        result.add(new PoisonEffectInstance(potionClass, duration, amplifier, source));
        result.add(new CustomEffectInstance.Impl(MobEffects.CONFUSION, duration, duration, Math.min(potionClass, 2), false, false, false, MobEffects.POISON) {
            @Override
            public boolean shouldRender()
            {
                return false;
            }
        });
        if(potionClass > 0)
        {
            result.add(new CustomEffectInstance.Impl(MobEffects.MOVEMENT_SLOWDOWN, duration, duration, Math.min(potionClass - 1, 1), false, false, false, MobEffects.POISON) {
                @Override
                public boolean shouldRender()
                {
                    return false;
                }
            });
        }
        return result;
    }

    public static boolean isEntityAffected(LivingEntity living)
    {
        return living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonEffectInstance;
    }

    private static PoisonEffectInstance merge(PoisonEffectInstance poison, CustomEffectInstance custom)
    {
        return new PoisonEffectInstance(poison.getPotionClass(), custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), poison.getPoisonSource());
    }

    public static class Serializer extends ForgeRegistryEntry<IEffectInstanceSerializer<?>> implements IEffectInstanceSerializer<PoisonEffectInstance>
    {
        @Override
        public void encodePacket(PoisonEffectInstance effect, FriendlyByteBuf buf)
        {
            MineriaEffectInstanceSerializers.CUSTOM.get().encodePacket(effect, buf);
            buf.writeInt(effect.getPotionClass());
            buf.writeResourceLocation(effect.getPoisonSource().getId());
        }

        @Override
        public PoisonEffectInstance decodePacket(FriendlyByteBuf buf)
        {
            CustomEffectInstance custom = MineriaEffectInstanceSerializers.CUSTOM.get().decodePacket(buf);
            PoisonEffectInstance poison = new PoisonEffectInstance(buf.readInt(), 0, 0, PoisonSource.byName(buf.readResourceLocation()));

            return PoisonEffectInstance.merge(poison, custom);
        }

        @Override
        public PoisonEffectInstance deserialize(MobEffect effect, CompoundTag nbt)
        {
            CustomEffectInstance custom = MineriaEffectInstanceSerializers.CUSTOM.get().deserialize(effect, nbt);
            PoisonEffectInstance poison = new PoisonEffectInstance(nbt.getInt("PotionClass"), 0, 0, PoisonSource.byName(ResourceLocation.tryParse(nbt.getString("PoisonSource"))));

            return IEffectInstanceSerializer.readCurativeItems(PoisonEffectInstance.merge(poison, custom), nbt);
        }
    }
}
