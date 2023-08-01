package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.effects.instances.PoisonMobEffectInstance;
import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

// TODO: spawn, fix item texture (translucency)
public class FuguEntity extends AbstractFish {
    private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(FuguEntity.class, EntityDataSerializers.INT);
    private static final Predicate<LivingEntity> SCARY_MOB = (living) -> {
        if (living instanceof Player player && player.isCreative()) {
            return false;
        }
        return living.getType() == EntityType.AXOLOTL || living.getMobType() != MobType.WATER;
    };
    private static final TargetingConditions SELECTOR = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);

    protected int inflateCounter;
    protected int deflateTimer;

    public FuguEntity(EntityType<? extends FuguEntity> type, Level level) {
        super(type, level);
        this.refreshDimensions();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PuffGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PUFF_STATE, 0);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if (PUFF_STATE.equals(accessor)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("PuffState", this.getPuffState());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setPuffState(Math.min(nbt.getInt("PuffState"), 2));
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(MineriaItems.FUGU_BUCKET.get());
    }

    public int getPuffState() {
        return this.entityData.get(PUFF_STATE);
    }

    public void setPuffState(int state) {
        this.entityData.set(PUFF_STATE, state);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isEffectiveAi()) {
            if (this.inflateCounter > 0) {
                if (this.getPuffState() == 0) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(1);
                } else if (this.inflateCounter > 40 && this.getPuffState() == 1) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(2);
                }

                ++this.inflateCounter;
            } else if (this.getPuffState() != 0) {
                if (this.deflateTimer > 60 && this.getPuffState() == 2) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(1);
                } else if (this.deflateTimer > 100 && this.getPuffState() == 1) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(0);
                }

                ++this.deflateTimer;
            }
        }

        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isAlive() && this.getPuffState() > 0) {
            for(Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(0.3D), target -> SELECTOR.test(this, target))) {
                if (mob.isAlive()) {
                    this.touch(mob);
                }
            }
        }
    }

    protected void touch(Mob mob) {
        int state = this.getPuffState();

        if (mob.hurt(this.damageSources().mobAttack(this), 1 + state)) {
            PoisonMobEffectInstance.applyPoisonEffect(mob, 3, 20 * 15 * state, 0, PoisonSource.UNKNOWN);
            this.playSound(SoundEvents.PUFFER_FISH_STING, 1, 1);
        }
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        int state = this.getPuffState();
        if (player instanceof ServerPlayer serverPlayer && state > 0 && player.hurt(this.damageSources().mobAttack(this), 1 + state)) {
            if (!this.isSilent()) {
                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
            }

            PoisonMobEffectInstance.applyPoisonEffect(player, 3, 20 * 15 * state, 0, PoisonSource.UNKNOWN);
        }
    }

    @Override
    public int getExperienceReward() {
        return 3 + this.level().random.nextInt(5);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.PUFFER_FISH_FLOP;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return super.getDimensions(pose).scale(getScale(this.getPuffState()));
    }

    private static float getScale(int state) {
        return switch (state) {
            case 0 -> 0.5F;
            case 1 -> 0.7F;
            default -> 1.0F;
        };
    }

    protected static class PuffGoal extends Goal {
        private final FuguEntity fish;

        public PuffGoal(FuguEntity pFish) {
            this.fish = pFish;
        }

        @Override
        public boolean canUse() {
            List<LivingEntity> list = this.fish.level().getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), target -> FuguEntity.SELECTOR.test(this.fish, target));
            return !list.isEmpty();
        }

        @Override
        public void start() {
            this.fish.inflateCounter = 1;
            this.fish.deflateTimer = 0;
        }

        @Override
        public void stop() {
            this.fish.inflateCounter = 0;
        }
    }
}
