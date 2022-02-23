package com.mineria.mod.common.entity;

import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import java.util.Random;

public abstract class ElementaryGolemEntity extends AbstractGolem implements Enemy
{
    public ElementaryGolemEntity(EntityType<? extends ElementaryGolemEntity> type, Level world)
    {
        super(type, world);
        this.xpReward = 20;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.FALL && super.isInvulnerableTo(source);
    }

    public abstract float getAttackDamage(Random random);

    public abstract float getBlastProtectionValue();

    @Override
    public boolean doHurtTarget(Entity entity)
    {
        boolean hurt = entity.hurt(DamageSource.mobAttack(this), this.getAttackDamage(this.random));

        if (hurt)
        {
            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return hurt;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float dmg)
    {
        if(source.isExplosion())
        {
            dmg = CombatRules.getDamageAfterMagicAbsorb(dmg, getBlastProtectionValue() * 2);
        }

        super.actuallyHurt(source, dmg);
    }
}
