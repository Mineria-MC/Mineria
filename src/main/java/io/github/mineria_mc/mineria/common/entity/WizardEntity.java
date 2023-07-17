package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.init.MineriaPotions;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class WizardEntity extends Monster implements RangedAttackMob {
    public WizardEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.xpReward = 8;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WizardEntity.class, Witch.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return MineriaSounds.WIZARD_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return MineriaSounds.WIZARD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.WIZARD_DEATH.get();
    }

    @Override
    protected float getDamageAfterMagicAbsorb(@NotNull DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.is(DamageTypes.MAGIC)) {
            damage = (float) ((double) damage * 0.15D);
        }

        return damage;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        Vec3 delta = target.getDeltaMovement();
        double xDistance = target.getX() + delta.x - this.getX();
        double yDistance = target.getEyeY() - 1.1F - this.getY();
        double zDistance = target.getZ() + delta.z - this.getZ();
        double distance = Math.sqrt(xDistance * xDistance + zDistance * zDistance);
        Potion potion = Potions.HARMING;

        if (distance >= 8.0F && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            potion = Potions.SLOWNESS;
        } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
            potion = random.nextFloat() < 0.25F ? MineriaPotions.CLASS_3_POISON.get() : MineriaPotions.CLASS_2_POISON.get();
        } else if (distance <= 3.0F && !target.hasEffect(MobEffects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
            potion = Potions.WEAKNESS;
        }

        MineriaPotionEntity entity = new MineriaPotionEntity(this.level(), this);
        entity.setItem(PotionUtils.setPotion(new ItemStack(MineriaItems.MINERIA_SPLASH_POTION.get()), potion));
        entity.setXRot(getXRot() + 20.0F);
        entity.shoot(xDistance, yDistance + (distance * 0.2F), zDistance, 0.75F, 8.0F);

        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }

        this.level().addFreshEntity(entity);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose p_213348_1_, @NotNull EntityDimensions p_213348_2_) {
        return 1.62F;
    }

    @SuppressWarnings({"deprecation", "OverrideOnly"})
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag nbt) {
        if (reason.equals(MobSpawnType.REINFORCEMENT)) {
            ((ServerLevel) world).sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1.2, this.getZ(), 150, 0.3, 1, 0.3, 0.03);
        }
        if (reason.equals(MobSpawnType.STRUCTURE)) {
            this.setPersistenceRequired();
        }
        return super.finalizeSpawn(world, difficulty, reason, data, nbt);
    }
}
