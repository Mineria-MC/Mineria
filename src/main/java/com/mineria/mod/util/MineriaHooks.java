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
    public static final Map<SPlayEntityEffectPacket, EffectInstance> EFFECTS_FROM_PACKET = new HashMap<>();

    public static boolean replacePacket(IPacket<?> packet, NetworkManager manager)
    {
        if(packet instanceof SPlayEntityEffectPacket)
        {
            MineriaPacketHandler.PACKET_HANDLER.sendTo(new PlayEntityEffectMessageHandler.PlayEntityEffectMessage(((SPlayEntityEffectPacket) packet).getEntityId(), EFFECTS_FROM_PACKET.get(packet)), manager, NetworkDirection.PLAY_TO_CLIENT);
            return true;
        }

        return false;
    }
}
