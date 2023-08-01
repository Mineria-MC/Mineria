package io.github.mineria_mc.mineria.common.capabilities.ki;

import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Arrays;

public enum KiEffectCategory {
    MOVEMENT_SPEED("movement_speed", 1,
            new KiEffectInfo(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 0), 2),
            new KiEffectInfo(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, 1), 4)),
    JUMP_BOOST("jump_boost", 1,
            new KiEffectInfo(new MobEffectInstance(MobEffects.JUMP, 1, 0), 2),
            new KiEffectInfo(new MobEffectInstance(MobEffects.JUMP, 1, 1), 4)),
    LUCK("luck", 1,
            new KiEffectInfo(new MobEffectInstance(MobEffects.LUCK, 1, 0), 1)),
    SWIMMING_SPEED("swimming_speed", 2,
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.SWIMMING_SPEED.get(), 1, 0), 5)),
    NIGHT_VISION("night_vision", 5,
            new KiEffectInfo(new MobEffectInstance(MobEffects.NIGHT_VISION, 1, 0), 6)),
    RESISTANCE("resistance", 5,
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.KNOCKBACK_RESISTANCE.get(), 1, 0), 3),
            new KiEffectInfo(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1, 0), 3),
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.EXPLOSION_RESISTANCE.get(), 1, 0), 3),
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.FALL_DAMAGE_RESISTANCE.get(), 1, 0), 8)),
    HASTE("haste", 5,
            new KiEffectInfo(new MobEffectInstance(MobEffects.DIG_SPEED, 1, 0), 9)),
    WATER_BREATHING("water_breathing", 8,
            new KiEffectInfo(new MobEffectInstance(MobEffects.WATER_BREATHING, 1, 0), 8)),
    NUTRITION_QUALITY("nutrition_quality", 9,
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.NUTRITION_QUALITY.get(), 1, 2), 6)),
    HEALTH_BOOST("health_boost", 15,
            new KiEffectInfo(new MobEffectInstance(MineriaEffects.HEALTH_BOOST.get(), 1, 9), 10));
    
    private final String id;
    private final int requiredLevel;
    private final KiEffectInfo[] effects;

    KiEffectCategory(String id, int requiredLevel, KiEffectInfo... effects) {
        this.id = id;
        this.requiredLevel = requiredLevel;
        this.effects = effects;
    }

    public Component title() {
        return Component.translatable("ki_effect_category.mineria.".concat(id));
    }

    public int requiredLevel() {
        return requiredLevel;
    }

    public KiEffectInfo[] effects() {
        return effects;
    }

    @Override
    public String toString() {
        return "KiEffectCategory[" +
                "title=" + id + ", " +
                "requiredLevel=" + requiredLevel + ", " +
                "effects=" + Arrays.toString(effects) + ']';
    }

    public record KiEffectInfo(MobEffectInstance instance, int kiConsumption) {
    }
}
