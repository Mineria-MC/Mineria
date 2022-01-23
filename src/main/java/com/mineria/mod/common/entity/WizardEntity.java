package com.mineria.mod.common.entity;

import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.init.MineriaPotions;
import com.mineria.mod.common.init.MineriaSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class WizardEntity extends MonsterEntity implements IRangedAttackMob
{
    public WizardEntity(EntityType<? extends MonsterEntity> type, World world)
    {
        super(type, world);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WizardEntity.class, WitchEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, null));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return MineriaSounds.WIZARD_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.WIZARD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.WIZARD_DEATH.get();
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage)
    {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this)
        {
            damage = 0.0F;
        }

        if (source.isMagic())
        {
            damage = (float) ((double) damage * 0.15D);
        }

        return damage;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_)
    {
        Vector3d delta = target.getDeltaMovement();
        double xDistance = target.getX() + delta.x - this.getX();
        double yDistance = target.getEyeY() - 1.1F - this.getY();
        double zDistance = target.getZ() + delta.z - this.getZ();
        float distance = MathHelper.sqrt(xDistance * xDistance + zDistance * zDistance);
        Potion potion = Potions.HARMING;

        if (distance >= 8.0F && !target.hasEffect(Effects.MOVEMENT_SLOWDOWN))
        {
            potion = Potions.SLOWNESS;
        } else if (target.getHealth() >= 8.0F && !target.hasEffect(Effects.POISON))
        {
            potion = random.nextFloat() < 0.25F ? MineriaPotions.CLASS_3_POISON.get() : MineriaPotions.CLASS_2_POISON.get();
        } else if (distance <= 3.0F && !target.hasEffect(Effects.WEAKNESS) && this.random.nextFloat() < 0.25F)
        {
            potion = Potions.WEAKNESS;
        }

        MineriaPotionEntity entity = new MineriaPotionEntity(this.level, this);
        entity.setItem(PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_SPLASH_POTION), potion));
        entity.xRot -= -20.0F;
        entity.shoot(xDistance, yDistance + (double) (distance * 0.2F), zDistance, 0.75F, 8.0F);

        if (!this.isSilent())
        {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }

        this.level.addFreshEntity(entity);
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_)
    {
        return 1.62F;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt)
    {
        if(reason.equals(SpawnReason.REINFORCEMENT))
        {
            ((ServerWorld) world).sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.2, this.getZ(), 150, 0.3, 1, 0.3, 0.03);
        }
        if(reason.equals(SpawnReason.STRUCTURE))
        {
            this.setPersistenceRequired();
        }
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }
}
