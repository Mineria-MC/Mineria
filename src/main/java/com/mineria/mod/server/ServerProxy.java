package com.mineria.mod.server;

import com.mineria.mod.common.CommonProxy;
import com.mineria.mod.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.world.entity.player.Player;

public final class ServerProxy extends CommonProxy
{
    @Override
    public void onXpBlockContainerOpen(Player player, XpBlockTileEntity tile)
    {
        tile.onOpen(player);
    }
}
