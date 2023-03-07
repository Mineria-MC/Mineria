package io.github.mineria_mc.mineria.common.effects.instances;

import io.github.mineria_mc.mineria.common.effects.BowelSoundsEffect;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.init.MineriaEffectInstanceSerializers;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BowelSoundMobEffectInstance extends ModdedMobEffectInstance {
    private int ticksUntilEffectsCured;
    private int delay;
    private float volume;
    private Item source;

    public BowelSoundMobEffectInstance(int duration, int amplifier, Item source) {
        this(duration, amplifier, 200, source);
    }

    public BowelSoundMobEffectInstance(int duration, int amplifier, int ticksUntilEffectsCured, Item source) {
        this(duration, duration, amplifier, false, true, true, true, null, ticksUntilEffectsCured, 200, 2.0F, source);
    }

    protected BowelSoundMobEffectInstance(int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean shouldRender, @Nullable MobEffect parentEffect, int ticksUntilEffectsCured, int defaultDelay, float defaultVolume, Item source) {
        super(MineriaEffects.BOWEL_SOUNDS.get(), duration, maxDuration, amplifier, ambient, showParticles, showIcon, shouldRender, parentEffect);
        this.ticksUntilEffectsCured = ticksUntilEffectsCured;
        this.delay = defaultDelay;
        this.volume = defaultVolume;
        this.source = source;
    }

    public BowelSoundMobEffectInstance(Builder builder) {
        super(builder);
        this.ticksUntilEffectsCured = builder.ticksUntilEffectsCured;
        this.delay = builder.delay;
        this.volume = builder.volume;
        this.source = builder.source;
    }

    @Override
    public boolean update(@Nonnull MobEffectInstance effectInstance) {
        if (effectInstance instanceof BowelSoundMobEffectInstance other) {
            boolean combined = false;
            if (this.source != other.source) {
                this.source = other.source;
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            } else if (other.amplifier > this.amplifier) {
                this.amplifier = other.amplifier;
                this.maxDuration = other.maxDuration;
                this.duration = other.duration;
                combined = true;
            } else if (other.maxDuration > this.maxDuration) {
                if (other.amplifier == this.amplifier) {
                    this.maxDuration = other.maxDuration;
                    this.duration = other.duration;
                    combined = true;
                }
            } else if (other.duration > this.duration) {
                if (other.amplifier == this.amplifier && other.maxDuration == this.maxDuration) {
                    this.duration = other.duration;
                    combined = true;
                }
            }

            if (!other.ambient && this.ambient || combined) {
                this.ambient = other.ambient;
                combined = true;
            }

            if (other.showParticles != this.showParticles) {
                this.showParticles = other.showParticles;
                combined = true;
            }

            if (other.showIcon != this.showIcon) {
                this.showIcon = other.showIcon;
                combined = true;
            }

            if (combined) {
                this.ticksUntilEffectsCured = other.ticksUntilEffectsCured;
                this.delay = other.delay;
                this.volume = other.volume;
            }

            return combined;
        }
        return false;
    }

    @Override
    public boolean tick(@Nonnull LivingEntity living, @Nonnull Runnable onChangedPotionEffect) {
        if (this.maxDuration - this.duration == this.ticksUntilEffectsCured) {
            if (living.curePotionEffects(new ItemStack(Items.MILK_BUCKET)) || living.curePotionEffects(new ItemStack(this.source))) {
                living.getCommandSenderWorld().playSound(living instanceof Player ? (Player) living : null, living.getX(), living.getY(), living.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.NEUTRAL, 1.5F, MineriaUtils.randomPitch());
            }
        }

        return super.tick(living, onChangedPotionEffect);
    }

    @Override
    public void applyEffect(@Nonnull LivingEntity living) {
        if (this.duration > 0) {
            ((BowelSoundsEffect) this.potion).applyEffectTick(living, this.amplifier, this.volume);
        }
    }

    @Override
    public boolean isReady() {
        return this.duration % this.delay == 0;
    }

    public Item getSource() {
        return source;
    }

    public void decreaseVolume(float count, float limit) {
        this.volume = Math.max(this.volume - count, limit);
    }

    public void increaseDelay(int count, int limit) {
        this.delay = Math.min(this.delay + count, limit);
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("TicksUntilEffectsCured", this.ticksUntilEffectsCured);
        nbt.putInt("Delay", this.delay);
        nbt.putFloat("Volume", this.volume);
        ResourceLocation sourceId = ForgeRegistries.ITEMS.getKey(source);
        if(sourceId == null) {
            sourceId = new ResourceLocation("air");
        }
        nbt.putString("SourceItem", sourceId.toString());
        return nbt;
    }

    @Override
    public ResourceLocation getSerializerId() {
        return MineriaEffectInstanceSerializers.BOWEL_SOUNDS.getId();
    }

    @Override
    public BowelSoundMobEffectInstance copy() {
        return new BowelSoundMobEffectInstance(this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.showIcon(), this.shouldRender(), this.parentEffect, this.ticksUntilEffectsCured, this.delay, this.volume, this.source);
    }

    public static class Serializer implements IMobEffectInstanceSerializer<BowelSoundMobEffectInstance> {
        @Override
        public void encodePacket(BowelSoundMobEffectInstance effect, FriendlyByteBuf buf) {
            MineriaEffectInstanceSerializers.MODDED.get().encodePacket(effect, buf);
            buf.writeInt(effect.ticksUntilEffectsCured);
            buf.writeInt(effect.delay);
            buf.writeFloat(effect.volume);
            ResourceLocation sourceId = ForgeRegistries.ITEMS.getKey(effect.getSource());
            if(sourceId == null) {
                sourceId = new ResourceLocation("air");
            }
            buf.writeResourceLocation(sourceId);
        }

        @Override
        public BowelSoundMobEffectInstance decodePacket(FriendlyByteBuf buf) {
            ModdedMobEffectInstance custom = MineriaEffectInstanceSerializers.MODDED.get().decodePacket(buf);
            int ticksUntilEffectsCured = buf.readInt();
            int delay = buf.readInt();
            float volume = buf.readFloat();
            Item source = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            if(source == null) {
                source = Items.AIR;
            }
            return Builder.from(custom, source).setTicksUntilEffectsCured(ticksUntilEffectsCured).setDelay(delay).setVolume(volume).build();
        }

        @Override
        public BowelSoundMobEffectInstance deserialize(MobEffect effect, CompoundTag nbt) {
            ModdedMobEffectInstance custom = MineriaEffectInstanceSerializers.MODDED.get().deserialize(effect, nbt);
            Item source = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("SourceItem")));
            if(source == null) {
                source = Items.AIR;
            }
            Builder builder = Builder.from(custom, source).setTicksUntilEffectsCured(nbt.getInt("TicksUntilEffectsCured")).setDelay(nbt.getInt("Delay")).setVolume(nbt.getFloat("Volume"));
            return IMobEffectInstanceSerializer.readCurativeItems(builder.build(), nbt);
        }
    }

    public static class Builder extends ModdedMobEffectInstance.Builder<BowelSoundMobEffectInstance, Builder> {
        private int ticksUntilEffectsCured = 200;
        private int delay = 200;
        private float volume = 2.0F;
        private Item source;

        public Builder(MobEffect effect, Item source) {
            super(effect);
            this.source = source;
        }

        public Builder setTicksUntilEffectsCured(int ticksUntilEffectsCured) {
            this.ticksUntilEffectsCured = ticksUntilEffectsCured;
            return this;
        }

        public Builder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Builder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder setSource(Item source) {
            this.source = source;
            return this;
        }

        @Override
        public BowelSoundMobEffectInstance build() {
            return new BowelSoundMobEffectInstance(this);
        }

        public static Builder from(ModdedMobEffectInstance instance, Item source) {
            return new Builder(instance.getEffect(), source)
                    .duration(instance.getDuration())
                    .maxDuration(instance.getMaxDuration())
                    .amplifier(instance.getAmplifier())
                    .ambient(instance.isAmbient())
                    .showParticles(instance.isVisible())
                    .showIcon(instance.showIcon())
                    .shouldRenderInGui(instance.shouldRender())
                    .parentEffect(instance.getParentEffect());
        }
    }
}
