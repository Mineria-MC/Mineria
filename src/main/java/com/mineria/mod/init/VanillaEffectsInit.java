package com.mineria.mod.init;

import com.mineria.mod.References;
import com.mineria.mod.effects.PoisonEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VanillaEffectsInit
{
    private static final List<Effect> EFFECTS = new ArrayList<>();

    public static final Effect POISON = register("poison", new PoisonEffect());

    private static Effect register(String name, Effect effect)
    {
        EFFECTS.add(effect.setRegistryName("minecraft", name));
        return effect;
    }

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event)
    {
        EFFECTS.forEach(event.getRegistry()::register);
    }
}
