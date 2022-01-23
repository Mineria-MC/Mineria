package com.mineria.mod.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BrownBearEntity extends CreatureEntity
{
    private static final DataParameter<Boolean> DATA_STANDING_ID = EntityDataManager.defineId(BrownBearEntity.class, DataSerializers.BOOLEAN);
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    public final boolean isLightBrown;

    public BrownBearEntity(EntityType<? extends BrownBearEntity> type, World world)
    {
        super(type, world);
        this.xpReward = 5;
        this.isLightBrown = random.nextFloat() < 0.1F;
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal());
        this.goalSelector.addGoal(4, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, FoxEntity.class, 10, true, true, null));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state)
    {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_STANDING_ID, false);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.level.isClientSide)
        {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO)
            {
                this.refreshDimensions();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding())
            {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else
            {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }
    }

    @Override
    public EntitySize getDimensions(Pose pose)
    {
        if (this.clientSideStandAnimation > 0.0F)
        {
            float heightValue = this.clientSideStandAnimation / 6.0F;
            float heightScaleFactor = 1.0F + heightValue;
            return super.getDimensions(pose).scale(1.0F, heightScaleFactor);
        } else
        {
            return super.getDimensions(pose);
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean didHurt = target.hurt(DamageSource.mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (didHurt)
            this.doEnchantDamageEffects(this, target);

        return didHurt;
    }

    public boolean isStanding()
    {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean standing)
    {
        this.entityData.set(DATA_STANDING_ID, standing);
    }

    @OnlyIn(Dist.CLIENT)
    public float getStandingAnimationScale(float p_189795_1_)
    {
        return MathHelper.lerp(p_189795_1_, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    @Override
    protected float getWaterSlowDown()
    {
        return 0.98F;
    }

    class MeleeAttackGoal extends net.minecraft.entity.ai.goal.MeleeAttackGoal {
        public MeleeAttackGoal()
        {
            super(BrownBearEntity.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity target, double distance)
        {
            double reach = this.getAttackReachSqr(target);
            if (distance <= reach && this.isTimeToAttack())
            {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                BrownBearEntity.this.setStanding(false);
            } else if (distance <= reach * 2.0D)
            {
                if (this.isTimeToAttack())
                {
                    BrownBearEntity.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10)
                {
                    BrownBearEntity.this.setStanding(true);
                }
            } else
            {
                this.resetAttackCooldown();
                BrownBearEntity.this.setStanding(false);
            }
        }

        public void stop()
        {
            BrownBearEntity.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity target)
        {
            return 4 + target.getBbWidth();
        }
    }
}
