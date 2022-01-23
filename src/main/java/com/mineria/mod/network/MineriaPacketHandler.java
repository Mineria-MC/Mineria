package com.mineria.mod.network;

import com.mineria.mod.Mineria;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class MineriaPacketHandler
{
    private static final String PROTOCOL_VERSION = "1.1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(Mineria.MODID, "mineria"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerNetworkMessages()
    {
        addNetworkMessage(PlayEntityEffectMessageHandler.PlayEntityEffectMessage.class, new PlayEntityEffectMessageHandler(), NetworkDirection.PLAY_TO_CLIENT);
        addNetworkMessage(XpBlockMessageHandler.XpBlockMessage.class, new XpBlockMessageHandler(), NetworkDirection.PLAY_TO_SERVER);
    }

    // an easy way to assign a unique id to a message
    private static int messageID = 0;

    /**
     * Registers a message handler to the channel.
     *
     * @param msgClass the class of the message
     * @param msg the handler of the message
     * @param directions the directions in which the message will be registered
     * @param <MSG> the message type
     */
    public static <MSG> void addNetworkMessage(Class<MSG> msgClass, IMessageHandler<MSG> msg, NetworkDirection... directions)
    {
        for (NetworkDirection dir : directions)
        {
            MineriaPacketHandler.PACKET_HANDLER.registerMessage(messageID, msgClass, msg::encode, msg::decode, msg::onMessage, Optional.of(dir));
        }
        messageID++;
    }
}
