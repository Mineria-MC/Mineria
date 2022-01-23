package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DruidicWolfEntity extends CreatureEntity
{
    public DruidicWolfEntity(EntityType<? extends DruidicWolfEntity> type, World world)
    {
        super(type, world);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new StrikeTargetGoal());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, (new AlertTeamHurtByTargetGoal(this)).setAlertEntities(WolfEntity.class));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state)
    {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.WOLF_GROWL;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.DRUIDIC_WOLF_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.DRUIDIC_WOLF_DEATH.get();
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source)
    {
        return source == DamageSource.LIGHTNING_BOLT || super.isInvulnerableTo(source);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MobEntity.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.MAX_HEALTH, 25).add(Attributes.ATTACK_DAMAGE, 4.5);
    }

    class StrikeTargetGoal extends Goal
    {
        protected int nextAttackTickCount;

        @Override
        public boolean canUse()
        {
            LivingEntity target = DruidicWolfEntity.this.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start()
        {
            this.nextAttackTickCount = tickCount + 150 + random.nextInt(150);
        }

        @Override
        public void tick()
        {
            if(tickCount >= this.nextAttackTickCount)
            {
                LivingEntity target = DruidicWolfEntity.this.getTarget();
                if(!level.isClientSide() && target != null)
                {
                    ServerWorld world = (ServerWorld) level;
                    MineriaLightningBoltEntity.create(world, new BlockPos(target.position()), SpawnReason.EVENT, false, 0, target::equals).ifPresent(world::addFreshEntityWithPassengers);
                }

                this.nextAttackTickCount = tickCount + 150 + random.nextInt(150);
            }
        }
    }
}
