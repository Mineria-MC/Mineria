package com.mineria.mod.client.events;

import com.mineria.mod.Mineria;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
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
        if(event.getGui() instanceof CreativeModeInventoryScreen screen)
        {
            Minecraft mc = screen.getMinecraft();
            Window window = mc.getWindow();

            if(Mineria.APOTHECARY_GROUP.equals(CreativeModeTab.TABS[screen.getSelectedTab()]))
            {
                RenderSystem.setShaderTexture(0, BRANDING);
                int startX = 0;
                int startY = 0;
                int drawWidth = window.getWidth() / 4;
                int drawHeight = window.getHeight() / 6;
                int width = 3000;
                int height = 1080;
                int imageWidth = 3072;
                int imageHeight = 3072;
                GuiComponent.blit(event.getMatrixStack(), startX, startY, drawWidth, drawHeight, 0, 0, width, height, imageWidth, imageHeight);
            }
        }
    }

    // Cool way to disable client input

    /*@SubscribeEvent
    public static void modifyMovementInputs(InputUpdateEvent event)
    {
        LivingEntity living = event.getEntityLiving();

        boolean lockInputs = false;

        if(!PoisonEffect.isImmune(living))
        {
            if(living.hasEffect(Effects.POISON) && living.getEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                PoisonEffectInstance poison = (PoisonEffectInstance) living.getEffect(Effects.POISON);
                if(poison.doConvulsions())
                {
                    lockInputs = true;
                }
            }
        }

        if(lockInputs)
        {
            event.getMovementInput().forwardImpulse = 0;
            event.getMovementInput().leftImpulse = 0;
        }
    }*/
}
