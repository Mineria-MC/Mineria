package io.github.mineria_mc.mineria.network;

import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.items.ApothecariumItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SavePlayerBookmarkMessageHandler implements MessageHandler<SavePlayerBookmarkMessageHandler.SavePlayerBookmarkMessage> {
    @Override
    public void onMessage(SavePlayerBookmarkMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player == null) {
                return;
            }
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);
                if(!heldItem.is(MineriaItems.APOTHECARIUM.get())) {
                    continue;
                }
                ApothecariumItem.savePlayerBookmarkData(heldItem, msg.bookmarkedPage, msg.pagesAmount);
            }
            ctx.get().setPacketHandled(true);
        });
    }

    @Override
    public void encode(SavePlayerBookmarkMessage msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.bookmarkedPage).writeVarInt(msg.pagesAmount);
    }

    @Override
    public SavePlayerBookmarkMessage decode(FriendlyByteBuf buf) {
        int bookmarkedPage = buf.readVarInt();
        int pagesAmount = buf.readVarInt();
        return new SavePlayerBookmarkMessage(bookmarkedPage, pagesAmount);
    }

    public record SavePlayerBookmarkMessage(int bookmarkedPage, int pagesAmount) {}
}
