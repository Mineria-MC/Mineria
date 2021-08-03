package com.mineria.mod.util;

import com.mineria.mod.References;
import com.mineria.mod.blocks.barrel.golden.GoldenWaterBarrelTileEntity;
import com.mineria.mod.capabilities.CapabilityRegistry;
import com.mineria.mod.capabilities.IIngestedFood;
import com.mineria.mod.capabilities.provider.IngestedFoodProvider;
import com.mineria.mod.effects.PoisonEffect;
import com.mineria.mod.effects.PoisonEffectInstance;
import com.mineria.mod.init.BiomesInit;
import com.mineria.mod.world.gen.WorldGenerationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        event.getEntityLiving().getCapability(CapabilityRegistry.INGESTED_FOOD_CAP).ifPresent(IIngestedFood::increaseTicksSinceFoodIngested);
    }

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.Watch event)
    {
        event.getWorld().getChunk(event.getPos().asBlockPos()).getTileEntitiesPos().stream()
                .map(event.getWorld()::getTileEntity)
                .filter(GoldenWaterBarrelTileEntity.class::isInstance)
                .map(GoldenWaterBarrelTileEntity.class::cast)
                .forEach(GoldenWaterBarrelTileEntity::reloadBlockState);
    }

    @SubscribeEvent
    public static void modifyMovementInputs(InputUpdateEvent event)
    {
        LivingEntity living = event.getEntityLiving();

        if(!PoisonEffect.isImmune(living))
        {
            if(living.isPotionActive(Effects.POISON) && living.getActivePotionEffect(Effects.POISON) instanceof PoisonEffectInstance)
            {
                PoisonEffectInstance poison = (PoisonEffectInstance) living.getActivePotionEffect(Effects.POISON);
                if(poison.doConvulsions())
                {
                    event.getMovementInput().moveForward = 0;
                    event.getMovementInput().moveStrafe = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void loadBiomes(BiomeLoadingEvent event)
    {
        WorldGenerationHandler.loadVanillaBiomes(event);
        BiomesInit.loadModdedBiomes(event);
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof LivingEntity)
            event.addCapability(new ResourceLocation(References.MODID, "food_info"), new IngestedFoodProvider(CapabilityRegistry.INGESTED_FOOD_CAP.getDefaultInstance()));
    }
}
