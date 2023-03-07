package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MineriaLightningBoltEntity extends LightningBolt {
    protected int life;
    public long seed;
    protected int flashes;
    protected boolean visualOnly;
    protected boolean spawnsFire = true;
    protected int fireRadius = 4;
    protected Predicate<Entity> targetPredicate = entity -> true;
    @Nullable
    protected ServerPlayer cause;
    protected float damage = 5.0F;

    public MineriaLightningBoltEntity(EntityType<? extends MineriaLightningBoltEntity> type, Level world) {
        super(type, world);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    @Override
    public void setVisualOnly(boolean visualOnly) {
        this.visualOnly = visualOnly;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    @Override
    public void setCause(@Nullable ServerPlayer cause) {
        this.cause = cause;
    }

    @Override
    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public float getDamage() {
        return this.damage;
    }

    public MineriaLightningBoltEntity setSpawnsFire(boolean spawnsFire) {
        this.spawnsFire = spawnsFire;
        return this;
    }

    public MineriaLightningBoltEntity setFireRadius(int fireRadius) {
        this.fireRadius = fireRadius;
        return this;
    }

    public MineriaLightningBoltEntity setTargetPredicate(Predicate<Entity> predicate) {
        this.targetPredicate = predicate;
        return this;
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.isCurrentlyGlowing());
        }

        this.baseTick();
        if (this.life == 2) {
            Difficulty difficulty = this.level.getDifficulty();
            if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                this.spawnFire(this.fireRadius);
            }

            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }

        if (this.life >= 0) {
            if (!(this.level instanceof ServerLevel)) {
                this.level.setSkyFlashTime(2);
            } else if (!this.visualOnly) {
                List<Entity> entities = this.level.getEntities(this, new AABB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : entities) {
                    if (!ForgeEventFactory.onEntityStruckByLightning(entity, this) && this.targetPredicate.test(entity))
                        entity.thunderHit((ServerLevel) this.level, this);
                }

                if (this.cause != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, entities);
                }
            }
        }

    }

    private void spawnFire(int radius) {
        if (!this.visualOnly && !this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && this.spawnsFire) {
            BlockPos pos = this.blockPosition();
            BlockState state = BaseFireBlock.getState(this.level, pos);
            if (this.level.getBlockState(pos).isAir() && state.canSurvive(this.level, pos)) {
                this.level.setBlockAndUpdate(pos, state);
            }

            for (int i = 0; i < radius; ++i) {
                BlockPos pos1 = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                state = BaseFireBlock.getState(this.level, pos1);
                if (this.level.getBlockState(pos1).isAir() && state.canSurvive(this.level, pos1)) {
                    this.level.setBlockAndUpdate(pos1, state);
                }
            }
        }
    }

    public static Optional<MineriaLightningBoltEntity> create(ServerLevel world, BlockPos pos, MobSpawnType reason, boolean spawnsFire, int fireRadius, Predicate<Entity> targetPredicate) {
        return Optional.ofNullable(MineriaEntities.MINERIA_LIGHTNING_BOLT.get().create(world, null, null, pos, reason, false, false)).map(entity -> entity.setSpawnsFire(spawnsFire).setFireRadius(fireRadius).setTargetPredicate(targetPredicate));
    }
}
