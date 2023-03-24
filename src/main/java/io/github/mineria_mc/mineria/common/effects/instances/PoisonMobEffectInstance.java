package io.github.mineria_mc.mineria.common.effects.instances;

import com.google.common.collect.ImmutableList;
import io.github.mineria_mc.mineria.common.capabilities.MineriaCapabilities;
import io.github.mineria_mc.mineria.common.effects.PoisonEffect;
import io.github.mineria_mc.mineria.common.effects.util.EffectUpdater;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.effects.util.IPoisonEffect;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaEffectInstanceSerializers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PoisonMobEffectInstance extends ModdedMobEffectInstance {
    private int potionClass;
    private final IPoisonEffect potion;
    private final PoisonSource poisonSource;

    public PoisonMobEffectInstance(int potionClass, int duration, int amplifier, PoisonSource source) {
        this(potionClass, duration, duration, amplifier, source);
    }

    public PoisonMobEffectInstance(int potionClass, int duration, int maxDuration, int amplifier, PoisonSource source) {
        super(MobEffects.POISON, duration, maxDuration, amplifier, false, true, true, true, null);
        this.potionClass = potionClass;
        this.potion = (IPoisonEffect) MobEffects.POISON;
        this.poisonSource = source;
    }

    public PoisonMobEffectInstance(Builder builder) {
        this(builder.potionClass, builder.duration, builder.maxDuration, builder.amplifier, builder.source);
    }

    @Override
    public boolean update(@Nonnull MobEffectInstance effectInstance) {
        if (effectInstance instanceof PoisonMobEffectInstance other) {
            return getEffectUpdater().updateEffect(this, other);
        }
        return false;
    }

    private static EffectUpdater<PoisonMobEffectInstance> EFFECT_UPDATER;

    protected static EffectUpdater<PoisonMobEffectInstance> getEffectUpdater() {
        if (EFFECT_UPDATER == null) {
            EffectUpdater<PoisonMobEffectInstance> updater = new EffectUpdater<>();
            updater.compareOrderedInts(0, PoisonMobEffectInstance::getPotionClass, (inst, value) -> inst.potionClass = value);
            updater.compareOrderedInts(1, PoisonMobEffectInstance::getAmplifier, PoisonMobEffectInstance::setAmplifier);
            updater.compareOrderedInts(2, PoisonMobEffectInstance::getMaxDuration, (inst, value) -> inst.maxDuration = value);
            updater.compareOrderedInts(3, PoisonMobEffectInstance::getDuration, PoisonMobEffectInstance::setDuration);
            updater.compareBooleans(ModdedMobEffectInstance::isAmbient, ModdedMobEffectInstance::setAmbient);
            updater.compareBooleans(ModdedMobEffectInstance::isVisible, ModdedMobEffectInstance::setVisible);
            updater.compareBooleans(ModdedMobEffectInstance::showIcon, ModdedMobEffectInstance::setShowIcon);
            EFFECT_UPDATER = updater;
        }

        return EFFECT_UPDATER.copy();
    }

    public int getPotionClass() {
        return potionClass;
    }

    @Override
    public ResourceLocation getSerializerId() {
        return MineriaEffectInstanceSerializers.POISON.getId();
    }

    @Override
    public boolean isReady() {
        return this.potion.isDurationEffectTick(this.duration, this.amplifier, this.potionClass);
    }

    @Override
    public void applyEffect(@Nonnull LivingEntity living) {
        if (this.duration > 0) {
            this.potion.applyEffectTick(living, this.amplifier, this.duration, this.maxDuration, this.potionClass);
        }
    }

    public boolean doSpasms() {
        return this.potion.doSpasms(this.duration, this.maxDuration, this.potionClass);
    }

    public boolean doConvulsions() {
        return this.potion.doConvulsions(this.duration, this.maxDuration, this.potionClass);
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return this.potion.getCurativeItems(this.potionClass, this.amplifier, this.maxDuration, this.duration, this.poisonSource);
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("PotionClass", this.potionClass);
        nbt.putString("PoisonSource", this.poisonSource.getId().toString());
        return nbt;
    }

    public PoisonSource getPoisonSource() {
        return poisonSource;
    }

    @Override
    public void drawPotionName(Font font, PoseStack matrixStack, float x, float y) {
        Component txt = this.poisonSource.getDescription(this.potionClass, this.amplifier).append(" - ").append(MobEffectUtil.formatDuration(this, 1.0F));
        int width = font.width(txt);
        font.drawShadow(matrixStack, txt, x - (int) (width / 2), y, 16727643);
    }

    @Override
    public int getColor() {
        return this.poisonSource.getColor();
    }

    @Override
    public void onEffectRemoved(LivingEntity living) {
        this.potion.removeMovementSpeedModifier(living);
        living.getCapability(MineriaCapabilities.POISON_EXPOSURE).ifPresent(cap -> cap.removePoison(this.poisonSource));
    }

    @Override
    public PoisonMobEffectInstance copy() {
        return Util.make(new PoisonMobEffectInstance(this.getPotionClass(), this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.getPoisonSource()), effect -> effect.setCurativeItems(this.getCurativeItems()));
    }

    public static void applyPoisonEffect(LivingEntity living, int potionClass, int duration, int amplifier, PoisonSource source) {
        for (MobEffectInstance effect : getPoisonEffects(potionClass, duration, amplifier, source)) {
            living.addEffect(effect);
        }
    }

    public static MobEffectInstance[] getPoisonEffects(int potionClass, int duration, int amplifier, PoisonSource source, MobEffectInstance... additionalEffects) {
        ImmutableList.Builder<MobEffectInstance> effects = ImmutableList.builder();
        effects.add(new PoisonMobEffectInstance(potionClass, duration, amplifier, source));
        effects.add(new PoisoningHiddenEffectInstance(MobEffects.CONFUSION, duration, Math.min(potionClass, 2), source).withPoison());
        if(potionClass > 0) {
            effects.add(new PoisoningHiddenEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, Math.min(potionClass - 1, 1), source).withPoison());
        }
        effects.add(additionalEffects);
        return effects.build().toArray(MobEffectInstance[]::new);
    }

    public static boolean isEntityAffected(LivingEntity living) {
        return living.hasEffect(MobEffects.POISON) && living.getEffect(MobEffects.POISON) instanceof PoisonMobEffectInstance;
    }

    public static class Serializer implements IMobEffectInstanceSerializer<PoisonMobEffectInstance> {
        @Override
        public void encodePacket(PoisonMobEffectInstance effect, FriendlyByteBuf buf) {
            MineriaEffectInstanceSerializers.MODDED.get().encodePacket(effect, buf);
            buf.writeInt(effect.getPotionClass());
            buf.writeResourceLocation(effect.getPoisonSource().getId());
        }

        @Override
        public PoisonMobEffectInstance decodePacket(FriendlyByteBuf buf) {
            ModdedMobEffectInstance custom = MineriaEffectInstanceSerializers.MODDED.get().decodePacket(buf);
            int potionClass = buf.readInt();
            ResourceLocation sourceId = buf.readResourceLocation();
            return Builder.from(custom, potionClass, PoisonSource.byName(sourceId)).build();
        }

        @Override
        public PoisonMobEffectInstance deserialize(MobEffect effect, CompoundTag nbt) {
            ModdedMobEffectInstance custom = MineriaEffectInstanceSerializers.MODDED.get().deserialize(effect, nbt);
            return IMobEffectInstanceSerializer.readCurativeItems(Builder.from(custom, nbt.getInt("PotionClass"), PoisonSource.byName(new ResourceLocation(nbt.getString("PoisonSource")))).build(), nbt);
        }
    }

    public static class Builder extends ModdedMobEffectInstance.Builder<PoisonMobEffectInstance, Builder> {
        private final int potionClass;
        private final PoisonSource source;

        public Builder(int potionClass, PoisonSource source) {
            super(MobEffects.POISON);
            this.potionClass = potionClass;
            this.source = source;
        }

        @Override
        public PoisonMobEffectInstance build() {
            return new PoisonMobEffectInstance(this);
        }

        public static Builder from(ModdedMobEffectInstance instance, int potionClass, PoisonSource source) {
            return new Builder(potionClass, source).duration(instance.getDuration()).maxDuration(instance.getMaxDuration()).amplifier(instance.getAmplifier());
        }
    }
}
