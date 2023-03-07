package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.entity.goal.AlertTeamHurtByTargetGoal;
import io.github.mineria_mc.mineria.common.init.MineriaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

public class AirSpiritEntity extends ElementaryGolemEntity implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> TRANSLUCENT = SynchedEntityData.defineId(AirSpiritEntity.class, EntityDataSerializers.BOOLEAN);
    private int hitCount;
    private int invisibleTicks;

    public AirSpiritEntity(EntityType<? extends AirSpiritEntity> type, Level world) {
        super(type, world);
        this.moveControl = new AirSpiritMovementController();
        this.navigation.setCanFloat(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetGoal());
        this.goalSelector.addGoal(3, new RangedAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class)).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Nonnull
    @Override
    protected PathNavigation createNavigation(@Nonnull Level world) {
        return new FlyingPathNavigation(this, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRANSLUCENT, false);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("HitCount", this.hitCount);
        nbt.putInt("InvisibleTicks", this.invisibleTicks);
        nbt.putBoolean("Translucent", this.isTranslucent());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.hitCount = nbt.getInt("HitCount");
        this.invisibleTicks = nbt.getInt("InvisibleTicks");
        this.setTranslucent(nbt.getBoolean("Translucent"));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.isEmptyBlock(pos.below()) ? 10 : 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.invisibleTicks > 0) {
            if (--this.invisibleTicks == 0) {
                setTranslucent(false);
            }
        }
    }

    @Override
    protected void actuallyHurt(DamageSource source, float dmg) {
        if (source.getDirectEntity() != null && !isTranslucent()) hitCount = Math.min(hitCount + 1, 5);
        super.actuallyHurt(source, dmg);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.hitCount >= 5) {
            setTranslucent(true);
            this.invisibleTicks = 60;
            this.hitCount = 0;
        }
    }

    @Override
    public TextColor getCharacteristicColor() {
        return TextColor.parseColor("#A4B5B1");
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return MineriaSounds.AIR_SPIRIT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) {
        return MineriaSounds.AIR_SPIRIT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return MineriaSounds.AIR_SPIRIT_DEATH.get();
    }

    @Override
    public float getMinAttackDamage() {
        return 7;
    }

    @Override
    public float getMaxAttackDamage() {
        return 21;
    }

    @Override
    public float getBlastProtectionValue() {
        return 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 80).add(Attributes.ATTACK_DAMAGE, 15).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FLYING_SPEED, 0.7 * (1 + 0.2 * 4));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float dmg) {
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, new ItemStack(Items.SPECTRAL_ARROW), dmg);
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - arrow.getY();
        double z = target.getZ() - this.getZ();
        double dist = Math.sqrt(x * x + z * z);
        arrow.shoot(x, y + dist * (double) 0.2F, z, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    protected void setTranslucent(boolean translucent) {
        this.entityData.set(TRANSLUCENT, translucent);
    }

    public boolean isTranslucent() {
        return this.entityData.get(TRANSLUCENT);
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return navigation.isDone() && random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Vec3 destination = this.findPos();
            if (destination != null) {
                navigation.moveTo(navigation.createPath(new BlockPos(destination), 1), 1.6D);
            }
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 direction = getViewVector(0.0F);

            Vec3 vector3d2 = HoverRandomPos.getPos(AirSpiritEntity.this, 8, 7, direction.x, direction.z, ((float) Math.PI / 2F), 4, 1);
            return vector3d2 != null ? vector3d2 : AirAndWaterRandomPos.getPos(AirSpiritEntity.this, 8, 4, -2, direction.x, direction.z, ((float) Math.PI / 2F));
        }
    }

    class AvoidTargetGoal extends Goal {
        protected Path path;

        public AvoidTargetGoal() {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            if (target != null) {
                Vec3 avoidPos = DefaultRandomPos.getPosAway(AirSpiritEntity.this, 20, 7, target.position());
                if (avoidPos == null) {
                    return false;
                } else if (target.distanceToSqr(avoidPos.x, avoidPos.y, avoidPos.z) < target.distanceToSqr(AirSpiritEntity.this)) {
                    return false;
                } else {
                    this.path = getNavigation().createPath(avoidPos.x, avoidPos.y, avoidPos.z, 1);
                    return this.path != null;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return !getNavigation().isDone();
        }

        @Override
        public void start() {
            getNavigation().moveTo(this.path, 1.2);
        }
    }

    class RangedAttackGoal extends Goal {
        private int attackDelay = 0;

        @Override
        public boolean canUse() {
            return getTarget() != null;
        }

        @Override
        public void start() {
            super.start();
            setAggressive(true);
        }

        @Override
        public void stop() {
            super.stop();
            setAggressive(false);
            this.attackDelay = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            if (target != null) {
                boolean visible = getSensing().hasLineOfSight(target);

                if (visible) {
                    if (attackDelay >= 30) {
                        performRangedAttack(target, 1);
                        attackDelay = 0;
                    } else
                        attackDelay++;
                }
            }
        }
    }

    class AirSpiritMovementController extends FlyingMoveControl {
        private final int maxTurn;
        private final boolean hoversInPlace;

        public AirSpiritMovementController() {
            super(AirSpiritEntity.this, 10, true);
            this.maxTurn = 10;
            this.hoversInPlace = true;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                setNoGravity(true);
                double distX = this.wantedX - getX();
                double distY = this.wantedY - getY();
                double distZ = this.wantedZ - getZ();
                double distSqr = distX * distX + distY * distY + distZ * distZ;
                if (distSqr < (double) 2.5000003E-7F) {
                    setYya(0.0F);
                    setZza(0.0F);
                    return;
                }

                if (getTarget() == null) {
                    float f = (float) (Mth.atan2(distZ, distX) * (double) (180F / (float) Math.PI)) - 90.0F;
                    setYRot(this.rotlerp(getYRot(), f, 90.0F));
                } else {
                    double targetDistanceX = getTarget().getX() - getX();
                    double targetDistanceZ = getTarget().getZ() - getZ();
                    setYRot(-((float) Mth.atan2(targetDistanceX, targetDistanceZ)) * (180F / (float) Math.PI));
                }
                yBodyRot = getYRot();

                float impulse;
                if (isOnGround()) {
                    impulse = (float) (this.speedModifier * getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    impulse = (float) (this.speedModifier * getAttributeValue(Attributes.FLYING_SPEED));
                }

                setSpeed(impulse);
                double horizontalDist = Math.sqrt(distX * distX + distZ * distZ);
                float f2 = (float) (-(Mth.atan2(distY, horizontalDist) * (double) (180F / (float) Math.PI)));
                setXRot(this.rotlerp(getXRot(), f2, (float) this.maxTurn));
                setYya(distY > 0.0D ? impulse : -impulse);
            } else {
                if (!this.hoversInPlace) {
                    setNoGravity(false);
                }

                setYya(0.0F);
                setZza(0.0F);
            }
        }
    }
}
