package io.github.mineria_mc.mineria.common.effects.instances;

import io.github.mineria_mc.mineria.common.effects.util.EffectUpdater;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.init.MineriaEffectInstanceSerializers;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomMobEffectInstance extends ModdedMobEffectInstance {
    public CustomMobEffectInstance(MobEffect potion, int duration, int maxDuration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon, boolean shouldRender, @Nullable MobEffect parentEffect) {
        super(potion, duration, maxDuration, amplifier, ambient, showParticles, showIcon, shouldRender, parentEffect);
    }

    public CustomMobEffectInstance(Builder builder) {
        super(builder);
    }

    @Override
    public boolean update(@Nonnull MobEffectInstance effectInstance) {
        if (effectInstance instanceof CustomMobEffectInstance other) {
            if (other.parentEffect != this.parentEffect) {
                return false;
            }
            return getEffectUpdater().updateEffect(this, other);
        }

        return false;
    }

    @Override
    public ResourceLocation getSerializerId() {
        return MineriaEffectInstanceSerializers.MODDED.getId();
    }

    @Override
    public ModdedMobEffectInstance copy() {
        return Util.make(new CustomMobEffectInstance(this.getEffect(), this.getDuration(), this.getMaxDuration(), this.getAmplifier(), this.isAmbient(), this.isVisible(), this.showIcon(), this.shouldRender(), this.parentEffect), effect -> effect.setCurativeItems(this.getCurativeItems()));
    }

    private static EffectUpdater<CustomMobEffectInstance> EFFECT_UPDATER;

    public static EffectUpdater<CustomMobEffectInstance> getEffectUpdater() {
        if (EFFECT_UPDATER == null) {
            EffectUpdater<CustomMobEffectInstance> updater = new EffectUpdater<>();
            updater.compareOrderedInts(0, ModdedMobEffectInstance::getAmplifier, ModdedMobEffectInstance::setAmplifier);
            updater.compareOrderedInts(1, ModdedMobEffectInstance::getMaxDuration, (inst, value) -> inst.maxDuration = value);
            updater.compareOrderedInts(2, ModdedMobEffectInstance::getDuration, ModdedMobEffectInstance::setDuration);
            updater.compareBooleans(ModdedMobEffectInstance::isAmbient, ModdedMobEffectInstance::setAmbient);
            updater.compareBooleans(ModdedMobEffectInstance::isVisible, ModdedMobEffectInstance::setVisible);
            updater.compareBooleans(ModdedMobEffectInstance::showIcon, ModdedMobEffectInstance::setShowIcon);
            EFFECT_UPDATER = updater;
        }

        return EFFECT_UPDATER.copy();
    }

    public static class Builder extends ModdedMobEffectInstance.Builder<CustomMobEffectInstance, Builder> {
        public Builder(MobEffect effect) {
            super(effect);
        }

        @Override
        public CustomMobEffectInstance build() {
            return new CustomMobEffectInstance(this);
        }

        public static Builder from(MobEffectInstance instance) {
            return new Builder(instance.getEffect())
                    .duration(instance.getDuration())
                    .amplifier(instance.getAmplifier())
                    .ambient(instance.isAmbient())
                    .showParticles(instance.isVisible())
                    .showIcon(instance.showIcon());
        }
    }

    public static class Serializer implements IMobEffectInstanceSerializer<ModdedMobEffectInstance> {
        @Override
        public void encodePacket(ModdedMobEffectInstance effect, FriendlyByteBuf buf) {
            MineriaEffectInstanceSerializers.DEFAULT.get().encodePacket(effect, buf);
            buf.writeInt(effect.getMaxDuration());
            buf.writeBoolean(effect.shouldRender());
            buf.writeNullable(ForgeRegistries.MOB_EFFECTS.getKey(effect.parentEffect), FriendlyByteBuf::writeResourceLocation);
        }

        @Override
        public ModdedMobEffectInstance decodePacket(FriendlyByteBuf buf) {
            MobEffectInstance instance = MineriaEffectInstanceSerializers.DEFAULT.get().decodePacket(buf);
            int maxDuration = buf.readInt();
            boolean shouldRender = buf.readBoolean();
            ResourceLocation parentEffectId = buf.readNullable(FriendlyByteBuf::readResourceLocation);

            Builder builder = Builder.from(instance).maxDuration(maxDuration).shouldRenderInGui(shouldRender);
            if (parentEffectId != null) {
                builder.parentEffect(ForgeRegistries.MOB_EFFECTS.getValue(parentEffectId));
            }
            return builder.build();
        }

        @Override
        public ModdedMobEffectInstance deserialize(MobEffect effect, CompoundTag nbt) {
            MobEffectInstance instance = MineriaEffectInstanceSerializers.DEFAULT.get().deserialize(effect, nbt);

            Builder builder = Builder.from(instance).maxDuration(nbt.getInt("MaxDuration")).shouldRenderInGui(nbt.getBoolean("ShouldRender"));
            if (nbt.contains("ParentEffect")) {
                builder.parentEffect(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(nbt.getString("ParentEffect"))));
            }
            return IMobEffectInstanceSerializer.readCurativeItems(builder.build(), nbt);
        }
    }
}
