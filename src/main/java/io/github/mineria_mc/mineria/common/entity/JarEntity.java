package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.effects.util.PoisonSource;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import io.github.mineria_mc.mineria.common.items.JarItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class JarEntity extends ThrowableItemProjectile implements ItemSupplier {
    public JarEntity(EntityType<? extends JarEntity> type, Level world) {
        super(type, world);
    }

    @OnlyIn(Dist.CLIENT)
    public JarEntity(double x, double y, double z, Level world) {
        super(MineriaEntities.JAR.get(), x, y, z, world);
    }

    public JarEntity(LivingEntity living, Level world) {
        super(MineriaEntities.JAR.get(), living, world);
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return MineriaItems.JAR.get();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHit(HitResult rayTraceResult) {
        super.onHit(rayTraceResult);

        if (!this.level().isClientSide) {
            ItemStack stack = this.getItem();
            PoisonSource poisonSource = JarItem.getPoisonSourceFromStack(stack);

            if (this.isLingering()) {
//                this.makeAreaOfEffectCloud(stack, potion); TODO: Implement or not lingering on Jars
            } else {
                this.applySplash(poisonSource, rayTraceResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) rayTraceResult).getEntity() : null);
            }

            this.level().levelEvent(2002, this.blockPosition(), poisonSource.getColor());
            this.discard();
        }
    }

    private void applySplash(PoisonSource poisonSource, @Nullable Entity target) {
        AABB boundingBox = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, boundingBox);

        if (!entities.isEmpty()) {
            for (LivingEntity living : entities) {
                if (living.isAffectedByPotions()) {
                    double distanceSqrt = this.distanceToSqr(living);
                    if (distanceSqrt < 16.0D) {
                        double distance = 1.0D - Math.sqrt(distanceSqrt) / 4.0D;
                        if (living == target) {
                            distance = 1.0D;
                        }

                        poisonSource.applyPoisoning(living);
                    }
                }
            }
        }
    }

    /*private void makeAreaOfEffectCloud(ItemStack stack, Potion potion)
    {
        MineriaAreaEffectCloudEntity effectCloud = new MineriaAreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
        Entity owner = this.getOwner();

        if (owner instanceof LivingEntity)
        {
            effectCloud.setOwner((LivingEntity) owner);
        }

        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(-0.5F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-effectCloud.getRadius() / (float) effectCloud.getDuration());
        effectCloud.setPotion(potion);

        for (EffectInstance effect : PotionUtils.getCustomEffects(stack))
        {
            effectCloud.addEffect(CustomEffectInstance.copyEffect(effect));
        }

        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("CustomPotionColor", 99))
        {
            effectCloud.setFixedColor(nbt.getInt("CustomPotionColor"));
        }

        this.level.addFreshEntity(effectCloud);
    }*/

    private boolean isLingering() {
        return JarItem.isLingering(this.getItem());
    }
}
