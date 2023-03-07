package io.github.mineria_mc.mineria.server;

import io.github.mineria_mc.mineria.common.MineriaProxy;
import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public final class ServerProxy implements MineriaProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        throw new UnsupportedOperationException("Called clientSetup on ServerProxy!");
    }

    @Override
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
    }

    @Override
    public void onXpBlockContainerOpen(Player player, XpBlockTileEntity tile) {
        tile.onOpen(player);
    }
}
