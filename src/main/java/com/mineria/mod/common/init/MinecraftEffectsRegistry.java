package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.PoisonEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Mineria.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftEffectsRegistry
{
    private static final List<Effect> EFFECTS = new ArrayList<>();

    public static final Effect POISON = register(new PoisonEffect());

    private static Effect register(Effect effect)
    {
        EFFECTS.add(effect);
        return effect;
    }

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event)
    {
        EFFECTS.forEach(event.getRegistry()::register);
    }
}
