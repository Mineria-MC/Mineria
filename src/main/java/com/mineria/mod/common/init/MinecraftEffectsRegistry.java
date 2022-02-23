package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftEffectsRegistry
{
    private static final List<MobEffect> EFFECTS = new ArrayList<>();

    public static final MobEffect POISON = register(new PoisonEffect());

    private static MobEffect register(MobEffect effect)
    {
        EFFECTS.add(effect);
        return effect;
    }

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<MobEffect> event)
    {
        EFFECTS.forEach(event.getRegistry()::register);
    }
}
