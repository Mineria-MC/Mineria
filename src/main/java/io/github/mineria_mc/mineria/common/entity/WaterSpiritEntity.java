package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.entity.goal.AlertTeamHurtByTargetGoal;
import io.github.mineria_mc.mineria.common.init.MineriaEffects;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import io.github.mineria_mc.mineria.util.MineriaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterSpiritEntity extends ElementaryGolemEntity {
    private static final EntityDataAccessor<Boolean> FROZEN = SynchedEntityData.defineId(WaterSpiritEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean frozen;
    private int frozenTicks;

    public WaterSpiritEntity(EntityType<? extends WaterSpiritEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.6, 240, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROZEN, false);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(!isFrozen()) {
            for (BlockPos pos : BlockPos.betweenClosed(blockPosition().offset(-1, -1, -1), blockPosition().offset(1, 1, 1))) {
                if(level.getBlockState(pos).is(Blocks.FIRE)) {
                    level.removeBlock(pos, false);
                }
            }
        }

        if (!level.isClientSide) {
            if (frozenTicks > 0) {
                frozenTicks--;
                if (!isFrozen()) {
                    setFrozen(true);
                }
            } else if (isFrozen()) {
                melt();
            }
        }
    }

    private void melt() {
        if(frozenTicks != 0) {
            frozenTicks = 0;
        }
        level.broadcastEntityEvent(this, (byte) 61);
        level.playSound(null, this, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1f, 1.0f);
        setFrozen(false);
    }

    @Override
    public double getAttributeValue(@Nonnull Attribute attribute) {
        double defaultValue = super.getAttributeValue(attribute);
        if (attribute.equals(Attributes.MOVEMENT_SPEED)) {
            if (!isFrozen()) {
                return defaultValue * (1 + 0.2 * 4);
            }
        }
        return defaultValue;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Frozen", isFrozen());
        nbt.putInt("FrozenTicks", this.frozenTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setFrozen(nbt.getBoolean("Frozen"));
        this.frozenTicks = nbt.getInt("FrozenTicks");
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity living) {
                MobEffectInstance effect = isFrozen() ? new MobEffectInstance(MineriaEffects.FAST_FREEZING.get(), 100) : new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2);
                living.addEffect(effect, this);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float dmg) {
        if (super.hurt(source, dmg)) {
            if (source.getDirectEntity() instanceof Snowball) {
                if (frozenTicks < 200) {
                    frozenTicks += 80;
                } else if (frozenTicks < 320) {
                    frozenTicks += 60;
                } else if (frozenTicks < 400) {
                    frozenTicks += 40;
                }
                this.invulnerableTime = 0;
                this.level.broadcastEntityEvent(this, (byte) 60);
            }
            return true;
        }
        return false;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        float damage = super.getDamageAfterMagicAbsorb(source, amount);
        if(!isFrozen()) {
            return damage;
        }

        if(source.getEntity() instanceof LivingEntity living) {
            if(EnchantmentHelper.getFireAspect(living) > 0) {
                melt();
                level.playSound(null, this, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 1f, 1.0f);
                return damage * 2;
            }
        }
        if (source instanceof IndirectEntityDamageSource indirect) {
            Entity directEntity = indirect.getDirectEntity();
            if(directEntity != null && directEntity.isOnFire()) {
                melt();
                level.playSound(null, this, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 1f, 1.0f);
                return damage * 2;
            }
        }
        return damage;
    }

    @Override
    public void handleEntityEvent(byte type) {
        if (type == 60) {
            this.invulnerableTime = 0;
        }
        if(type == 61) {
            MineriaUtils.addParticles(level, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PACKED_ICE.defaultBlockState()), getX(), getY() + 1.5, getZ(), 0.275, 0.6, 0.275, 0, 50, false);
        }
        super.handleEntityEvent(type);
    }

    @Override
    public boolean fireImmune() {
        return !isFrozen();
    }

    @Override
    public TextColor getCharacteristicColor() {
        return TextColor.parseColor("#1446F3");
    }

    /*@Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return MineriaSounds.WATER_SPIRIT_AMBIENT.get()*//*SoundEvents.GENERIC_SWIM*//*;
    }*/

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) {
        return MineriaSounds.WATER_SPIRIT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.WATER_SPIRIT_DEATH.get();
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 20;
    }

    @Override
    public float getMinAttackDamage() {
        return 5;
    }

    @Override
    public float getMaxAttackDamage() {
        return 10;
    }

    @Override
    public float getBlastProtectionValue() {
        return 4;
    }

    public boolean isFrozen() {
        return level.isClientSide ? this.entityData.get(FROZEN) : this.frozen;
    }

    public void setFrozen(boolean value) {
        this.frozen = value;
        this.entityData.set(FROZEN, value);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.ATTACK_DAMAGE, 15).add(Attributes.MOVEMENT_SPEED, 0.225).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }
}
