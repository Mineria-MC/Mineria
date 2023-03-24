package io.github.mineria_mc.mineria.common;

import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

import javax.annotation.Nullable;

public interface MineriaProxy {
    void clientSetup(final FMLClientSetupEvent event);

    void serverSetup(final FMLDedicatedServerSetupEvent event);

    default void openApothecariumScreen(Player player, int startPage, int pagesAmount) {
    }

    default void onXpBlockContainerOpen(Player player, XpBlockEntity tile) {
    }
}
