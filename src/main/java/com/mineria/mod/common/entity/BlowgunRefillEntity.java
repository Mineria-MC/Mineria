package com.mineria.mod.common.entity;

import com.mineria.mod.common.effects.PoisonSource;
import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.util.DamageSourceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlowgunRefillEntity extends ProjectileEntity
{
    private static final DataParameter<String> POISON_SOURCE_ID = EntityDataManager.defineId(BlowgunRefillEntity.class, DataSerializers.STRING);
    private PoisonSource poisonSource = PoisonSource.UNKNOWN;
    private int life;

    public BlowgunRefillEntity(EntityType<? extends BlowgunRefillEntity> type, World world)
    {
        super(type, world);
    }

    public BlowgunRefillEntity(World world, double x, double y, double z)
    {
        this(MineriaEntities.DART.get(), world);
        this.setPos(x, y, z);
    }

    public BlowgunRefillEntity(World world, LivingEntity living, PoisonSource poisonSource)
    {
        this(world, living.getX(), living.getEyeY() - (double) 0.1F, living.getZ());
        setOwner(living);
        setPoisonSource(poisonSource);
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(POISON_SOURCE_ID, "mineria:unknown");
    }

    public void setPoisonSource(PoisonSource poisonSource)
    {
        this.poisonSource = poisonSource;
        this.entityData.set(POISON_SOURCE_ID, poisonSource.getId().toString());
    }

    public PoisonSource getPoisonSource()
    {
        return this.level.isClientSide ? PoisonSource.byName(ResourceLocation.tryParse(this.entityData.get(POISON_SOURCE_ID))) : this.poisonSource;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public void tick()
    {
        super.tick();
        Vector3d motion = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F)
        {
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(motion));
            this.yRot = (float) (MathHelper.atan2(motion.x, motion.z) * (double) (180F / (float) Math.PI));
            this.xRot = (float) (MathHelper.atan2(motion.y, (double) f) * (double) (180F / (float) Math.PI));
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }

        if (this.isInWaterOrRain())
        {
            this.clearFire();
        }

        tickDespawn();

        Vector3d pos = this.position();
        Vector3d motionPos = pos.add(motion);
        RayTraceResult result = this.level.clip(new RayTraceContext(pos, motionPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));

        if (result.getType() != RayTraceResult.Type.MISS)
            motionPos = result.getLocation();

        EntityRayTraceResult entityHit = this.findHitEntity(pos, motionPos);

        if (entityHit != null)
            result = entityHit;

        if (result.getType() == RayTraceResult.Type.ENTITY)
        {
            Entity hit = ((EntityRayTraceResult) result).getEntity();
            Entity owner = this.getOwner();
            if (hit instanceof PlayerEntity && owner instanceof PlayerEntity && !((PlayerEntity) owner).canHarmPlayer((PlayerEntity) hit))
            {
                result = null;
            }
        }

        if (result != null && result.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result))
        {
            this.onHit(result);
            this.hasImpulse = true;
        }

        motion = this.getDeltaMovement();

        double moX = motion.x;
        double moY = motion.y;
        double moZ = motion.z;
        double modX = this.getX() + moX;
        double modY = this.getY() + moY;
        double modZ = this.getZ() + moZ;
        float horizontalDistance = MathHelper.sqrt(getHorizontalDistanceSqr(motion));

        this.yRot = (float) (MathHelper.atan2(moX, moZ) * (double) (180F / (float) Math.PI));
        this.xRot = (float) (MathHelper.atan2(moY, (double) horizontalDistance) * (double) (180F / (float) Math.PI));
        this.xRot = lerpRotation(this.xRotO, this.xRot);
        this.yRot = lerpRotation(this.yRotO, this.yRot);

        float inertia = 0.99F;
        if (this.isInWater())
        {
            for (int j = 0; j < 4; ++j)
                this.level.addParticle(ParticleTypes.BUBBLE, modX - moX * 0.25D, modY - moY * 0.25D, modZ - moZ * 0.25D, moX, moY, moZ);

            inertia = 0.6F;
        }

        this.setDeltaMovement(motion.scale(inertia));
        if (!this.isNoGravity())
        {
            Vector3d motion2 = this.getDeltaMovement();
            this.setDeltaMovement(motion2.x, motion2.y - (double) 0.05F, motion2.z);
        }

        this.setPos(modX, modY, modZ);
        this.checkInsideBlocks();
    }

    protected void tickDespawn()
    {
        ++this.life;
        if (this.life >= 1200)
            this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result)
    {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        float motionLength = (float) this.getDeltaMovement().length();
        int damage = MathHelper.ceil(MathHelper.clamp((double) motionLength * 3.5D, 0.0D, 2.147483647E9D));

        Entity owner = this.getOwner();
        DamageSource source = DamageSourceUtil.bambooBlowgun(this, owner == null ? this : owner);

        if (owner instanceof LivingEntity)
            ((LivingEntity) owner).setLastHurtMob(hit);

        boolean isEnderman = hit.getType() == EntityType.ENDERMAN;

        if (hit.hurt(source, (float) damage))
        {
            if (isEnderman)
                return;

            if (hit instanceof LivingEntity)
            {
                LivingEntity living = (LivingEntity) hit;
                doPostHurtEffects(living);

                if (!this.level.isClientSide)
                    living.setArrowCount(living.getArrowCount() + 1);

                if (living != owner && living instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent())
                    ((ServerPlayerEntity) owner).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
            }

            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.remove();
        } else
        {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.yRot += 180.0F;
            this.yRotO += 180.0F;

            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D)
                this.remove();
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result)
    {
        super.onHitBlock(result);
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.remove();
    }

    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_)
    {
        return ProjectileHelper.getEntityHitResult(this.level, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    protected void doPostHurtEffects(LivingEntity living)
    {
        this.getPoisonSource().poison(living);
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public boolean isAttackable()
    {
        return false;
    }

    @Override
    public float getBrightness()
    {
        return 1.0F;
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt)
    {
        super.addAdditionalSaveData(nbt);
        nbt.putShort("life", (short)this.life);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt)
    {
        super.readAdditionalSaveData(nbt);
        this.life = nbt.getShort("life");
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        Entity owner = this.getOwner();
        return new SSpawnObjectPacket(this, owner == null ? 0 : owner.getId());
    }
}
