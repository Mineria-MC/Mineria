package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaEffects;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class WaterSpiritEntity extends ElementaryGolemEntity
{
    private static final DataParameter<Boolean> FROZEN = EntityDataManager.defineId(WaterSpiritEntity.class, DataSerializers.BOOLEAN);

    private boolean frozen;
    private int frozenTicks;

    public WaterSpiritEntity(EntityType<? extends WaterSpiritEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 0.6, 240, true));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(FROZEN, false);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if(!level.isClientSide)
        {
            if(frozenTicks > 0)
            {
                frozenTicks--;
                if(!isFrozen())
                    setFrozen(true);
            } else if(isFrozen())
                setFrozen(false);
        }

        /*if(nextStateTicks <= 0)
        {
            nextStateTicks = 400 + random.nextInt(500);
        }
        else
        {
            if(--nextStateTicks == 1)
                setFrozen(!this.frozen);
        }*/
    }

    @Override
    public double getAttributeValue(Attribute attribute)
    {
        double defaultValue = super.getAttributeValue(attribute);
        if(attribute.equals(Attributes.MOVEMENT_SPEED))
        {
            if(!isFrozen())
            {
                return isFrozen() ? defaultValue * 0.5 : defaultValue * (1 + 0.2 * 4);
            }
        }
        return defaultValue;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Frozen", this.frozen);
        nbt.putInt("FrozenTicks", this.frozenTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.setFrozen(nbt.getBoolean("Frozen"));
        this.frozenTicks = nbt.getInt("FrozenTicks");
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source.isFire() && super.isInvulnerableTo(source);
    }

    @Override
    public boolean doHurtTarget(Entity entity)
    {
        if(super.doHurtTarget(entity))
        {
            if(entity instanceof LivingEntity)
            {
                EffectInstance effect = isFrozen() ? new EffectInstance(MineriaEffects.FAST_FREEZING.get(), 100) : new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 2);
                ((LivingEntity) entity).addEffect(effect);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float dmg)
    {
        if(super.hurt(source, dmg))
        {
            if(source.getDirectEntity() instanceof SnowballEntity)
            {
                if(frozenTicks < 100)
                {
                    frozenTicks += 40;
//                this.level.broadcastEntityEvent(this, (byte) 60);
                } else if(frozenTicks < 160)
                {
                    frozenTicks += 30;
//                this.level.broadcastEntityEvent(this, (byte) 61);
                } else if(frozenTicks < 200)
                {
                    frozenTicks += 20;
//                this.level.broadcastEntityEvent(this, (byte) 62);
                }
                this.invulnerableTime = 0;
                this.level.broadcastEntityEvent(this, (byte) 60);
            }
            return true;
        }
        return false;
    }

    @Override
    public void handleEntityEvent(byte type)
    {
        if(type == 60)
            this.invulnerableTime = 0;
        super.handleEntityEvent(type);
    }

    /*@Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return MineriaSounds.WATER_SPIRIT_AMBIENT.get()*//*SoundEvents.GENERIC_SWIM*//*;
    }*/

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.WATER_SPIRIT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.WATER_SPIRIT_DEATH.get();
    }

    @Override
    protected float getVoicePitch()
    {
        return super.getVoicePitch() * 0.95F;
    }

    @Override
    public int getAmbientSoundInterval()
    {
        return 20;
    }

    @Override
    public float getAttackDamage(Random random)
    {
        return 5 + random.nextInt(5);
    }

    @Override
    public float getBlastProtectionValue()
    {
        return 4;
    }

    public boolean isFrozen()
    {
        return level.isClientSide ? this.entityData.get(FROZEN) : this.frozen;
    }

    public void setFrozen(boolean value)
    {
        this.frozen = value;
        this.entityData.set(FROZEN, value);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.ATTACK_DAMAGE, 15).add(Attributes.MOVEMENT_SPEED, 0.225).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    /*private static Field WANTED_X;
    private static Field WANTED_Y;
    private static Field WANTED_Z;

    class WaterSpiritMoveTowardsTargetGoal extends MoveTowardsTargetGoal
    {
        public WaterSpiritMoveTowardsTargetGoal(float distance)
        {
            super(WaterSpiritEntity.this, 0.9, distance);
        }

        @Override
        public void start()
        {
            try
            {
                if(WANTED_X == null) WANTED_X = ObfuscationReflectionHelper.findField(MoveTowardsTargetGoal.class, "field_75430_c");
                if(WANTED_Y == null) WANTED_Y = ObfuscationReflectionHelper.findField(MoveTowardsTargetGoal.class, "field_75427_d");
                if(WANTED_Z == null) WANTED_Z = ObfuscationReflectionHelper.findField(MoveTowardsTargetGoal.class, "field_75428_e");
                getNavigation().moveTo((double) WANTED_X.get(this), (double) WANTED_Y.get(this), (double) WANTED_Z.get(this), isFrozen() ? 0.9 : 0.9 * (1 + 0.2 * 4));
            }
            catch (Exception e)
            {
                super.start();
            }
        }
    }

    class WaterSpiritRandomWalkingGoal extends RandomWalkingGoal
    {
        public WaterSpiritRandomWalkingGoal(int interval, boolean checkNoActionTime)
        {
            super(WaterSpiritEntity.this, 0.6, interval, checkNoActionTime);
        }

        @Override
        public void start()
        {
            getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, isFrozen() ? this.speedModifier : this.speedModifier * (1 + 0.2 * 4));
        }
    }*/
}
