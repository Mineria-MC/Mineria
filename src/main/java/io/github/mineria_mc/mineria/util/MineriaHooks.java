package io.github.mineria_mc.mineria.util;

import io.github.mineria_mc.mineria.network.MineriaPacketHandler;
import io.github.mineria_mc.mineria.network.PlayEntityEffectMessageHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;

public class MineriaHooks {
    // This map stores the effect instance associated to a packet (they aren't stored in the packet instance)
    public static final Map<ClientboundUpdateMobEffectPacket, MobEffectInstance> EFFECTS_FROM_PACKET = new HashMap<>();

    /**
     * This method replaces a minecraft packet with a message from Mineria and sends it on Mineria's channel.
     *
     * @param packet  the packet to replace
     * @param manager the network manager
     * @return true if the packet was successfully replaced
     */
    public static boolean replacePacket(Packet<?> packet, Connection manager) {
        if (packet instanceof ClientboundUpdateMobEffectPacket) {
            MineriaPacketHandler.PACKET_HANDLER.sendTo(new PlayEntityEffectMessageHandler.PlayEntityEffectMessage(((ClientboundUpdateMobEffectPacket) packet).getEntityId(), EFFECTS_FROM_PACKET.remove(packet)), manager, NetworkDirection.PLAY_TO_CLIENT);
            return true;
        }

        return false;
    }
}
