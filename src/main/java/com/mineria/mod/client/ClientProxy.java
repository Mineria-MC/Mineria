package com.mineria.mod.client;

import com.mineria.mod.client.screens.ApothecariumScreen;
import com.mineria.mod.common.CommonProxy;
import com.mineria.mod.common.blocks.xp_block.XpBlockTileEntity;
import com.mineria.mod.network.MineriaPacketHandler;
import com.mineria.mod.network.XpBlockMessageHandler;
import com.mineria.mod.util.MineriaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public final class ClientProxy extends CommonProxy
{
    @Override
    public void openApothecariumScreen(Player player)
    {
        if(player instanceof LocalPlayer)
        {
            Minecraft mc = Minecraft.getInstance();
            if(mc.screen == null)
            {
                mc.setScreen(new ApothecariumScreen());
            }
        }
    }

    @Override
    public void onXpBlockContainerOpen(Player player, XpBlockTileEntity tile)
    {
        MineriaPacketHandler.PACKET_HANDLER.sendToServer(new XpBlockMessageHandler.XpBlockMessage(tile.getBlockPos()));
    }
}
