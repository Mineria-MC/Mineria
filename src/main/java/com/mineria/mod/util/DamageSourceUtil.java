package com.mineria.mod.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

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
