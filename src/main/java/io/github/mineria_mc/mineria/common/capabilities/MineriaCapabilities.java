package io.github.mineria_mc.mineria.common.capabilities;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.capabilities.ki.IKiCapability;
import io.github.mineria_mc.mineria.common.capabilities.ticking_data.ITickingDataCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MineriaCapabilities {
    public static Capability<ITickingDataCapability> TICKING_DATA = CapabilityManager.get(new CapabilityToken<>() {});
    public static Capability<IKiCapability> KI = CapabilityManager.get(new CapabilityToken<>() {});

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(ITickingDataCapability.class);
        event.register(IKiCapability.class);
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(Mineria.MODID, "ticking_data"), SimpleCapabilityProvider.serializable(new TickingDataCapabilityImpl(), () -> TICKING_DATA));
        }
        if(event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(Mineria.MODID, "ki"), SimpleCapabilityProvider.serializable(new KiCapabilityImpl(), () -> KI));
        }
    }
}
