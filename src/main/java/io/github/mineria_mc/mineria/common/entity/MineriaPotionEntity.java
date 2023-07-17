package io.github.mineria_mc.mineria.common.entity;

import io.github.mineria_mc.mineria.common.effects.instances.ModdedMobEffectInstance;
import io.github.mineria_mc.mineria.common.init.MineriaEntities;
import io.github.mineria_mc.mineria.common.init.MineriaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class MineriaPotionEntity extends ThrowableItemProjectile implements ItemSupplier {
    public MineriaPotionEntity(EntityType<? extends MineriaPotionEntity> type, Level world) {
        super(type, world);
    }

    public MineriaPotionEntity(Level world, LivingEntity living) {
        super(MineriaEntities.MINERIA_POTION.get(), living, world);
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return MineriaItems.MINERIA_SPLASH_POTION.get();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult rayTraceResult) {
        super.onHitBlock(rayTraceResult);
        if (!this.level().isClientSide) {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean flag = potion == Potions.WATER && list.isEmpty();
            Direction direction = rayTraceResult.getDirection();
            BlockPos blockpos = rayTraceResult.getBlockPos();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (flag) {
                this.dowseFire(blockpos1);
                this.dowseFire(blockpos1.relative(direction.getOpposite()));

                for (Direction direction1 : Direction.Plane.HORIZONTAL) {
                    this.dowseFire(blockpos1.relative(direction1));
                }
            }

        }
    }

    @Override
    protected void onHit(@NotNull HitResult rayTraceResult) {
        super.onHit(rayTraceResult);
        if (!this.level().isClientSide) {
            ItemStack itemstack = this.getItem();
            Potion potion = PotionUtils.getPotion(itemstack);
            List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
            boolean water = potion == Potions.WATER && list.isEmpty();
            if (water) {
                this.applyWater();
            } else if (!list.isEmpty()) {
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud(itemstack, potion);
                } else {
                    this.applySplash(list, rayTraceResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) rayTraceResult).getEntity() : null);
                }
            }

            int i = potion.hasInstantEffects() ? 2007 : 2002;
            this.level().levelEvent(i, this.blockPosition(), PotionUtils.getColor(itemstack));
            this.discard();
        }
    }

    private void applyWater() {
        AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb, LivingEntity::isSensitiveToWater);
        if (!list.isEmpty()) {
            for (LivingEntity livingentity : list) {
                double d0 = this.distanceToSqr(livingentity);
                if (d0 < 16.0D && livingentity.isSensitiveToWater()) {
                    livingentity.hurt(damageSources().indirectMagic(livingentity, this.getOwner()), 1.0F);
                }
            }
        }
    }

    private void applySplash(List<MobEffectInstance> effects, @Nullable Entity target) {
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

                        for (MobEffectInstance instance : effects) {
                            MobEffect effect = instance.getEffect();
                            if (effect.isInstantenous()) {
                                effect.applyInstantenousEffect(this, this.getOwner(), living, instance.getAmplifier(), distance);
                            } else {
                                int newDuration = (int) (distance * (double) instance.getDuration() + 0.5D);
                                if (newDuration > 20) {
                                    if (instance instanceof ModdedMobEffectInstance) {
                                        ((ModdedMobEffectInstance) instance).setDuration(newDuration);
                                        living.addEffect(instance);
                                    } else
                                        living.addEffect(new MobEffectInstance(effect, newDuration, instance.getAmplifier(), instance.isAmbient(), instance.isVisible()));
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void makeAreaOfEffectCloud(ItemStack stack, Potion potion) {
        MineriaAreaEffectCloudEntity effectCloud = new MineriaAreaEffectCloudEntity(this.level(), this.getX(), this.getY(), this.getZ());
        Entity owner = this.getOwner();

        if (owner instanceof LivingEntity) {
            effectCloud.setOwner((LivingEntity) owner);
        }

        effectCloud.setRadius(3.0F);
        effectCloud.setRadiusOnUse(-0.5F);
        effectCloud.setWaitTime(10);
        effectCloud.setRadiusPerTick(-effectCloud.getRadius() / (float) effectCloud.getDuration());
        effectCloud.setPotion(potion);

        for (MobEffectInstance effect : PotionUtils.getCustomEffects(stack)) {
            effectCloud.addEffect(ModdedMobEffectInstance.copyEffect(effect));
        }

        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("CustomPotionColor", 99)) {
            effectCloud.setFixedColor(nbt.getInt("CustomPotionColor"));
        }

        this.level().addFreshEntity(effectCloud);
    }

    private boolean isLingering() {
        return this.getItem().is(MineriaItems.MINERIA_LINGERING_POTION.get());
    }

    private void dowseFire(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (state.is(BlockTags.FIRE)) {
            this.level().removeBlock(pos, false);
        } else if (CampfireBlock.isLitCampfire(state)) {
            this.level().levelEvent(null, 1009, pos, 0);
            CampfireBlock.dowse(this.getOwner(), this.level(), pos, state);
            this.level().setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }
}
