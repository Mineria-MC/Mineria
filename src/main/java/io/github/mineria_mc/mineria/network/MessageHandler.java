package io.github.mineria_mc.mineria.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This interface has the 3 main methods for handling a message class
 *
 * @param <MSG> the message class
 */
public interface MessageHandler<MSG> {
    /**
     * Called when the message is received.
     *
     * @param msg the message instance
     * @param ctx the context for the NetworkEvent
     */
    void onMessage(MSG msg, Supplier<NetworkEvent.Context> ctx);

    /**
     * Used to store the message in the given buffer.
     *
     * @param msg the message instance
     * @param buf the buffer in which values can be written
     */
    void encode(MSG msg, FriendlyByteBuf buf);

    /**
     * Used to parse a message from the given buffer.
     *
     * @param buf the buffer in which values can be read
     * @return a message instance from these values
     */
    MSG decode(FriendlyByteBuf buf);
}
