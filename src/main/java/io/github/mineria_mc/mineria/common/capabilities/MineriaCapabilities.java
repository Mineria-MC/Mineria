package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.Mineria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MineriaCapabilities {
    // TODO Remove deprecated caps in 1.4
    @Deprecated(forRemoval = true) // use TickingData instead
    public static Capability<IPoisonCapability> POISON_EXPOSURE = CapabilityManager.get(new CapabilityToken<>() {
    });
    @Deprecated(forRemoval = true) // use TickingData instead
    public static Capability<IElementCapability> ELEMENT_EXPOSURE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<ITickingDataCapability> TICKING_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SuppressWarnings("deprecation")
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IPoisonCapability.class);
        event.register(IElementCapability.class);
        event.register(ITickingDataCapability.class);
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(Mineria.MODID, "poison_exposure"), SimpleCapabilityProvider.serializable(new PoisonCapabilityImpl(), () -> POISON_EXPOSURE));
            event.addCapability(new ResourceLocation(Mineria.MODID, "element_exposure"), SimpleCapabilityProvider.serializable(new ElementCapabilityImpl(), () -> ELEMENT_EXPOSURE));
            event.addCapability(new ResourceLocation(Mineria.MODID, "ticking_data"), SimpleCapabilityProvider.serializable(new TickingDataCapabilityImpl(), () -> TICKING_DATA));
        }
    }
}
