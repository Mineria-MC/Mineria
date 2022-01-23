package com.mineria.mod.common.entity;

import com.mineria.mod.common.entity.goal.AlertTeamHurtByTargetGoal;
import com.mineria.mod.common.init.MineriaSounds;
import com.mineria.mod.util.RandomPositionGeneratorUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class AirSpiritEntity extends ElementaryGolemEntity implements IRangedAttackMob
{
    private static final DataParameter<Boolean> TRANSLUCENT = EntityDataManager.defineId(AirSpiritEntity.class, DataSerializers.BOOLEAN);
    private int hitCount;
    private int invisibleTicks;

    public AirSpiritEntity(EntityType<? extends AirSpiritEntity> type, World world)
    {
        super(type, world);
        this.moveControl = new AirSpiritMovementController();
        this.navigation.setCanFloat(false);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetGoal());
        this.goalSelector.addGoal(3, new RangedAttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new AlertTeamHurtByTargetGoal(this, AbstractDruidEntity.class, ElementaryGolemEntity.class)).setAlertEntities(ElementaryGolemEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected PathNavigator createNavigation(World world)
    {
        return new FlyingPathNavigator(this, world);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(TRANSLUCENT, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("HitCount", this.hitCount);
        nbt.putInt("InvisibleTicks", this.invisibleTicks);
        nbt.putBoolean("Translucent", this.isTranslucent());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.hitCount = nbt.getInt("HitCount");
        this.invisibleTicks = nbt.getInt("InvisibleTicks");
        this.setTranslucent(nbt.getBoolean("Translucent"));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, IWorldReader world)
    {
        return world.isEmptyBlock(pos.below()) ? 10 : 0;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.invisibleTicks > 0)
        {
            if(--this.invisibleTicks == 0)
                setTranslucent(false);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource source, float dmg)
    {
        if(source.getDirectEntity() != null && !isTranslucent()) hitCount = Math.min(hitCount + 1, 5);
        super.actuallyHurt(source, dmg);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if(this.hitCount >= 5)
        {
            setTranslucent(true);
            this.invisibleTicks = 60;
            this.hitCount = 0;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return MineriaSounds.AIR_SPIRIT_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MineriaSounds.AIR_SPIRIT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return MineriaSounds.AIR_SPIRIT_DEATH.get();
    }

    @Override
    public float getAttackDamage(Random random)
    {
        return 7 + random.nextInt(15);
    }

    @Override
    public float getBlastProtectionValue()
    {
        return 2;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes()
    {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 80).add(Attributes.ATTACK_DAMAGE, 15).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FLYING_SPEED, 0.7 * (1 + 0.2 * 4));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float dmg)
    {
        AbstractArrowEntity arrow = ProjectileHelper.getMobArrow(this, new ItemStack(Items.SPECTRAL_ARROW), dmg);
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - arrow.getY();
        double z = target.getZ() - this.getZ();
        double dist = MathHelper.sqrt(x * x + z * z);
        arrow.shoot(x, y + dist * (double)0.2F, z, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    protected void setTranslucent(boolean translucent)
    {
        this.entityData.set(TRANSLUCENT, translucent);
    }

    public boolean isTranslucent()
    {
        return this.entityData.get(TRANSLUCENT);
    }

    class MoveRandomGoal extends Goal
    {
        public MoveRandomGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse()
        {
            return navigation.isDone() && random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse()
        {
            return false;
        }

        @Override
        public void start()
        {
            Vector3d destination = this.findPos();
            if (destination != null)
            {
                navigation.moveTo(navigation.createPath(new BlockPos(destination), 1), 1.6D);
            }
        }

        @Nullable
        private Vector3d findPos()
        {
            Vector3d direction = getViewVector(0.0F);

            Vector3d vector3d2 = RandomPositionGeneratorUtil.getAboveLandPos(AirSpiritEntity.this, 8, 7, direction, ((float) Math.PI / 2F), 4, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGeneratorUtil.getAirPos(AirSpiritEntity.this, 8, 4, -2, direction, ((float) Math.PI / 2F));
        }
    }

    class AvoidTargetGoal extends Goal
    {
        protected Path path;

        public AvoidTargetGoal()
        {
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse()
        {
            LivingEntity target = getTarget();
            if(target != null)
            {
                Vector3d avoidPos = RandomPositionGenerator.getPosAvoid(AirSpiritEntity.this, 20, 7, target.position());
                if (avoidPos == null)
                {
                    return false;
                } else if (target.distanceToSqr(avoidPos.x, avoidPos.y, avoidPos.z) < target.distanceToSqr(AirSpiritEntity.this))
                {
                    return false;
                } else
                {
                    this.path = getNavigation().createPath(avoidPos.x, avoidPos.y, avoidPos.z, 1);
                    return this.path != null;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse()
        {
            return !getNavigation().isDone();
        }

        @Override
        public void start()
        {
            getNavigation().moveTo(this.path, 1.2);
        }
    }

    class RangedAttackGoal extends Goal
    {
        private int attackDelay = 0;

        @Override
        public boolean canUse()
        {
            return getTarget() != null;
        }

        @Override
        public void start()
        {
            super.start();
            setAggressive(true);
        }

        @Override
        public void stop()
        {
            super.stop();
            setAggressive(false);
            this.attackDelay = 0;
        }

        @Override
        public void tick()
        {
            LivingEntity target = getTarget();
            if(target != null)
            {
                boolean visible = getSensing().canSee(target);

                if(visible)
                {
                    if(attackDelay >= 30)
                    {
                        performRangedAttack(target, 1);
                        attackDelay = 0;
                    }
                    else
                        attackDelay++;
                }
            }
        }
    }

    class AirSpiritMovementController extends FlyingMovementController
    {
        private final int maxTurn;
        private final boolean hoversInPlace;

        public AirSpiritMovementController()
        {
            super(AirSpiritEntity.this, 10, true);
            this.maxTurn = 10;
            this.hoversInPlace = true;
        }

        @Override
        public void tick()
        {
            if (this.operation == MovementController.Action.MOVE_TO)
            {
                this.operation = MovementController.Action.WAIT;
                setNoGravity(true);
                double distX = this.wantedX - getX();
                double distY = this.wantedY - getY();
                double distZ = this.wantedZ - getZ();
                double distSqr = distX * distX + distY * distY + distZ * distZ;
                if (distSqr < (double) 2.5000003E-7F)
                {
                    setYya(0.0F);
                    setZza(0.0F);
                    return;
                }

                if(getTarget() == null)
                {
                    float f = (float) (MathHelper.atan2(distZ, distX) * (double) (180F / (float) Math.PI)) - 90.0F;
                    yRot = this.rotlerp(yRot, f, 90.0F);
                } else
                {
                    double targetDistanceX = getTarget().getX() - getX();
                    double targetDistanceZ = getTarget().getZ() - getZ();
                    yRot = -((float) MathHelper.atan2(targetDistanceX, targetDistanceZ)) * (180F / (float) Math.PI);
                }
                yBodyRot = yRot;

                float impulse;
                if (isOnGround())
                {
                    impulse = (float) (this.speedModifier * getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else
                {
                    impulse = (float) (this.speedModifier * getAttributeValue(Attributes.FLYING_SPEED));
                }

                setSpeed(impulse);
                double horizontalDist = MathHelper.sqrt(distX * distX + distZ * distZ);
                float f2 = (float) (-(MathHelper.atan2(distY, horizontalDist) * (double) (180F / (float) Math.PI)));
                xRot = this.rotlerp(xRot, f2, (float) this.maxTurn);
                setYya(distY > 0.0D ? impulse : -impulse);
            } else
            {
                if (!this.hoversInPlace)
                {
                    setNoGravity(false);
                }

                setYya(0.0F);
                setZza(0.0F);
            }
        }
    }
}
