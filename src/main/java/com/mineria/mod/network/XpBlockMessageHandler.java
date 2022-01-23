package com.mineria.mod.network;

import com.mineria.mod.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class XpBlockMessageHandler implements IMessageHandler<XpBlockMessageHandler.XpBlockMessage>
{
    @Override
    public void onMessage(XpBlockMessage msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player != null)
            {
                World world = player.level;
                BlockPos tilePos = msg.pos;

                if (!world.hasChunkAt(tilePos))
                    return;

                TileEntity tile = world.getBlockEntity(tilePos);
                if(tile instanceof XpBlockTileEntity)
                {
                    XpBlockTileEntity xpBlock = (XpBlockTileEntity) tile;
                    int activeState = msg.activeState;
                    int delay = msg.delay;

                    if(activeState > -1)
                        xpBlock.setActive(activeState == 1);
                    else if(delay > -1)
                        xpBlock.setOrbItemDelay(delay);
                    else
                        xpBlock.setPlayer(player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(XpBlockMessage msg, PacketBuffer buf)
    {
        buf.writeBlockPos(msg.pos).writeInt(msg.activeState).writeInt(msg.delay);
    }

    @Override
    public XpBlockMessage decode(PacketBuffer buf)
    {
        return new XpBlockMessage(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    public static class XpBlockMessage
    {
        // The position of the TileEntity
        private final BlockPos pos;
        // The state of the Xp Block ; any negative value: unset, 0: false, 1: true
        private final int activeState;
        // The delay of the Xp Block ; any negative value: unset, any positive value: set
        private final int delay;

        private XpBlockMessage(BlockPos pos, int activeState, int delay)
        {
            this.pos = pos;
            this.activeState = activeState;
            this.delay = delay;
        }

        /**
         * The basic message, used to set the player in {@link XpBlockTileEntity#setPlayer(PlayerEntity)}
         */
        public XpBlockMessage(BlockPos pos)
        {
            this(pos, -1, -1);
        }

        /**
         * Used to set the state of the xp block in {@link XpBlockTileEntity#setActive(boolean)}
         */
        public static XpBlockMessage state(BlockPos pos, boolean active)
        {
            return new XpBlockMessage(pos, active ? 1 : 0, -1);
        }

        /**
         * Used to set the delay of the xp block in {@link XpBlockTileEntity#setOrbItemDelay(int)}
         */
        public static XpBlockMessage delay(BlockPos pos, int delay)
        {
            return new XpBlockMessage(pos, -1, delay);
        }
    }
}
