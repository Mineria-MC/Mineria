package io.github.mineria_mc.mineria.util;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

import javax.annotation.Nullable;

/**
 * Utility class for custom DamageSources.
 */
public class DamageSourceUtil {
    public static DamageSource kunai(Entity kunai, @Nullable Entity owner) {
        return new IndirectEntityDamageSource("kunai", kunai, owner).setProjectile();
    }

    public static DamageSource elementalOrb(Entity owner) {
        return new EntityDamageSource("elemental_orb", owner).setProjectile();
    }

    public static DamageSource bambooBlowgun(Entity refill, @Nullable Entity owner) {
        return (new IndirectEntityDamageSource("bamboo_blowgun", refill, owner)).setProjectile();
    }
}
