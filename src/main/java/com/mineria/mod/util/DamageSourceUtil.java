package com.mineria.mod.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

import javax.annotation.Nullable;

/**
 * Utility class for custom DamageSources.
 */
public class DamageSourceUtil
{
    public static DamageSource kunai(Entity kunai, @Nullable Entity owner)
    {
        return new IndirectEntityDamageSource("kunai", kunai, owner).setProjectile();
    }

    public static DamageSource elementalOrb(Entity elementalOrb, @Nullable Entity owner)
    {
        return new IndirectEntityDamageSource("elemental_orb", elementalOrb, owner).setProjectile();
    }

    public static DamageSource bambooBlowgun(Entity refill, @Nullable Entity owner)
    {
        return (new IndirectEntityDamageSource("bamboo_blowgun", refill, owner)).setProjectile();
    }
}
