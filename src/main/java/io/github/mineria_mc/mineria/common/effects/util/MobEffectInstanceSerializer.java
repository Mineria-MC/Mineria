package io.github.mineria_mc.mineria.common.effects.util;

import io.github.mineria_mc.mineria.common.init.MineriaEffectInstanceSerializers;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class MobEffectInstanceSerializer implements IMobEffectInstanceSerializer<MobEffectInstance> {
    @Override
    public void encodePacket(MobEffectInstance effect, FriendlyByteBuf buf) {
        ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(effect.getEffect());
        if(effectId == null) {
            LOGGER.error("Tried to encode unregistered mob effect!");
            return;
        }
        buf.writeResourceLocation(effectId);
        buf.writeByte(effect.getAmplifier() & 255);
        buf.writeInt(effect.getDuration());
        buf.writeBoolean(effect.isAmbient());
        buf.writeBoolean(effect.isVisible());
        buf.writeBoolean(effect.showIcon());
        buf.writeOptional(effect.getFactorData(), (otherBuffer, factorData) -> otherBuffer.writeWithCodec(MobEffectInstance.FactorData.CODEC, factorData));
    }

    @Override
    public MobEffectInstance decodePacket(FriendlyByteBuf buf) {
        ResourceLocation effectId = buf.readResourceLocation();
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);
        if(effect == null) {
            LOGGER.error("Tried to decode unregistered mob effect!");
            return new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 0);
        }
        byte amplifier = buf.readByte();
        int duration = buf.readInt();
        boolean isAmbient = buf.readBoolean();
        boolean doesShowParticles = buf.readBoolean();
        boolean doesShowIcon = buf.readBoolean();
        Optional<MobEffectInstance.FactorData> factorData = buf.readOptional(otherBuffer -> otherBuffer.readWithCodec(MobEffectInstance.FactorData.CODEC));

        return new MobEffectInstance(effect, duration, amplifier, isAmbient, doesShowParticles, doesShowIcon, null, factorData);
    }

    @Override
    public MobEffectInstance deserialize(MobEffect effect, CompoundTag nbt) {
        int duration = nbt.getInt("Duration");
        int amplifier = nbt.getByte("Amplifier");
        boolean isAmbient = nbt.getBoolean("Ambient");

        boolean showParticles = true;
        if (nbt.contains("ShowParticles", 1)) {
            showParticles = nbt.getBoolean("ShowParticles");
        }

        boolean showIcon = showParticles;
        if (nbt.contains("ShowIcon", 1)) {
            showIcon = nbt.getBoolean("ShowIcon");
        }

        MobEffectInstance hiddenEffect = null;
        if (nbt.contains("HiddenEffect", 10)) {
            hiddenEffect = MineriaEffectInstanceSerializers.DEFAULT.get().deserialize(effect, nbt.getCompound("HiddenEffect"));
        }

        Optional<MobEffectInstance.FactorData> factorData;
        if (nbt.contains("FactorCalculationData", 10)) {
            factorData = MobEffectInstance.FactorData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("FactorCalculationData"))).resultOrPartial(LOGGER::error);
        } else {
            factorData = Optional.empty();
        }

        return IMobEffectInstanceSerializer.readCurativeItems(new MobEffectInstance(effect, duration, Math.max(amplifier, 0), isAmbient, showParticles, showIcon, hiddenEffect, factorData), nbt);
    }
}
