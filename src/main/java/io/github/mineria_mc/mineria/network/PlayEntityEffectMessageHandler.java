package io.github.mineria_mc.mineria.network;

import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.IMobEffectInstanceSerializer;
import io.github.mineria_mc.mineria.common.init.MineriaRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Replacement for {@link net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket}
 */
public class PlayEntityEffectMessageHandler implements MessageHandler<PlayEntityEffectMessageHandler.PlayEntityEffectMessage> {
    @Override
    public void onMessage(PlayEntityEffectMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new Handler(msg)));
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(PlayEntityEffectMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        ResourceLocation serializerId = getRegisteredId(msg.effect);
        buf.writeResourceLocation(serializerId);
        getSerializer(serializerId).encodePacket(msg.effect, buf);
    }

    @Override
    public PlayEntityEffectMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        MobEffectInstance effect = getSerializer(buf.readResourceLocation()).decodePacket(buf);

        return new PlayEntityEffectMessage(entityId, effect);
    }

    private static ResourceLocation getRegisteredId(MobEffectInstance effect) {
        if (effect instanceof ModdedMobEffectInstance custom) {
            ResourceLocation serializerId = custom.getSerializerId();
            if (MineriaRegistries.EFFECT_SERIALIZERS.get().containsKey(serializerId)) {
                return serializerId;
            }
        }
        return MineriaRegistries.EFFECT_SERIALIZERS.get().getDefaultKey();
    }

    @SuppressWarnings("unchecked")
    private static <T extends MobEffectInstance> IMobEffectInstanceSerializer<T> getSerializer(ResourceLocation serializerId) {
        return (IMobEffectInstanceSerializer<T>) MineriaRegistries.EFFECT_SERIALIZERS.get().getValue(serializerId);
    }

    public static class PlayEntityEffectMessage {
        private int entityId;
        private MobEffectInstance effect;

        @SuppressWarnings("unused")
        public PlayEntityEffectMessage() {
        }

        public PlayEntityEffectMessage(int entityId, MobEffectInstance effect) {
            this.entityId = entityId;
            this.effect = effect;
        }
    }

    private record Handler(PlayEntityEffectMessage msg) implements DistExecutor.SafeRunnable {
        @Override
        public void run() {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                if (entity instanceof LivingEntity living) {
                    living.forceAddEffect(msg.effect, null);
                }
            }
        }
    }
}
