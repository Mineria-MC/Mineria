package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.entity.goal.AlertTeamHurtByTargetGoal;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DruidicWolfEntity extends PathfinderMob {
    public DruidicWolfEntity(EntityType<? extends DruidicWolfEntity> type, Level world) {
        super(type, world);
        this.xpReward = 5;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new StrikeTargetGoal());
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, (new AlertTeamHurtByTargetGoal(this)).setAlertEntities(Wolf.class));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return MineriaSounds.DRUIDIC_WOLF_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.DRUIDIC_WOLF_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source == DamageSource.LIGHTNING_BOLT || super.isInvulnerableTo(source);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.MAX_HEALTH, 25).add(Attributes.ATTACK_DAMAGE, 4.5);
    }

    class StrikeTargetGoal extends Goal {
        protected int nextAttackTickCount;

        @Override
        public boolean canUse() {
            LivingEntity target = DruidicWolfEntity.this.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void start() {
            this.nextAttackTickCount = tickCount + 150 + random.nextInt(150);
        }

        @Override
        public void tick() {
            if (tickCount >= this.nextAttackTickCount) {
                LivingEntity target = DruidicWolfEntity.this.getTarget();
                if (!level.isClientSide() && target != null) {
                    ServerLevel world = (ServerLevel) level;
                    MineriaLightningBoltEntity.create(world, new BlockPos(target.position()), MobSpawnType.EVENT, false, 0, target::equals).ifPresent(world::addFreshEntityWithPassengers);
                }

                this.nextAttackTickCount = tickCount + 150 + random.nextInt(150);
            }
        }
    }
}
