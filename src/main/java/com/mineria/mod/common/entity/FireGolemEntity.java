package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class FireGolemEntity extends ElementaryGolemEntity
{
    public FireGolemEntity(EntityType<? extends FireGolemEntity> type, World world)
    {
        super(type, world);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
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

        if(this.tickCount % 1200 == 0)
        {
            int radius = 4;
            BlockPos pos = this.blockPosition();
            boolean clearedWater = false;

            for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius)))
            {
                if(this.level.getBlockState(blockPos).is(Blocks.WATER))
                {
                    this.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.NOTIFY_NEIGHBORS);
                    clearedWater = true;
                }
            }

            if(clearedWater)
                this.level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public void move(MoverType type, Vector3d vec)
    {
        if(type.equals(MoverType.SELF))
        {
            BlockPos pos = this.blockPosition();
            if(!this.level.isEmptyBlock(pos.below()) && this.level.isEmptyBlock(pos) || this.level.getBlockState(pos).getMaterial().equals(Material.REPLACEABLE_PLANT))
                this.level.setBlock(pos, Blocks.FIRE.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
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

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_DAMAGE, 26.0D);
    }
}
