package com.mineria.mod.network;

import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.IEffectInstanceSerializer;
import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Replacement for {@link net.minecraft.network.play.server.SPlayEntityEffectPacket}
 */
public class PlayEntityEffectMessageHandler implements IMessageHandler<PlayEntityEffectMessageHandler.PlayEntityEffectMessage>
{
    @Override
    public void onMessage(PlayEntityEffectMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().level;
            if(world != null)
            {
                Entity entity = world.getEntity(msg.entityId);
                if(entity instanceof LivingEntity)
                    ((LivingEntity) entity).forceAddEffect(msg.effect);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(PlayEntityEffectMessage msg, PacketBuffer buf)
    {
        buf.writeInt(msg.entityId);
        buf.writeResourceLocation(getSerializer(msg.effect).getRegistryName());
        getSerializer(msg.effect).encodePacket(msg.effect, buf);
    }

    @Override
    public PlayEntityEffectMessage decode(PacketBuffer buf)
    {
        int entityId = buf.readInt();
        EffectInstance effect = MineriaEffectInstanceSerializers.byName(buf.readResourceLocation()).decodePacket(buf);

        return new PlayEntityEffectMessage(entityId, effect);
    }

    @SuppressWarnings("unchecked")
    private static <T extends EffectInstance> IEffectInstanceSerializer<T> getSerializer(T effect)
    {
        return effect instanceof CustomEffectInstance ? (IEffectInstanceSerializer<T>) ((CustomEffectInstance) effect).getSerializer() : (IEffectInstanceSerializer<T>) MineriaEffectInstanceSerializers.DEFAULT.get();
    }

    public static class PlayEntityEffectMessage
    {
        private int entityId;
        private EffectInstance effect;

        @SuppressWarnings("unused")
        public PlayEntityEffectMessage() {}

        public PlayEntityEffectMessage(int entityId, EffectInstance effect)
        {
            this.entityId = entityId;
            this.effect = effect;
        }
    }
}
