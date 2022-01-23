package com.mineria.mod.client.events;

import com.mineria.mod.Mineria;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Hooks for every Forge event on client-side.
 */
@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventHandler
{
    private static final ResourceLocation BRANDING = new ResourceLocation(Mineria.MODID, "textures/branding/branding_mineria_apothecary_update.png");

    @SubscribeEvent
    public static void onRenderBackground(GuiScreenEvent.BackgroundDrawnEvent event)
    {
        if(event.getGui() instanceof CreativeScreen)
        {
            CreativeScreen screen = (CreativeScreen) event.getGui();
            Minecraft mc = screen.getMinecraft();
            MainWindow window = mc.getWindow();

            if(Mineria.APOTHECARY_GROUP.equals(ItemGroup.TABS[screen.getSelectedTab()]))
            {
                mc.getTextureManager().bind(BRANDING);
                int startX = 0;
                int startY = 0;
                int drawWidth = window.getWidth() / 4;
                int drawHeight = window.getHeight() / 6;
                int width = 3000;
                int height = 1080;
                int imageWidth = 3072;
                int imageHeight = 3072;
                AbstractGui.blit(event.getMatrixStack(), startX, startY, drawWidth, drawHeight, 0, 0, width, height, imageWidth, imageHeight);
            }
        }
    }
}
