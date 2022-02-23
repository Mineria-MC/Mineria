package com.mineria.mod.network;

import com.mineria.mod.common.effects.CustomEffectInstance;
import com.mineria.mod.common.effects.IEffectInstanceSerializer;
import com.mineria.mod.common.init.MineriaEffectInstanceSerializers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Replacement for {@link net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket}
 */
public class PlayEntityEffectMessageHandler implements IMessageHandler<PlayEntityEffectMessageHandler.PlayEntityEffectMessage>
{
    @Override
    public void onMessage(PlayEntityEffectMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Handler.handle(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(PlayEntityEffectMessage msg, FriendlyByteBuf buf)
    {
        buf.writeInt(msg.entityId);
        buf.writeResourceLocation(getSerializer(msg.effect).getRegistryName());
        getSerializer(msg.effect).encodePacket(msg.effect, buf);
    }

    @Override
    public PlayEntityEffectMessage decode(FriendlyByteBuf buf)
    {
        int entityId = buf.readInt();
        MobEffectInstance effect = MineriaEffectInstanceSerializers.byName(buf.readResourceLocation()).decodePacket(buf);

        return new PlayEntityEffectMessage(entityId, effect);
    }

    @SuppressWarnings("unchecked")
    private static <T extends MobEffectInstance> IEffectInstanceSerializer<T> getSerializer(T effect)
    {
        return effect instanceof CustomEffectInstance custom ? (IEffectInstanceSerializer<T>) custom.getSerializer() : (IEffectInstanceSerializer<T>) MineriaEffectInstanceSerializers.DEFAULT.get();
    }

    public static class PlayEntityEffectMessage
    {
        private int entityId;
        private MobEffectInstance effect;

        @SuppressWarnings("unused")
        public PlayEntityEffectMessage() {}

        public PlayEntityEffectMessage(int entityId, MobEffectInstance effect)
        {
            this.entityId = entityId;
            this.effect = effect;
        }
    }

    private static class Handler
    {
        public static void handle(PlayEntityEffectMessage msg, Supplier<NetworkEvent.Context> ctx)
        {
            ClientLevel world = Minecraft.getInstance().level;
            if(world != null)
            {
                Entity entity = world.getEntity(msg.entityId);
                if(entity instanceof LivingEntity living)
                    living.forceAddEffect(msg.effect, null);
            }
        }
    }
}
