package com.mineria.mod.util.handler;

public class MineriaPacketHandler
{
    /*
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
    }*/
}
