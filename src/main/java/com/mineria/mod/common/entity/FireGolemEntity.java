package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class FireGolemEntity extends ElementaryGolemEntity
{
    public FireGolemEntity(EntityType<? extends FireGolemEntity> type, Level world)
    {
        super(type, world);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.6D, 240, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if(this.tickCount % 1200 == 0)
        {
            int radius = 4;
            BlockPos pos = this.blockPosition();
            boolean clearedWater = false;

            for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius)))
            {
                if(this.level.getBlockState(blockPos).is(Blocks.WATER))
                {
                    this.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_NEIGHBORS);
                    clearedWater = true;
                }
            }

            if(clearedWater)
                this.level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public void move(MoverType type, Vec3 vec)
    {
        if(type.equals(MoverType.SELF))
        {
            BlockPos pos = this.blockPosition();
            if(!this.level.isEmptyBlock(pos.below()) && this.level.isEmptyBlock(pos) || this.level.getBlockState(pos).getMaterial().equals(Material.REPLACEABLE_PLANT))
                this.level.setBlock(pos, Blocks.FIRE.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
        super.move(type, vec);
    }

    @Override
    public float getBlastProtectionValue()
    {
        return 4;
    }

    @Override
    public boolean doHurtTarget(Entity entity)
    {
        if(super.doHurtTarget(entity))
        {
            entity.setSecondsOnFire(2);
            return true;
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float dmg)
    {
        if(super.hurt(source, dmg))
        {
            if(source.getDirectEntity() != null && random.nextFloat() < 0.1F)
            {
                source.getDirectEntity().setSecondsOnFire(1);
            }
            return true;
        }
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.FIRE_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.FIRE_GOLEM_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state)
    {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    public float getAttackDamage(Random random)
    {
        return 21 + random.nextInt(11);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 26.0D);
    }
}
