package com.mineria.mod.network;

import com.mineria.mod.References;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class MineriaPacketHandler
{
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(References.MODID, "mineria"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerNetworkMessages()
    {
        addNetworkMessage(GuiButtonPressedMessageHandler.GuiButtonPressedMessage.class, new GuiButtonPressedMessageHandler(), NetworkDirection.PLAY_TO_SERVER);
        addNetworkMessage(PlayEntityEffectMessageHandler.PlayEntityEffectMessage.class, new PlayEntityEffectMessageHandler(), NetworkDirection.PLAY_TO_CLIENT);
    }

    private static int messageID = 0;

    public static <MSG> void addNetworkMessage(Class<MSG> msgClass, IMessageHandler<MSG> msg, NetworkDirection... directions)
    {
        for (NetworkDirection dir : directions)
        {
            MineriaPacketHandler.PACKET_HANDLER.registerMessage(messageID, msgClass, msg::encode, msg::decode, msg::onMessage, Optional.of(dir));
        }
        messageID++;
    }
}
