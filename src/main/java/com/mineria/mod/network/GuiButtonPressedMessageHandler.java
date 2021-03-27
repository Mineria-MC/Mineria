package com.mineria.mod.network;

import com.mineria.mod.blocks.xp_block.XpBlockTileEntity;
import com.mineria.mod.util.IMessageHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class GuiButtonPressedMessageHandler implements IMessageHandler<GuiButtonPressedMessageHandler.GuiButtonPressedMessage>
{
    @Override
    public void onMessage(GuiButtonPressedMessage message, Supplier<NetworkEvent.Context> ctx)
    {
        ServerPlayerEntity player = ctx.get().getSender();
        player.getServer().deferTask(() -> {
            World world = player.world;
            if (!world.isBlockLoaded(message.pos))
                return;
            if (message.buttonID == 0)
            {
                XpBlockTileEntity.executeProcedure(message.pos, world, player);
            }
        });
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