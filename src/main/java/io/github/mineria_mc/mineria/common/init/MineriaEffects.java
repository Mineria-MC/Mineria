package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.BowelSoundsEffect;
import io.github.mineria_mc.mineria.common.effects.DrowningEffect;
import io.github.mineria_mc.mineria.common.effects.FastFreezingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MineriaEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Mineria.MODID);

    public static final RegistryObject<BowelSoundsEffect> BOWEL_SOUNDS = EFFECTS.register("bowel_sounds", BowelSoundsEffect::new);
    public static final RegistryObject<MobEffect> VAMPIRE = EFFECTS.register("vampire", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 16719719));
    public static final RegistryObject<DrowningEffect> DROWNING = EFFECTS.register("drowning", DrowningEffect::new);
    public static final RegistryObject<MobEffect> NO_NATURAL_REGENERATION = EFFECTS.register("no_natural_regeneration", () -> new EffectImpl(MobEffectCategory.HARMFUL, 3607054));
    public static final RegistryObject<MobEffect> HALLUCINATIONS = EFFECTS.register("hallucinations", () -> new EffectImpl(MobEffectCategory.NEUTRAL, 16711840));
    public static final RegistryObject<FastFreezingEffect> FAST_FREEZING = EFFECTS.register("fast_freezing", FastFreezingEffect::new);

    static class EffectImpl extends MobEffect {
        EffectImpl(MobEffectCategory type, int color) {
            super(type, color);
        }
    }
}
