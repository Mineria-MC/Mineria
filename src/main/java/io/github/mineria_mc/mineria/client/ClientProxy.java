package io.github.mineria_mc.mineria.client;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.client.models.MineriaArmPoses;
import io.github.mineria_mc.mineria.client.screens.apothecarium.ApothecariumScreen;
import io.github.mineria_mc.mineria.common.MineriaProxy;
import io.github.mineria_mc.mineria.common.blocks.xp_block.XpBlockEntity;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.network.MineriaPacketHandler;
import io.github.mineria_mc.mineria.network.XpBlockMessageHandler;
import io.github.mineria_mc.mineria.util.MineriaRendering;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public final class ClientProxy implements MineriaProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MineriaRendering.registerScreenFactories();
            MineriaRendering.registerItemModelsProperties();

            if(MineriaUtils.currentDateMatches(6, 26)) {
                Minecraft.getInstance().getItemRenderer().getItemModelShaper().register(MineriaItems.LONSDALEITE_SWORD.get(), new ModelResourceLocation(new ResourceLocation(Mineria.MODID, "mrlulu_sword"), "inventory"));
            } else if(MineriaUtils.currentDateMatches(11, 10)) {
                Minecraft.getInstance().getItemRenderer().getItemModelShaper().register(MineriaItems.LONSDALEITE_SWORD.get(), new ModelResourceLocation(new ResourceLocation(Mineria.MODID, "mathys_craft_sword"), "inventory"));
            }
            MineriaArmPoses.init();
        });
    }

    @Override
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        throw new UnsupportedOperationException("Called serverSetup on ClientProxy!");
    }

    @Override
    public void openApothecariumScreen(Player player, int startPage, int pagesAmount) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null) {
            mc.setScreen(new ApothecariumScreen(startPage, pagesAmount));
        }
    }

    @Override
    public void onXpBlockContainerOpen(Player player, XpBlockEntity tile) {
        MineriaPacketHandler.PACKET_HANDLER.sendToServer(new XpBlockMessageHandler.XpBlockMessage(tile.getBlockPos()));
    }
}
