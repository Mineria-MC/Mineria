package com.mineria.mod.network;

import com.mineria.mod.effects.CustomEffectInstance;
import com.mineria.mod.effects.IEffectInstanceSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayEntityEffectMessageHandler implements IMessageHandler<PlayEntityEffectMessageHandler.PlayEntityEffectMessage>
{
    @Override
    public void onMessage(PlayEntityEffectMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            if(world != null)
            {
                Entity entity = world.getEntityByID(msg.entityId);
                if(entity instanceof LivingEntity)
                    ((LivingEntity) entity).func_233646_e_(msg.effect);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(PlayEntityEffectMessage msg, PacketBuffer buf)
    {
        buf.writeInt(msg.entityId);
        buf.writeResourceLocation(getSerializerName(msg.effect));
        IEffectInstanceSerializer.getSerializer(getSerializerName(msg.effect)).encodePacket(msg.effect, buf);
        /*buf.writeByte(msg.effectId);
        buf.writeByte(msg.amplifier);
        buf.writeInt(msg.duration);
        buf.writeBoolean(msg.isAmbient);
        buf.writeBoolean(msg.doesShowParticles);
        buf.writeBoolean(msg.doesShowIcon);
        buf.writeBoolean(msg.shouldRender);
        buf.writeInt(msg.maxDuration);
        buf.writeBoolean(msg.bowelSounds);
        buf.writeInt(msg.potionClass);
        buf.writeInt(msg.poisonSource);*/
    }

    @Override
    public PlayEntityEffectMessage decode(PacketBuffer buf)
    {
        int entityId = buf.readInt();
        EffectInstance effect = IEffectInstanceSerializer.getSerializer(buf.readResourceLocation()).decodePacket(buf);

        return new PlayEntityEffectMessage(entityId, effect);
        /*byte amplifier = buf.readByte();
        int duration = buf.readInt();
        boolean isAmbient = buf.readBoolean();
        boolean doesShowParticles = buf.readBoolean();
        boolean doesShowIcon = buf.readBoolean();
        boolean shouldRender = buf.readBoolean();

        // Custom
        int maxDuration = buf.readInt();
        boolean bowelSounds = buf.readBoolean();

        // Poison
        int potionClass = buf.readInt();
        int poisonSource = buf.readInt();

        return new PlayEntityEffectMessage(entityId, makeEffectInstance(effectId, amplifier, duration, isAmbient, doesShowParticles, doesShowIcon, shouldRender, maxDuration, bowelSounds, potionClass, poisonSource));*/
    }

    private static ResourceLocation getSerializerName(EffectInstance effect)
    {
        return effect instanceof CustomEffectInstance ? ((CustomEffectInstance) effect).getSerializerName() : new ResourceLocation("default");
    }

    /*protected static EffectInstance makeEffectInstance(byte effectId, byte amplifier, int duration, boolean isAmbient, boolean doesShowParticles, boolean doesShowIcon, boolean shouldRender, int maxDuration, boolean bowelSounds, int potionClass, int poisonSource)
    {
        Effect effect = Effect.get(effectId & 0xFF);

        if(maxDuration > -1)
        {
            if(potionClass > -1 && poisonSource > -1)
            {
                PoisonEffectInstance poison = new PoisonEffectInstance(potionClass, duration, maxDuration, amplifier, PoisonSource.byId(poisonSource));
                poison.setPotionDurationMax(PlayEntityEffectMessage.isPotionDurationMax(duration));
                return poison;
            }
            else if(bowelSounds)
            {
                BowelSoundEffectInstance bowelSoundEffectInstance = new BowelSoundEffectInstance(duration, maxDuration, amplifier, isAmbient, doesShowParticles, doesShowIcon);
                bowelSoundEffectInstance.setPotionDurationMax(PlayEntityEffectMessage.isPotionDurationMax(duration));
                return bowelSoundEffectInstance;
            }
            else
            {
                CustomEffectInstance customInstance = CustomEffectInstance.makeCustomEffectInstance(effect, duration, maxDuration, amplifier, isAmbient, doesShowParticles, doesShowIcon, shouldRender);
                customInstance.setPotionDurationMax(PlayEntityEffectMessage.isPotionDurationMax(duration));
                return customInstance;
            }
        }

        EffectInstance instance = new EffectInstance(effect, duration, amplifier, isAmbient, doesShowParticles, doesShowIcon) {
            @Override
            public boolean shouldRender()
            {
                return shouldRender;
            }
        };
        instance.setPotionDurationMax(PlayEntityEffectMessage.isPotionDurationMax(duration));
        return instance;
    }*/

    public static class PlayEntityEffectMessage
    {
        private int entityId;
        private EffectInstance effect;
        /*private byte effectId;
        private byte amplifier;
        private int duration;
        private boolean isAmbient;
        private boolean doesShowParticles;
        private boolean doesShowIcon;
        private boolean shouldRender;

        // Custom
        private int maxDuration = -1;
        private boolean bowelSounds;

        // Poison
        private int potionClass = -1;
        private int poisonSource = -1;*/

        public PlayEntityEffectMessage(int entityId, EffectInstance effect)
        {
            this.entityId = entityId;
            this.effect = effect;
            /*
            this.effectId = (byte)(Effect.getId(effect.getPotion()) & 255);
            this.amplifier = (byte)(effect.getAmplifier() & 255);
            this.duration = Math.min(effect.getDuration(), 32767);
            this.isAmbient = effect.isAmbient();
            this.doesShowParticles = effect.doesShowParticles();
            this.doesShowIcon = effect.isShowIcon();
            this.shouldRender = effect.shouldRender();

            if(effect instanceof CustomEffectInstance)
                this.maxDuration = ((CustomEffectInstance) effect).getMaxDuration();

            if(effect instanceof BowelSoundEffectInstance)
                this.bowelSounds = true;

            if(effect instanceof PoisonEffectInstance)
            {
                this.potionClass = ((PoisonEffectInstance) effect).getPotionClass();
                this.poisonSource = ((PoisonEffectInstance) effect).getPoisonSource().getId();
            }*/
        }

        /*protected static boolean isPotionDurationMax(int duration)
        {
            return duration == 32767;
        }*/
    }
}
