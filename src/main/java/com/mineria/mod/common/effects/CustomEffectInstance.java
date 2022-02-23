package com.mineria.mod.common.effects;

import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

public abstract class CustomEffectInstance extends MobEffectInstance
{
    protected MobEffect potion;
    protected int duration;
    protected int maxDuration;
    protected int amplifier;
    protected boolean ambient;
    protected boolean showParticles;
    protected boolean showIcon;
    protected List<ItemStack> curativeItems;
    @Nullable
    protected final MobEffect parentEffect;

    public CustomEffectInstance(MobEffect potion, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable MobEffect parentEffect)
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
        this.parentEffect = parentEffect;
    }

    @Override
    public abstract boolean update(MobEffectInstance effectInstance);
    /*{
        if (effectInstance instanceof CustomEffectInstance)
        {
            return getEffectUpdater().updateEffect(this, (CustomEffectInstance) effectInstance);
            *//*CustomEffectInstance other = (CustomEffectInstance) effectInstance;

            boolean combined = false;
            if (other.amplifier > this.amplifier)
            {
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            } else if (other.maxDuration > this.maxDuration)
            {
                if (other.amplifier == this.amplifier)
                {
                    this.maxDuration = other.maxDuration;
                    this.duration = other.duration;
                    combined = true;
                }
            } else if (other.duration > this.duration)
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

            return combined;*//*
        }

        return false;
    }*/

    public MobEffect getEffect()
    {
        return this.potion;
    }

    public int getDuration()
    {
        return this.duration;
    }

    /*
    Let's allow mutability to avoid creating new instances when we want to modify existing one.
     */
    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public int getMaxDuration()
    {
        return maxDuration;
    }

    public int getAmplifier()
    {
        return this.amplifier;
    }

    public void setAmplifier(int amplifier)
    {
        this.amplifier = amplifier;
    }

    public boolean isAmbient()
    {
        return this.ambient;
    }

    public void setAmbient(boolean ambient)
    {
        this.ambient = ambient;
    }

    public boolean isVisible()
    {
        return this.showParticles;
    }

    public void setVisible(boolean showParticles)
    {
        this.showParticles = showParticles;
    }

    public boolean showIcon()
    {
        return this.showIcon;
    }

    public void setShowIcon(boolean showIcon)
    {
        this.showIcon = showIcon;
    }

    public boolean shouldRender()
    {
        return true;
    }

    public Optional<MobEffectInstance> getActiveParentEffect(LivingEntity living)
    {
        return Optional.ofNullable(this.parentEffect == null ? null : living.getEffect(this.parentEffect));
    }

    @Nullable
    public MobEffect getParentEffect()
    {
        return this.parentEffect;
    }

    public abstract IEffectInstanceSerializer<? extends CustomEffectInstance> getSerializer();

    @Override
    public boolean tick(LivingEntity living, Runnable onChangedPotionEffect)
    {
        if (this.duration > 0)
        {
            if (this.isReady())
                this.applyEffect(living);

            --this.duration;
        }

        return this.duration > 0;
    }

    public boolean isReady()
    {
        return this.potion.isDurationEffectTick(this.duration, this.amplifier);
    }

    @Override
    public void applyEffect(LivingEntity living)
    {
        if (duration > 0)
        {
            this.potion.applyEffectTick(living, this.amplifier);
        }
    }

    @Override
    public String getDescriptionId()
    {
        return this.potion.getDescriptionId();
    }

    @Override
    public String toString()
    {
        String text;
        if (this.amplifier > 0)
        {
            text = this.getDescriptionId() + " x " + (this.amplifier + 1) + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        } else
        {
            text = this.getDescriptionId() + ", Duration: " + this.duration + ", Max Duration: " + this.maxDuration;
        }

        if (!this.showParticles)
        {
            text = text + ", Particles: false";
        }

        if (!this.showIcon)
        {
            text = text + ", Show Icon: false";
        }

        return text;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        else if (!(obj instanceof MobEffectInstance))
            return false;
        else
        {
            CustomEffectInstance effect = (CustomEffectInstance) obj;
            return this.duration == effect.duration && this.amplifier == effect.amplifier && this.ambient == effect.ambient && this.potion.equals(effect.potion);
        }
    }

    @Override
    public int hashCode()
    {
        int i = this.potion.hashCode();
        i = 31 * i + this.duration;
        i = 31 * i + this.amplifier;
        return 31 * i + (this.ambient ? 1 : 0);
    }

    @Override
    public CompoundTag save(CompoundTag nbt)
    {
        super.save(nbt);
        nbt.putInt("MaxDuration", this.maxDuration);
        nbt.putString("Serializer", this.getSerializer().getRegistryName().toString());
        if(this.parentEffect != null) nbt.putString("ParentEffect", this.parentEffect.getRegistryName().toString());
        return nbt;
    }

    public void drawPotionName(Font font, PoseStack matrixStack, float x, float y)
    {
        font.drawShadow(matrixStack, new TranslatableComponent(this.getDescriptionId()), x, y, 16777215);
    }

    public int getColor()
    {
        return this.potion.getColor();
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

    public abstract CustomEffectInstance copy();

    public static class Impl extends CustomEffectInstance
    {
        public Impl(MobEffect potion, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable MobEffect parentEffect)
        {
            super(potion, duration, maxDuration, amplifier, ambient, showParticles, showIcon, parentEffect);
        }

        @Override
        public boolean update(MobEffectInstance effectInstance)
        {
            if(effectInstance instanceof Impl other)
            {
                if(other.parentEffect != this.parentEffect)
                    return false;
                return getEffectUpdater().updateEffect(this, other);
            }

            return false;
        }

        @Override
        public IEffectInstanceSerializer<? extends CustomEffectInstance> getSerializer()
        {
            return MineriaEffectInstanceSerializers.CUSTOM.get();
        }

        @Override
        public CustomEffectInstance copy()
        {
            return Util.make(makeCustomEffectInstance(this.getEffect(), this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.showIcon(), this.parentEffect, this.shouldRender()), effect -> effect.setCurativeItems(this.getCurativeItems()));
        }

        private static EffectUpdater<Impl> EFFECT_UPDATER;

        public static EffectUpdater<Impl> getEffectUpdater()
        {
            if(EFFECT_UPDATER == null)
            {
                EffectUpdater<Impl> updater = new EffectUpdater<>();
                updater.compareOrderedInts(0, CustomEffectInstance::getAmplifier, CustomEffectInstance::setAmplifier);
                updater.compareOrderedInts(1, CustomEffectInstance::getMaxDuration, (inst, value) -> inst.maxDuration = value);
                updater.compareOrderedInts(2, CustomEffectInstance::getDuration, CustomEffectInstance::setDuration);
                updater.compareBooleans(CustomEffectInstance::isAmbient, CustomEffectInstance::setAmbient);
                updater.compareBooleans(CustomEffectInstance::isVisible, CustomEffectInstance::setVisible);
                updater.compareBooleans(CustomEffectInstance::showIcon, CustomEffectInstance::setShowIcon);
                EFFECT_UPDATER = updater;
            }

            return EFFECT_UPDATER;
        }
    }

    public static CustomEffectInstance.Impl makeCustomEffectInstance(MobEffect effect, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable MobEffect parentEffect, boolean shouldRender)
    {
        return new CustomEffectInstance.Impl(effect, duration, maxDuration, amplifier, ambient, showParticles, showIcon, parentEffect)
        {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        };
    }

    public static CustomEffectInstance merge(CustomEffectInstance custom, MobEffectInstance effect)
    {
        return makeCustomEffectInstance(effect.getEffect(), effect.getDuration(), custom.maxDuration, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon(), custom.parentEffect, true);
    }

    public static MobEffectInstance copyEffect(MobEffectInstance effect)
    {
        return effect instanceof CustomEffectInstance ? ((CustomEffectInstance) effect).copy() : new MobEffectInstance(effect);
    }

    public static class EffectUpdater<T extends CustomEffectInstance>
    {
        private final List<Entry<T>> orderedEntries;
        private final Set<Entry<T>> entries;

        public EffectUpdater()
        {
            this.orderedEntries = new ArrayList<>();
            this.entries = new HashSet<>();
        }

        public EffectUpdater<T> compareOrdered(int comparisonOrder, Comparator<T> comparator, BiConsumer<T, T> setter)
        {
            orderedEntries.add(comparisonOrder, new Entry<>(comparator, setter));
            return this;
        }

        public EffectUpdater<T> compare(Comparator<T> comparator, BiConsumer<T, T> setter)
        {
            this.entries.add(new Entry<>(comparator, setter));
            return this;
        }

        public EffectUpdater<T> compareOrderedInts(int comparisonOrder, ToIntFunction<T> getter, ObjIntConsumer<T> setter)
        {
            return compareOrdered(comparisonOrder, Comparator.comparingInt(getter), (inst, other) -> setter.accept(inst, getter.applyAsInt(other)));
        }

        public EffectUpdater<T> compareBooleans(Function<T, Boolean> getter, BiConsumer<T, Boolean> setter)
        {
            return compare((o1, o2) -> Boolean.compare(getter.apply(o1), getter.apply(o2)), (inst, other) -> setter.accept(inst, getter.apply(other)));
        }

        public boolean updateEffect(T instance, T other)
        {
            boolean combined = false;
            for(int i = 0; i < this.orderedEntries.size(); i++)
            {
                EffectUpdater.Entry<T> entry = this.orderedEntries.get(i);
                if(entry.getComparator().compare(other, instance) > 0)
                {
                    if(checkComparators(this.orderedEntries, i, other, instance))
                    {
                        combined = true;
                        for(int j = i; j < this.orderedEntries.size(); j++)
                        {
                            this.orderedEntries.get(j).getSetter().accept(instance, other);
                        }
                        break;
                    }
                }
            }

            for(EffectUpdater.Entry<T> entry : this.entries)
            {
                if(entry.getComparator().compare(other, instance) != 0)
                {
                    combined = true;
                    entry.getSetter().accept(instance, other);
                }
            }

            return combined;
        }

        private static <T extends CustomEffectInstance> boolean checkComparators(List<EffectUpdater.Entry<T>> comparators, int index, T comparing, T compared)
        {
            if(index > 0)
            {
                if(comparators.get(index).getComparator().compare(comparing, compared) == 0)
                {
                    return checkComparators(comparators, index - 1, comparing, compared);
                }
                return false;
            }
            return true;
        }

        protected static class Entry<T>
        {
            private final Comparator<T> comparator;
            private final BiConsumer<T, T> setter;

            private Entry(Comparator<T> comparator, BiConsumer<T, T> setter)
            {
                this.comparator = comparator;
                this.setter = setter;
            }

            public Comparator<T> getComparator()
            {
                return comparator;
            }

            public BiConsumer<T, T> getSetter()
            {
                return setter;
            }
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IEffectInstanceSerializer<?>> implements IEffectInstanceSerializer<CustomEffectInstance>
    {
        @Override
        public void encodePacket(CustomEffectInstance effect, FriendlyByteBuf buf)
        {
            MineriaEffectInstanceSerializers.DEFAULT.get().encodePacket(effect, buf);
            buf.writeInt(effect.getMaxDuration());
            buf.writeResourceLocation(effect.parentEffect == null ? new ResourceLocation("mineria", "null") : effect.parentEffect.getRegistryName());
        }

        @Override
        public CustomEffectInstance decodePacket(FriendlyByteBuf buf)
        {
            MobEffectInstance instance = MineriaEffectInstanceSerializers.DEFAULT.get().decodePacket(buf);
            int maxDuration = buf.readInt();
            ResourceLocation parentEffectId = buf.readResourceLocation();
            CustomEffectInstance custom = new CustomEffectInstance.Impl(MobEffects.MOVEMENT_SPEED, 0, maxDuration, 0, false, false, false, parentEffectId.getPath().equals("null") ? null : ForgeRegistries.MOB_EFFECTS.getValue(parentEffectId));

            return CustomEffectInstance.merge(custom, instance);
        }

        @Override
        public CustomEffectInstance deserialize(MobEffect effect, CompoundTag nbt)
        {
            MobEffectInstance instance = MineriaEffectInstanceSerializers.DEFAULT.get().deserialize(effect, nbt);
            CustomEffectInstance custom = new CustomEffectInstance.Impl(MobEffects.MOVEMENT_SPEED, 0, nbt.getInt("MaxDuration"), 0, false, false, false, nbt.contains("ParentEffect") ? ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(nbt.getString("ParentEffect"))) : null);

            return IEffectInstanceSerializer.readCurativeItems(CustomEffectInstance.merge(custom, instance), nbt);
        }
    }
}
