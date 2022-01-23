package com.mineria.mod.common.init;

import com.mineria.mod.Mineria;
import com.mineria.mod.common.effects.BowelSoundsEffect;
import com.mineria.mod.common.effects.DrowningEffect;
import com.mineria.mod.common.effects.FastFreezingEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaEffects
{
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Mineria.MODID);

    public static final RegistryObject<BowelSoundsEffect> BOWEL_SOUNDS = EFFECTS.register("bowel_sounds", BowelSoundsEffect::new);
    public static final RegistryObject<Effect> VAMPIRE = EFFECTS.register("vampire", () -> new EffectImpl(EffectType.BENEFICIAL, 16719719));
    public static final RegistryObject<DrowningEffect> DROWNING = EFFECTS.register("drowning", DrowningEffect::new);
    public static final RegistryObject<Effect> NO_NATURAL_REGENERATION = EFFECTS.register("no_natural_regeneration", () -> new EffectImpl(EffectType.HARMFUL, 3607054));
    public static final RegistryObject<Effect> HALLUCINATIONS = EFFECTS.register("hallucinations", () -> new EffectImpl(EffectType.NEUTRAL, 16711840));
    public static final RegistryObject<FastFreezingEffect> FAST_FREEZING = EFFECTS.register("fast_freezing", FastFreezingEffect::new);

    static class EffectImpl extends Effect
    {
        EffectImpl(EffectType type, int color)
        {
            super(type, color);
        }
    }
}
