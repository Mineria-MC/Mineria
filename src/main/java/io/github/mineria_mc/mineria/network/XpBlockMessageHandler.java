package io.github.mineria_mc.mineria.network;

import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class XpBlockMessageHandler implements MessageHandler<XpBlockMessageHandler.XpBlockMessage> {
    @SuppressWarnings("deprecation")
    @Override
    public void onMessage(XpBlockMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Level world = player.level;
                BlockPos tilePos = msg.pos;

                if (!world.hasChunkAt(tilePos)) {
                    return;
                }

                BlockEntity tile = world.getBlockEntity(tilePos);
                if (tile instanceof XpBlockEntity xpBlock) {
                    int activeState = msg.activeState;
                    int delay = msg.delay;

                    if (activeState > -1) {
                        xpBlock.setActive(activeState == 1);
                    }
                    else if (delay > -1) {
                        xpBlock.setOrbItemDelay(delay);
                    }
                    else {
                        xpBlock.setPlayer(player);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(XpBlockMessage msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos).writeInt(msg.activeState).writeInt(msg.delay);
    }

    @Override
    public XpBlockMessage decode(FriendlyByteBuf buf) {
        return new XpBlockMessage(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public static class XpBlockMessage {
        // The position of the TileEntity
        private final BlockPos pos;
        // The state of the Xp Block ; any negative value: unset, 0: false, 1: true
        private final int activeState;
        // The delay of the Xp Block ; any negative value: unset, any positive value or zero: set
        private final int delay;

        private XpBlockMessage(BlockPos pos, int activeState, int delay) {
            this.pos = pos;
            this.activeState = activeState;
            this.delay = delay;
        }

        /**
         * The basic message, used to set the player in {@link XpBlockEntity#setPlayer(Player)}
         */
        public XpBlockMessage(BlockPos pos) {
            this(pos, -1, -1);
        }

        /**
         * Used to set the state of the xp block in {@link XpBlockEntity#setActive(boolean)}
         */
        public static XpBlockMessage state(BlockPos pos, boolean active) {
            return new XpBlockMessage(pos, active ? 1 : 0, -1);
        }

        /**
         * Used to set the delay of the xp block in {@link XpBlockEntity#setOrbItemDelay(int)}
         */
        public static XpBlockMessage delay(BlockPos pos, int delay) {
            return new XpBlockMessage(pos, -1, delay);
        }
    }
}
