package com.mineria.mod.network;

import com.mineria.mod.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GuiButtonPressedMessageHandler implements IMessageHandler<GuiButtonPressedMessageHandler.GuiButtonPressedMessage>
{
    @Override
    public void onMessage(GuiButtonPressedMessage message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            World world = player.world;
            if (!world.isBlockLoaded(message.pos))
                return;
            if (message.buttonID == 0)
            {
                XpBlockTileEntity.executeProcedure(message.pos, world, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public void encode(GuiButtonPressedMessage message, PacketBuffer buf)
    {
        buf.writeBlockPos(message.pos);
        buf.writeInt(message.buttonID);
    }

    @Override
    public GuiButtonPressedMessage decode(PacketBuffer buf)
    {
        return new GuiButtonPressedMessage(buf.readBlockPos(), buf.readInt());
    }

    public static class GuiButtonPressedMessage
    {
        private final BlockPos pos;
        private final int buttonID;

        public GuiButtonPressedMessage(BlockPos pos, int buttonID)
        {
            this.buttonID = buttonID;
            this.pos = pos;
        }
    }
}