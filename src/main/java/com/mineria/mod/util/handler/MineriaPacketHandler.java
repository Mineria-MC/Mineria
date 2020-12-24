package com.mineria.mod.util.handler;

import com.mineria.mod.network.GuiButtonPressedMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MineriaPacketHandler
{
    public static final SimpleNetworkWrapper PACKET_HANDLER = NetworkRegistry.INSTANCE.newSimpleChannel("mineria");

    public static void registerNetworkMessagesMessages()
    {
        addNetworkMessage(GuiButtonPressedMessageHandler.class, GuiButtonPressedMessageHandler.GUIButtonPressedMessage.class, Side.SERVER);
    }

    private static int messageID = 0;

    public static <T extends IMessage, V extends IMessage> void addNetworkMessage(Class<? extends IMessageHandler<T, V>> handler, Class<T> messageClass, Side... sides)
    {
        for (Side side : sides)
        {
            MineriaPacketHandler.PACKET_HANDLER.registerMessage(handler, messageClass, messageID, side);
        }
        messageID++;
    }
}
