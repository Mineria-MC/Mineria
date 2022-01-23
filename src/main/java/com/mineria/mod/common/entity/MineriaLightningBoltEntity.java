package com.mineria.mod.common.entity;

import com.mineria.mod.common.init.MineriaEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MineriaLightningBoltEntity extends LightningBoltEntity
{
    protected int life;
    public long seed;
    protected int flashes;
    protected boolean visualOnly;
    protected boolean spawnsFire = true;
    protected int fireRadius = 4;
    protected Predicate<Entity> targetPredicate = entity -> true;
    @Nullable
    protected ServerPlayerEntity cause;
    protected float damage = 5.0F;

    public MineriaLightningBoltEntity(EntityType<? extends MineriaLightningBoltEntity> type, World world)
    {
        super(type, world);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    @Override
    public void setVisualOnly(boolean visualOnly)
    {
        this.visualOnly = visualOnly;
    }

    @Override
    public SoundCategory getSoundSource()
    {
        return SoundCategory.WEATHER;
    }

    @Override
    public void setCause(@Nullable ServerPlayerEntity cause)
    {
        this.cause = cause;
    }

    @Override
    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    @Override
    public float getDamage()
    {
        return this.damage;
    }

    public MineriaLightningBoltEntity setSpawnsFire(boolean spawnsFire)
    {
        this.spawnsFire = spawnsFire;
        return this;
    }

    public MineriaLightningBoltEntity setFireRadius(int fireRadius)
    {
        this.fireRadius = fireRadius;
        return this;
    }

    public MineriaLightningBoltEntity setTargetPredicate(Predicate<Entity> predicate)
    {
        this.targetPredicate = predicate;
        return this;
    }

    @Override
    public void tick()
    {
        if (!this.level.isClientSide) {
            this.setSharedFlag(6, this.isGlowing());
        }

        this.baseTick();
        if (this.life == 2)
        {
            Difficulty difficulty = this.level.getDifficulty();
            if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD)
            {
                this.spawnFire(this.fireRadius);
            }

            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.life;
        if (this.life < 0)
        {
            if (this.flashes == 0)
            {
                this.remove();
            } else if (this.life < -this.random.nextInt(10))
            {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }

        if (this.life >= 0)
        {
            if (!(this.level instanceof ServerWorld))
            {
                this.level.setSkyFlashTime(2);
            } else if (!this.visualOnly)
            {
                List<Entity> entities = this.level.getEntities(this, new AxisAlignedBB(this.getX() - 3.0D, this.getY() - 3.0D, this.getZ() - 3.0D, this.getX() + 3.0D, this.getY() + 6.0D + 3.0D, this.getZ() + 3.0D), Entity::isAlive);

                for (Entity entity : entities)
                {
                    if (!ForgeEventFactory.onEntityStruckByLightning(entity, this) && this.targetPredicate.test(entity))
                        entity.thunderHit((ServerWorld) this.level, this);
                }

                if (this.cause != null)
                {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, entities);
                }
            }
        }

    }

    private void spawnFire(int radius)
    {
        if (!this.visualOnly && !this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK) && this.spawnsFire)
        {
            BlockPos pos = this.blockPosition();
            BlockState state = AbstractFireBlock.getState(this.level, pos);
            if (this.level.getBlockState(pos).isAir(level, pos) && state.canSurvive(this.level, pos))
            {
                this.level.setBlockAndUpdate(pos, state);
            }

            for (int i = 0; i < radius; ++i)
            {
                BlockPos pos1 = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                state = AbstractFireBlock.getState(this.level, pos1);
                if (this.level.getBlockState(pos1).isAir(level, pos) && state.canSurvive(this.level, pos1))
                {
                    this.level.setBlockAndUpdate(pos1, state);
                }
            }
        }
    }

    public static Optional<MineriaLightningBoltEntity> create(ServerWorld world, BlockPos pos, SpawnReason reason, boolean spawnsFire, int fireRadius, Predicate<Entity> targetPredicate)
    {
        return Optional.ofNullable(MineriaEntities.MINERIA_LIGHTNING_BOLT.get().create(world, null, null, null, pos, reason, false, false)).map(entity -> entity.setSpawnsFire(spawnsFire).setFireRadius(fireRadius).setTargetPredicate(targetPredicate));
    }
}
