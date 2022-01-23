package com.mineria.mod.util;

import com.mineria.mod.network.MineriaPacketHandler;
import com.mineria.mod.network.PlayEntityEffectMessageHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;

public class MineriaHooks
{
    // This map stores the effect instance associated to a packet (they aren't stored in the packet instance)
    public static final Map<SPlayEntityEffectPacket, EffectInstance> EFFECTS_FROM_PACKET = new HashMap<>();

    // A way for other modders to disable packet replacing.
    public static boolean replacePlayEffectPacket = true;

    /**
     * This method replaces a minecraft packet with a message from Mineria and sends it on Mineria's channel.
     *
     * @param packet the packet to replace
     * @param manager the network manager
     * @return true if the packet was successfully replaced
     */
    public static boolean replacePacket(IPacket<?> packet, NetworkManager manager)
    {
        if(packet instanceof SPlayEntityEffectPacket && replacePlayEffectPacket)
        {
            MineriaPacketHandler.PACKET_HANDLER.sendTo(new PlayEntityEffectMessageHandler.PlayEntityEffectMessage(((SPlayEntityEffectPacket) packet).getEntityId(), EFFECTS_FROM_PACKET.remove(packet)), manager, NetworkDirection.PLAY_TO_CLIENT);
            return true;
        }

        return false;
    }
}
