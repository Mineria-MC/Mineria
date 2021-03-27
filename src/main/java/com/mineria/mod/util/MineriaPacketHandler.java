package com.mineria.mod.util;

import com.mineria.mod.References;
import com.mineria.mod.network.GuiButtonPressedMessageHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class MineriaPacketHandler
{
    public static SimpleChannel PACKET_HANDLER;

    public static void registerNetworkMessagesMessages()
    {
        PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(References.MODID, "mineria"), () -> "1", e -> true, e -> true);
        addNetworkMessage(GuiButtonPressedMessageHandler.GuiButtonPressedMessage.class, new GuiButtonPressedMessageHandler(), NetworkDirection.PLAY_TO_SERVER);
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
