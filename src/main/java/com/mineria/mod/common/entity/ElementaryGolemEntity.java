package com.mineria.mod.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class ElementaryGolemEntity extends GolemEntity implements IMob
{
    public ElementaryGolemEntity(EntityType<? extends ElementaryGolemEntity> type, World world)
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
