package io.github.mineria_mc.mineria.common.init;

import io.github.mineria_mc.mineria.common.init.datagen.MineriaBootstrapEntries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.function.Supplier;

public class MineriaDamageTypes {
    public static final MineriaBootstrapEntries<DamageType, MineriaBootstrapEntries.Entry<DamageType>> DAMAGE_TYPES = new MineriaBootstrapEntries<>(Registries.DAMAGE_TYPE);

    public static final ResourceKey<DamageType> SPIKE = register("on_spike", () -> new DamageType("on_spike", 0));
    public static final ResourceKey<DamageType> KUNAI = register("kunai", () -> new DamageType("kunai", 0.1f));
    public static final ResourceKey<DamageType> ELEMENTAL_ORB = register("elemental_orb", () -> new DamageType("elemental_orb", 0.1f));
    public static final ResourceKey<DamageType> BAMBOO_BLOWGUN = register("bamboo_blowgun", () -> new DamageType("bamboo_blowgun", 0.1f));

    private static ResourceKey<DamageType> register(String name, Supplier<DamageType> damageTypeSup) {
        return DAMAGE_TYPES.register(name, ctx -> damageTypeSup.get());
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        DAMAGE_TYPES.registerAll(ctx);
    }
}
