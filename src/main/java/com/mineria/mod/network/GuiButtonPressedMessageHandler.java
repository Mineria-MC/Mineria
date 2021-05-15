package com.mineria.mod.network;

import com.mineria.mod.blocks.xp_block.TileEntityXpBlock;
import com.mineria.mod.util.GuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiButtonPressedMessageHandler implements IMessageHandler<GuiButtonPressedMessageHandler.GuiButtonPressedMessage, IMessage>
{
    @Override
    public IMessage onMessage(GuiButtonPressedMessage message, MessageContext context)
    {
        EntityPlayerMP player = context.getServerHandler().player;
        player.getServerWorld().addScheduledTask(() -> {
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            World world = player.world;
            if (!world.isBlockLoaded(pos))
                return;
            if(message.guiID == GuiHandler.GUI_XP_BLOCK)
                TileEntityXpBlock.execute(message.buttonID, pos, world, player);
        });
        return null;
    }

    public static class GuiButtonPressedMessage implements IMessage
    {
        private int guiID, buttonID, x, y, z;

        public GuiButtonPressedMessage()
        {
        }

        public GuiButtonPressedMessage(int guiID, int buttonID, int x, int y, int z)
        {
            this.guiID = guiID;
            this.buttonID = buttonID;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt(guiID);
            buf.writeInt(buttonID);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            guiID = buf.readInt();
            buttonID = buf.readInt();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
        }
    }
}