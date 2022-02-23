package com.mineria.mod.common.effects;

import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class EffectInstanceSerializer extends ForgeRegistryEntry<IEffectInstanceSerializer<?>> implements IEffectInstanceSerializer<MobEffectInstance>
{
    @Override
    public void encodePacket(MobEffectInstance effect, FriendlyByteBuf buf)
    {
        byte effectId = (byte)(MobEffect.getId(effect.getEffect()) & 255);
        byte amplifier = (byte)(effect.getAmplifier() & 255);
        buf.writeByte(effectId);
        buf.writeByte(amplifier);
        buf.writeInt(effect.getDuration());
        buf.writeBoolean(effect.isAmbient());
        buf.writeBoolean(effect.isVisible());
        buf.writeBoolean(effect.showIcon());
//        buf.writeBoolean(effect.shouldRender());
    }

    @Override
    public MobEffectInstance decodePacket(FriendlyByteBuf buf)
    {
        byte effectId = buf.readByte();
        byte amplifier = buf.readByte();
        int duration = buf.readInt();
        boolean isAmbient = buf.readBoolean();
        boolean doesShowParticles = buf.readBoolean();
        boolean doesShowIcon = buf.readBoolean();
//        boolean shouldRender = buf.readBoolean();

        MobEffect effect = MobEffect.byId(effectId & 0xFF);

        MobEffectInstance instance = new MobEffectInstance(effect, duration, amplifier, isAmbient, doesShowParticles, doesShowIcon)/* {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        }*/;
        instance.setNoCounter(duration > 32767);

        return instance;
    }

    @Override
    public MobEffectInstance deserialize(MobEffect effect, CompoundTag nbt)
    {
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

        return IEffectInstanceSerializer.readCurativeItems(new MobEffectInstance(effect, duration, Math.max(amplifier, 0), isAmbient, showParticles, showIcon, hiddenEffect), nbt);
    }
}
