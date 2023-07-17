package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.util.MineriaDamageSourceBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlowgunRefillEntity extends Projectile {
    private static final EntityDataAccessor<String> POISON_SOURCE_ID = SynchedEntityData.defineId(BlowgunRefillEntity.class, EntityDataSerializers.STRING);
    private PoisonSource poisonSource = PoisonSource.UNKNOWN;
    private int life;

    public BlowgunRefillEntity(EntityType<? extends BlowgunRefillEntity> type, Level world) {
        super(type, world);
    }

    public BlowgunRefillEntity(Level world, double x, double y, double z) {
        this(MineriaEntities.DART.get(), world);
        this.setPos(x, y, z);
    }

    public BlowgunRefillEntity(Level world, LivingEntity living, PoisonSource poisonSource) {
        this(world, living.getX(), living.getEyeY() - (double) 0.1F, living.getZ());
        setOwner(living);
        setPoisonSource(poisonSource);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(POISON_SOURCE_ID, "mineria:unknown");
    }

    public void setPoisonSource(PoisonSource poisonSource) {
        this.poisonSource = poisonSource;
        this.entityData.set(POISON_SOURCE_ID, poisonSource.getId().toString());
    }

    public PoisonSource getPoisonSource() {
        return this.level().isClientSide ? PoisonSource.byName(ResourceLocation.tryParse(this.entityData.get(POISON_SOURCE_ID))) : this.poisonSource;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.getBoundingBox().getSize() * 10.0D;
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double f = Math.sqrt(motion.horizontalDistanceSqr());
            setYRot((float) (Mth.atan2(motion.x, motion.z) * (double) (180F / (float) Math.PI)));
            setXRot((float) (Mth.atan2(motion.y, f) * (double) (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (this.isInWaterOrRain()) {
            this.clearFire();
        }

        tickDespawn();

        Vec3 pos = this.position();
        Vec3 motionPos = pos.add(motion);
        HitResult result = this.level().clip(new ClipContext(pos, motionPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (result.getType() != HitResult.Type.MISS)
            motionPos = result.getLocation();

        EntityHitResult entityHit = this.findHitEntity(pos, motionPos);

        if (entityHit != null)
            result = entityHit;

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) result).getEntity();
            Entity owner = this.getOwner();
            if (hit instanceof Player && owner instanceof Player && !((Player) owner).canHarmPlayer((Player) hit)) {
                result = null;
            }
        }

        if (result != null && result.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
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
        double horizontalDistance = Math.sqrt(motion.horizontalDistanceSqr());

        setYRot((float) (Mth.atan2(moX, moZ) * (double) (180F / (float) Math.PI)));
        setXRot((float) (Mth.atan2(moY, horizontalDistance) * (double) (180F / (float) Math.PI)));
        setXRot(lerpRotation(this.xRotO, this.getXRot()));
        setYRot(lerpRotation(this.yRotO, this.getYRot()));

        float inertia = 0.99F;
        if (this.isInWater()) {
            for (int j = 0; j < 4; ++j) {
                this.level().addParticle(ParticleTypes.BUBBLE, modX - moX * 0.25D, modY - moY * 0.25D, modZ - moZ * 0.25D, moX, moY, moZ);
            }

            inertia = 0.6F;
        }

        this.setDeltaMovement(motion.scale(inertia));
        if (!this.isNoGravity()) {
            Vec3 motion2 = this.getDeltaMovement();
            this.setDeltaMovement(motion2.x, motion2.y - (double) 0.05F, motion2.z);
        }

        this.setPos(modX, modY, modZ);
        this.checkInsideBlocks();
    }

    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 1200)
            this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity hit = result.getEntity();
        float motionLength = (float) this.getDeltaMovement().length();
        int damage = Mth.ceil(Mth.clamp((double) motionLength * 3.5D, 0.0D, 2.147483647E9D));

        Entity owner = this.getOwner();
        DamageSource source = MineriaDamageSourceBuilder.get(level()).bambooBlowgun(this, owner == null ? this : owner);

        if (owner instanceof LivingEntity)
            ((LivingEntity) owner).setLastHurtMob(hit);

        boolean isEnderman = hit.getType() == EntityType.ENDERMAN;

        if (hit.hurt(source, (float) damage)) {
            if (isEnderman)
                return;

            if (hit instanceof LivingEntity living) {
                doPostHurtEffects(living);

                if (!this.level().isClientSide)
                    living.setArrowCount(living.getArrowCount() + 1);

                if (living != owner && living instanceof Player && owner instanceof ServerPlayer && !this.isSilent())
                    ((ServerPlayer) owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
            }

            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            setYRot(getYRot() + 180.0F);
            this.yRotO += 180.0F;

            if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D)
                this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.discard();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_213866_1_, Vec3 p_213866_2_) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, p_213866_1_, p_213866_2_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    protected void doPostHurtEffects(LivingEntity living) {
        this.getPoisonSource().applyPoisoning(living);
    }

    @Override
    protected @NotNull MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putShort("life", (short) this.life);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.life = nbt.getShort("life");
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity owner = this.getOwner();
        return new ClientboundAddEntityPacket(this, owner == null ? 0 : owner.getId());
    }
}
