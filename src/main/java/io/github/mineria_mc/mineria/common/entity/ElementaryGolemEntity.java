package io.github.mineria_mc.mineria.common.entity;

import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public abstract class ElementaryGolemEntity extends AbstractGolem implements Enemy {
    public ElementaryGolemEntity(EntityType<? extends ElementaryGolemEntity> type, Level world) {
        super(type, world);
        this.xpReward = 20;
    }

    public float getAttackDamage(RandomSource random) {
        return Math.max(0, getMinAttackDamage() + random.nextFloat() * (getMaxAttackDamage() - getMinAttackDamage()));
    }

    public abstract float getMinAttackDamage();

    public abstract float getMaxAttackDamage();

    public abstract float getBlastProtectionValue();

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = entity.hurt(damageSources().mobAttack(this), this.getAttackDamage(this.random));

        if (hurt) {
            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return hurt;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float dmg) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            dmg = CombatRules.getDamageAfterMagicAbsorb(dmg, getBlastProtectionValue() * 2);
        }

        super.actuallyHurt(source, dmg);
    }

    public abstract TextColor getCharacteristicColor();
}
