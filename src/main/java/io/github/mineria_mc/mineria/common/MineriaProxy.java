package io.github.mineria_mc.mineria.common;

import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public interface MineriaProxy {
    void clientSetup(final FMLClientSetupEvent event);

    void serverSetup(final FMLDedicatedServerSetupEvent event);

    default void openApothecariumScreen(Player player) {
    }

    default void onXpBlockContainerOpen(Player player, XpBlockTileEntity tile) {
    }
}
