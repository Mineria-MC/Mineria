package com.mineria.mod.network;

import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiButtonPressedMessageHandler implements IMessageHandler<GuiButtonPressedMessageHandler.GUIButtonPressedMessage, IMessage>
{
    @Override
    public IMessage onMessage(GUIButtonPressedMessage message, MessageContext context)
    {
        EntityPlayerMP entity = context.getServerHandler().player;
        entity.getServerWorld().addScheduledTask(() -> {
            int buttonID = message.buttonID;
            int x = message.x;
            int y = message.y;
            int z = message.z;
            World world = entity.world;
            if (!world.isBlockLoaded(new BlockPos(x, y, z)))
                return;
            if (buttonID == 0)
            {
                TileEntityXpBlock.executeProcedure(x, y, z, world, entity);
            }
        });
        return null;
    }

    public static class GUIButtonPressedMessage implements IMessage
    {
        int buttonID, x, y, z;

        public GUIButtonPressedMessage()
        {
        }

        public GUIButtonPressedMessage(int buttonID, int x, int y, int z)
        {
            this.buttonID = buttonID;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void toBytes(io.netty.buffer.ByteBuf buf)
        {
            buf.writeInt(buttonID);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        @Override
        public void fromBytes(io.netty.buffer.ByteBuf buf)
        {
            buttonID = buf.readInt();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }
    }
}