package com.mineria.mod.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This interface has the 3 main methods for handling a message class
 * @param <MSG> the message class
 */
public interface IMessageHandler<MSG>
{
    void onMessage(MSG msg, Supplier<NetworkEvent.Context> ctx);

    void encode(MSG msg, PacketBuffer buf);

    MSG decode(PacketBuffer buf);
}
