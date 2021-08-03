package com.mineria.mod.effects;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;

public class EffectInstanceSerializer implements IEffectInstanceSerializer<EffectInstance>
{
    @Override
    public ResourceLocation getName()
    {
        return new ResourceLocation("minecraft", "default");
    }

    @Override
    public void encodePacket(EffectInstance effect, PacketBuffer buf)
    {
        byte effectId = (byte)(Effect.getId(effect.getPotion()) & 255);
        byte amplifier = (byte)(effect.getAmplifier() & 255);
        buf.writeByte(effectId);
        buf.writeByte(amplifier);
        buf.writeInt(effect.getDuration());
        buf.writeBoolean(effect.isAmbient());
        buf.writeBoolean(effect.doesShowParticles());
        buf.writeBoolean(effect.isShowIcon());
        buf.writeBoolean(effect.shouldRender());
    }

    @Override
    public EffectInstance decodePacket(PacketBuffer buf)
    {
        byte effectId = buf.readByte();
        byte amplifier = buf.readByte();
        int duration = buf.readInt();
        boolean isAmbient = buf.readBoolean();
        boolean doesShowParticles = buf.readBoolean();
        boolean doesShowIcon = buf.readBoolean();
        boolean shouldRender = buf.readBoolean();

        Effect effect = Effect.get(effectId & 0xFF);

        EffectInstance instance = new EffectInstance(effect, duration, amplifier, isAmbient, doesShowParticles, doesShowIcon) {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        };
        instance.setPotionDurationMax(duration > 32767);

        return instance;
    }

    @Override
    public EffectInstance deserialize(Effect effect, CompoundNBT nbt)
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

        EffectInstance hiddenEffect = null;
        if (nbt.contains("HiddenEffect", 10)) {
            hiddenEffect = IEffectInstanceSerializer.getSerializer(this.getName()).deserialize(effect, nbt.getCompound("HiddenEffect"));
        }

        return IEffectInstanceSerializer.readCurativeItems(new EffectInstance(effect, duration, Math.max(amplifier, 0), isAmbient, showParticles, showIcon, hiddenEffect), nbt);
    }
}
