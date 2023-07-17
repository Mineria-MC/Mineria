package io.github.mineria_mc.mineria.client;

import com.mojang.blaze3d.vertex.*;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.joml.Matrix4f;

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

    public static void blitNoTex(GuiGraphics graphics, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        int x2 = x + width;
        int y2 = y + height;
        float u1 = u / (float) textureWidth;
        float u2 = (u + (float) regionWidth) / (float) textureWidth;
        float v1 = v / (float) textureHeight;
        float v2 = (v + (float) regionHeight) / (float) textureHeight;

        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, x, y, 0).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix4f, x, y2, 0).uv(u1, v2).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, 0).uv(u2, v2).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y, 0).uv(u2, v1).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}
