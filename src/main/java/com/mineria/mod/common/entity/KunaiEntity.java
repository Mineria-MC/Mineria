package com.mineria.mod.common.entity;

import com.mineria.mod.common.init.MineriaEntities;
import com.mineria.mod.common.init.MineriaItems;
import com.mineria.mod.common.items.KunaiItem;
import com.mineria.mod.util.DamageSourceUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class KunaiEntity extends AbstractArrowEntity
{
    private static final DataParameter<Boolean> ENCHANTED = EntityDataManager.defineId(KunaiEntity.class, DataSerializers.BOOLEAN);
    private ItemStack kunaiItem = new ItemStack(MineriaItems.KUNAI);
    private boolean dealtDamage;
    public int clientSideReturnKunaiTickCount;

    public KunaiEntity(EntityType<? extends KunaiEntity> type, World world)
    {
        super(type, world);
    }

    public KunaiEntity(LivingEntity living, World world, ItemStack stack)
    {
        super(MineriaEntities.KUNAI.get(), living, world);
        this.kunaiItem = stack;
        this.entityData.set(ENCHANTED, stack.hasFoil());
    }

    @OnlyIn(Dist.CLIENT)
    public KunaiEntity(double x, double y, double z, World world)
    {
        super(MineriaEntities.KUNAI.get(), x, y, z, world);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(ENCHANTED, false);
    }

    @Override
    public void tick()
    {
        if (this.inGroundTime > 4)
        {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if(dealtDamage || isNoPhysics())
        {
            if(this.kunaiItem.isEmpty())
                this.remove();
            else if(entity != null)
            {
                if (!this.isAcceptibleReturnOwner())
                {
                    if (!this.level.isClientSide && this.pickup == AbstractArrowEntity.PickupStatus.ALLOWED)
                    {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.remove();
                }
                else
                {
                    this.setNoPhysics(true);
                    Vector3d vec3 = new Vector3d(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                    this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.075D, this.getZ());

                    if (this.level.isClientSide)
                    {
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

    private boolean isAcceptibleReturnOwner()
    {
        Entity owner = this.getOwner();
        if (owner != null && owner.isAlive())
        {
            return !(owner instanceof ServerPlayerEntity) || !owner.isSpectator();
        } else
        {
            return false;
        }
    }

    @Override
    protected ItemStack getPickupItem()
    {
        return this.kunaiItem.copy();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEnchanted()
    {
        return this.entityData.get(ENCHANTED);
    }

    @Override
    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d vec0, Vector3d vec1)
    {
        return this.dealtDamage ? null : super.findHitEntity(vec0, vec1);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult)
    {
        Entity target = rayTraceResult.getEntity();
        float dmg = KunaiItem.getRangedAttackDamage(this.kunaiItem);
        if (target instanceof LivingEntity)
        {
            dmg += EnchantmentHelper.getDamageBonus(this.kunaiItem, ((LivingEntity) target).getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource source = DamageSourceUtil.kunai(this, (owner == null ? this : owner));
        this.dealtDamage = true;
        if (target.hurt(source, dmg))
        {
            if (target.getType() == EntityType.ENDERMAN)
            {
                return;
            }

            if (target instanceof LivingEntity)
            {
                LivingEntity livingTarget = (LivingEntity) target;
                if (owner instanceof LivingEntity)
                {
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

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent()
    {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(PlayerEntity player)
    {
        Entity owner = this.getOwner();
        if (owner == null || owner.getUUID() == player.getUUID())
        {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound)
    {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        if(compound.contains("KunaiItem"))
            this.kunaiItem = ItemStack.of(compound.getCompound("KunaiItem"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound)
    {
        super.addAdditionalSaveData(compound);
        compound.put("KunaiItem", this.kunaiItem.save(new CompoundNBT()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    protected void tickDespawn()
    {
        if(this.pickup != PickupStatus.ALLOWED)
            super.tickDespawn();
    }

    @Override
    protected float getWaterInertia()
    {
        return 0.45F;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_)
    {
        return true;
    }
}
