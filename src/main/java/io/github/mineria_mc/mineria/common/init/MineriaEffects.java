package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.Mineria;
import io.github.mineria_mc.mineria.common.effects.BowelSoundsEffect;
import io.github.mineria_mc.mineria.common.effects.DrowningEffect;
import io.github.mineria_mc.mineria.common.effects.FastFreezingEffect;
import net.minecraft.world.effect.HealthBoostMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineriaEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Mineria.MODID);

    public static final RegistryObject<BowelSoundsEffect> BOWEL_SOUNDS = EFFECTS.register("bowel_sounds", BowelSoundsEffect::new);
    public static final RegistryObject<MobEffect> VAMPIRE = EFFECTS.register("vampire", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 16719719));
    public static final RegistryObject<DrowningEffect> DROWNING = EFFECTS.register("drowning", DrowningEffect::new);
    public static final RegistryObject<MobEffect> NO_NATURAL_REGENERATION = EFFECTS.register("no_natural_regeneration", () -> new EffectImpl(MobEffectCategory.HARMFUL, 3607054));
    public static final RegistryObject<MobEffect> HALLUCINATIONS = EFFECTS.register("hallucinations", () -> new EffectImpl(MobEffectCategory.NEUTRAL, 16711840));
    public static final RegistryObject<FastFreezingEffect> FAST_FREEZING = EFFECTS.register("fast_freezing", FastFreezingEffect::new);
    public static final RegistryObject<MobEffect> SWIMMING_SPEED = EFFECTS.register("swimming_speed", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 0x4df9ff).addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "bec36be7-b44f-4b66-97fa-7a12b3d4ac9a", 0.5, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> KNOCKBACK_RESISTANCE = EFFECTS.register("knockback_resistance", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 0x7a300d).addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "c8b9cc13-0ea5-445c-9d44-7cdeb9bc9433", 1.0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> EXPLOSION_RESISTANCE = EFFECTS.register("explosion_resistance", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 0x781e1e));
    public static final RegistryObject<MobEffect> FALL_DAMAGE_RESISTANCE = EFFECTS.register("fall_damage_resistance", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 0x5c630e));
    public static final RegistryObject<MobEffect> NUTRITION_QUALITY = EFFECTS.register("nutrition_quality", () -> new EffectImpl(MobEffectCategory.BENEFICIAL, 0xeb8423));
    public static final RegistryObject<MobEffect> HEALTH_BOOST = EFFECTS.register("health_boost", () -> new HealthBoostMobEffect(MobEffectCategory.BENEFICIAL, 16284963).addAttributeModifier(Attributes.MAX_HEALTH, "178a427e-ad38-4785-8830-a982dab1cfbc", 1.0, AttributeModifier.Operation.ADDITION));

    static class EffectImpl extends MobEffect {
        EffectImpl(MobEffectCategory type, int color) {
            super(type, color);
        }
    }
}
