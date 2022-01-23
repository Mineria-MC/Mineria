package com.mineria.mod.common.effects.instances;

import com.mineria.mod.common.effects.BowelSoundsEffect;
import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.IEffectInstanceSerializer;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import com.mineria.mod.util.MineriaUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;

public class BowelSoundEffectInstance extends CustomEffectInstance
{
    private int ticksUntilEffectsCured;
    private int delay;
    private float volume;
    private Item source;

    public BowelSoundEffectInstance(int duration, int amplifier, Item source)
    {
        this(duration, amplifier, 200,  source);
    }

    public BowelSoundEffectInstance(int duration, int amplifier, int ticksUntilEffectsCured, Item source)
    {
        this(duration, duration, amplifier, false, true, true, null, ticksUntilEffectsCured, 200, 2.0F, source);
    }

    protected BowelSoundEffectInstance(int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, @Nullable Effect parentEffect, int ticksUntilEffectsCured, int defaultDelay, float defaultVolume, Item source)
    {
        super(MineriaEffects.BOWEL_SOUNDS.get(), duration, maxDuration, amplifier, ambient, showParticles, showIcon, parentEffect);
        this.ticksUntilEffectsCured = ticksUntilEffectsCured;
        this.delay = defaultDelay;
        this.volume = defaultVolume;
        this.source = source;
    }

    @Override
    public boolean update(EffectInstance effectInstance)
    {
        if (effectInstance instanceof BowelSoundEffectInstance)
        {
            BowelSoundEffectInstance other = (BowelSoundEffectInstance) effectInstance;

            boolean combined = false;
            if(this.source != other.source)
            {
                this.source = other.source;
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            }
            else if (other.amplifier > this.amplifier)
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

            if(combined)
            {
                this.ticksUntilEffectsCured = other.ticksUntilEffectsCured;
                this.delay = other.delay;
                this.volume = other.volume;
            }

            return combined;
        }
        return false;
    }

    @Override
    public boolean tick(LivingEntity living, Runnable onChangedPotionEffect)
    {
        if(this.maxDuration - this.duration == this.ticksUntilEffectsCured)
        {
            if(living.curePotionEffects(new ItemStack(Items.MILK_BUCKET)) || living.curePotionEffects(new ItemStack(this.source)))
                living.getCommandSenderWorld().playSound(living instanceof PlayerEntity ? (PlayerEntity) living : null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.NEUTRAL, 1.5F, MineriaUtils.randomPitch());
        }

        return super.tick(living, onChangedPotionEffect);
    }

    @Override
    public void applyEffect(LivingEntity living)
    {
        if(this.duration > 0)
        {
            ((BowelSoundsEffect) this.potion).applyEffectTick(living, this.amplifier, this.volume);
        }
    }

    @Override
    public boolean isReady()
    {
        return this.duration % this.delay == 0;
    }

    public Item getSource()
    {
        return source;
    }

    public void decreaseVolume(float count, float limit)
    {
        this.volume = Math.max(this.volume - count, limit);
    }

    public void increaseDelay(int count, int limit)
    {
        this.delay = Math.min(this.delay + count, limit);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt)
    {
        super.save(nbt);
        nbt.putInt("TicksUntilEffectsCured", this.ticksUntilEffectsCured);
        nbt.putInt("Delay", this.delay);
        nbt.putFloat("Volume", this.volume);
        nbt.putString("SourceItem", source.getRegistryName().toString());
        return nbt;
    }

    @Override
    public IEffectInstanceSerializer<? extends CustomEffectInstance> getSerializer()
    {
        return MineriaEffectInstanceSerializers.BOWEL_SOUNDS.get();
    }

    @Override
    public BowelSoundEffectInstance copy()
    {
        return new BowelSoundEffectInstance(this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.showIcon(), this.parentEffect, this.ticksUntilEffectsCured, this.delay, this.volume, this.source);
    }

    public static class Serializer extends ForgeRegistryEntry<IEffectInstanceSerializer<?>> implements IEffectInstanceSerializer<BowelSoundEffectInstance>
    {
        @Override
        public void encodePacket(BowelSoundEffectInstance effect, PacketBuffer buf)
        {
            MineriaEffectInstanceSerializers.CUSTOM.get().encodePacket(effect, buf);
            buf.writeInt(effect.ticksUntilEffectsCured);
            buf.writeInt(effect.delay);
            buf.writeFloat(effect.volume);
            buf.writeResourceLocation(Objects.requireNonNull(effect.getSource().getRegistryName()));
        }

        @Override
        public BowelSoundEffectInstance decodePacket(PacketBuffer buf)
        {
            CustomEffectInstance custom = MineriaEffectInstanceSerializers.CUSTOM.get().decodePacket(buf);

            return new BowelSoundEffectInstance(custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), custom.isAmbient(), custom.isVisible(), custom.showIcon(), custom.getParentEffect(), buf.readInt(), buf.readInt(), buf.readFloat(), ForgeRegistries.ITEMS.getValue(buf.readResourceLocation()));
        }

        @Override
        public BowelSoundEffectInstance deserialize(Effect effect, CompoundNBT nbt)
        {
            CustomEffectInstance custom = MineriaEffectInstanceSerializers.CUSTOM.get().deserialize(effect, nbt);

            return IEffectInstanceSerializer.readCurativeItems(new BowelSoundEffectInstance(custom.getDuration(), custom.getMaxDuration(), custom.getAmplifier(), custom.isAmbient(), custom.isVisible(), custom.showIcon(), custom.getParentEffect(), nbt.getInt("TicksUntilEffectsCured"), nbt.getInt("Delay"), nbt.getFloat("Volume"), ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("SourceItem")))), nbt);
        }
    }
}
