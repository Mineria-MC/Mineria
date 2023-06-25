package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.items.KunaiItem;
import io.github.mineria_mc.mineria.util.MineriaDamageSourceBuilder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KunaiEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.defineId(KunaiEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack kunaiItem = new ItemStack(MineriaItems.KUNAI.get());
    private boolean dealtDamage;
    public int clientSideReturnKunaiTickCount;

    public KunaiEntity(EntityType<? extends KunaiEntity> type, Level world) {
        super(type, world);
    }

    public KunaiEntity(LivingEntity living, Level world, ItemStack stack) {
        super(MineriaEntities.KUNAI.get(), living, world);
        this.kunaiItem = stack;
        this.entityData.set(ENCHANTED, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENCHANTED, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if (dealtDamage || isNoPhysics()) {
            if (this.kunaiItem.isEmpty())
                this.discard();
            else if (entity != null) {
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.discard();
                } else {
                    this.setNoPhysics(true);
                    Vec3 vec3 = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                    this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.075D, this.getZ());

                    if (this.level.isClientSide) {
                        this.yOld = this.getY();
                    }

                    double speed = 0.25D;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(speed)));
                    if (this.clientSideReturnKunaiTickCount == 0) {
                        this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                    }

                    ++this.clientSideReturnKunaiTickCount;
                }
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity owner = this.getOwner();
        if (owner != null && owner.isAlive()) {
            return !(owner instanceof ServerPlayer) || !owner.isSpectator();
        } else {
            return false;
        }
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return this.kunaiItem.copy();
    }

    public ItemStack getKunaiItem() {
        return kunaiItem;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEnchanted() {
        return this.entityData.get(ENCHANTED);
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vec0, Vec3 vec1) {
        return this.dealtDamage ? null : super.findHitEntity(vec0, vec1);
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTraceResult) {
        Entity target = rayTraceResult.getEntity();
        float dmg = KunaiItem.getRangedAttackDamage(this.kunaiItem);
        if (target instanceof LivingEntity) {
            dmg += EnchantmentHelper.getDamageBonus(this.kunaiItem, ((LivingEntity) target).getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource source = MineriaDamageSourceBuilder.get(level).kunai(this, (owner == null ? this : owner));
        this.dealtDamage = true;
        if (target.hurt(source, dmg)) {
            if (target.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingTarget, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, livingTarget);
                }

                this.doPostHurtEffects(livingTarget);
            }

            KunaiItem.onHitEntity(this.kunaiItem);
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));

        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Nonnull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(@Nonnull Player player) {
        Entity owner = this.getOwner();
        if (owner == null || owner.getUUID() == player.getUUID()) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        if (compound.contains("KunaiItem"))
            this.kunaiItem = ItemStack.of(compound.getCompound("KunaiItem"));
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("KunaiItem", this.kunaiItem.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    protected void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED)
            super.tickDespawn();
    }

    @Override
    protected float getWaterInertia() {
        return 0.45F;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }
}
