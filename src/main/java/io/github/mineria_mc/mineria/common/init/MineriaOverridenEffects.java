package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.PoisonEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineriaOverridenEffects {
    public static final MobEffect POISON = new PoisonEffect();

    @SubscribeEvent
    public static void registerEffects(RegisterEvent event) {
        event.register(Registries.MOB_EFFECT, helper -> helper.register(new ResourceLocation("poison"), POISON));
    }
}
