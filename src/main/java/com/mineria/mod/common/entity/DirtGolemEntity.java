package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DirtGolemEntity extends ElementaryGolemEntity
{
    public DirtGolemEntity(EntityType<? extends DirtGolemEntity> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 0.6D, 240, true));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if(this.tickCount % 600 == 0)
        {
            int radius = 4;
            BlockPos pos = this.blockPosition();
            boolean fertilizedDirt = false;

            for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius)))
            {
                BlockState state = this.level.getBlockState(blockPos);

                if(state.getBlock() instanceof IGrowable)
                {
                    IGrowable growable = (IGrowable) state.getBlock();

                    if(growable.isValidBonemealTarget(this.level, blockPos, state, this.level.isClientSide))
                    {
                        if(this.level instanceof ServerWorld && growable.isBonemealSuccess(this.level, this.level.random, blockPos, state))
                        {
                            growable.performBonemeal((ServerWorld) this.level, this.level.random, blockPos, state);
                        }

                        fertilizedDirt = true;
                    }
                }
            }

            if(fertilizedDirt)
                this.level.playSound(null, pos, SoundEvents.COMPOSTER_READY, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public float getBlastProtectionValue()
    {
        return 8;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.DIRT_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.DIRT_GOLEM_DEATH.get();
    }

    @Override
    public float getAttackDamage(Random random)
    {
        return 11 + random.nextInt(11);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 16.0D).add(Attributes.ATTACK_KNOCKBACK, 2);
    }
}
